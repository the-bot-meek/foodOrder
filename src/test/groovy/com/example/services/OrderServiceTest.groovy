package com.example.services

import com.example.Exceptions.OrderRequestConverterException
import com.example.dto.request.CreateOrderRequest
import com.example.models.Meal.Meal
import com.example.models.MenuItem
import com.example.models.Order
import com.example.models.Venue
import io.micronaut.security.authentication.Authentication
import spock.lang.Specification

import java.time.Instant

class OrderServiceTest extends Specification {

    private Authentication getUserAuth(String uid) {
        Authentication authentication = Mock(Authentication)
        authentication.getAttributes() >> Map.of("preferred_username", "usename")
        authentication.getName() >> uid
        return authentication
    }

    def "ConvertCreateOrderRequestToOrder"() {
        given:
        String mealId = "797b001f-de8f-47ed-833a-d84e61c73fe7"
        Instant dateOfMeal = Instant.ofEpochSecond(1711487392)
        String uid = "d84e61c73fe7-de8f-47ed-833a-797b001f"
        String preferredUsername = "Steven"
        String organizerUid = "384n4uc73fe7-de8f-47ed-833a-797b001f"
        String location = "London"
        String name = "MacD"
        Set<MenuItem> menuItems = [new MenuItem(name: "name", description: "description", price: 1.0)]
        CreateOrderRequest createOrderRequest = new CreateOrderRequest(dateOfMeal, mealId, menuItems, organizerUid)


        IDynamoDBFacadeService orderServiceIDynamoDBFacadeService = Mock(IDynamoDBFacadeService)
        MealService mealService = new MealService(orderServiceIDynamoDBFacadeService, null)
        Meal meal = new Meal(id: mealId, mealDate: dateOfMeal, location: location, venueName: name)
        orderServiceIDynamoDBFacadeService.load(Meal.class, organizerUid, dateOfMeal.toString() + "_" + mealId) >> {
            return Optional.of(meal)
        }

        IDynamoDBFacadeService venueServiceIDynamoDBFacadeService = Mock(IDynamoDBFacadeService)
        VenueService venueService = new VenueService(null, venueServiceIDynamoDBFacadeService)
        venueServiceIDynamoDBFacadeService.load(Venue.class, "Venue_" + location, name) >> {
            return Optional.of(
                    new Venue(menuItems: List.of(
                            new MenuItem(name: "name", description: "description", price: 1.0)
                    )
                )
            )
        }
        OrderService orderService = new OrderService(null, venueService, mealService)


        when:
        Order order = orderService.convertCreateOrderRequestToOrder(createOrderRequest, uid, preferredUsername)

        then:
        order == new Order(meal: meal, uid: uid, menuItems: menuItems, participantsName: preferredUsername)
    }

    def "ConvertCreateOrderRequestToOrder with invalid mealId organizerUid/sort key"() {
        given:
        String mealId = "797b001f-de8f-47ed-833a-d84e61c73fe7"
        Instant dateOfMeal = Instant.ofEpochSecond(1711487392)
        String uid = "d84e61c73fe7-de8f-47ed-833a-797b001f"
        String preferredUsername = "Steven"
        String organizerUid = "384n4uc73fe7-de8f-47ed-833a-797b001f"
        MenuItem menuItem = new MenuItem(
                name: "name",
                description: "description",
                price: 1.0
        )
        CreateOrderRequest createOrderRequest = new CreateOrderRequest(dateOfMeal, mealId, Set.of(menuItem), organizerUid)

        IDynamoDBFacadeService orderServiceIDynamoDBFacadeService = Mock(IDynamoDBFacadeService)
        MealService mealService = new MealService(orderServiceIDynamoDBFacadeService, null)
        orderServiceIDynamoDBFacadeService.load(Meal.class, organizerUid, dateOfMeal.toString() + "_" + mealId) >> {
            return Optional.empty()
        }
        OrderService orderService = new OrderService(null, null, mealService)


        when:
        orderService.convertCreateOrderRequestToOrder(createOrderRequest, uid, preferredUsername)

        then:
        thrown(OrderRequestConverterException)
    }

    def "ConvertCreateOrderRequestToOrder with invalid mealId menuItems"(Integer index, Optional<Venue> venue) {
        given:
        String mealId = "797b001f-de8f-47ed-833a-d84e61c73fe7"
        Instant dateOfMeal = Instant.ofEpochSecond(1711487392)
        String uid = "d84e61c73fe7-de8f-47ed-833a-797b001f"
        String preferredUsername = "Steven"
        String organizerUid = "384n4uc73fe7-de8f-47ed-833a-797b001f"
        String location = "London"
        String name = "MacD"
        MenuItem menuItem = new MenuItem(
                name: "name",
                description: "description",
                price: 1.0
        )
        CreateOrderRequest createOrderRequest = new CreateOrderRequest(dateOfMeal, mealId, Set.of(menuItem), organizerUid)

        IDynamoDBFacadeService orderServiceIDynamoDBFacadeService = Mock(IDynamoDBFacadeService)
        MealService mealService = new MealService(orderServiceIDynamoDBFacadeService, null)
        orderServiceIDynamoDBFacadeService.load(Meal.class, organizerUid, dateOfMeal.toString() + "_" + mealId) >> {
            return Optional.of(new Meal(location: location, venueName: name))
        }

        IDynamoDBFacadeService venueServiceIDynamoDBFacadeService = Mock(IDynamoDBFacadeService)
        VenueService venueService = new VenueService(null, venueServiceIDynamoDBFacadeService)
        venueServiceIDynamoDBFacadeService.load(Venue.class, "Venue_" + location, name) >> {
            return venue
        }
        OrderService orderService = new OrderService(null, venueService, mealService)


        when:
        orderService.convertCreateOrderRequestToOrder(createOrderRequest, uid, preferredUsername)

        then:
        thrown(OrderRequestConverterException)
        
        where:
        index | venue
        1     | Optional.of(new Venue(menuItems: List.of(new MenuItem(name: "name", description: "description", price: 5.0))))
        2     | Optional.of(new Venue(menuItems: new ArrayList<MenuItem>()))
    }
}
