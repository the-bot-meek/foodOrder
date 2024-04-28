package com.example.services

import com.example.dto.request.CreateOrderRequest
import com.example.models.Meal
import com.example.models.MenuItem
import com.example.models.Order
import com.example.models.Venue
import io.micronaut.security.authentication.Authentication
import spock.lang.Specification

import java.time.Instant

class OrderServiceTest extends Specification {
    def "ConvertCreateOrderRequestToOrder"() {
        given:
        String mealId = "797b001f-de8f-47ed-833a-d84e61c73fe7"
        Instant dateOfMeal = Instant.ofEpochSecond(1711487392)
        String uid = "d84e61c73fe7-de8f-47ed-833a-797b001f"
        String organizerUid = "384n4uc73fe7-de8f-47ed-833a-797b001f"
        String location = "London"
        String name = "MacD"
        MenuItem menuItem = new MenuItem(
                name: "name",
                description: "description",
                price: 1.0
        )
        CreateOrderRequest createOrderRequest = new CreateOrderRequest(dateOfMeal, mealId, List.of(menuItem), organizerUid)
        Authentication authentication = Mock(Authentication)
        authentication.getAttributes() >> Map.of("preferred_username", "usename")
        authentication.getName() >> uid

        IDynamoDBFacadeService orderServiceIDynamoDBFacadeService = Mock(IDynamoDBFacadeService)
        MealService mealService = new MealService(orderServiceIDynamoDBFacadeService)
        orderServiceIDynamoDBFacadeService.load(Meal.class, organizerUid, dateOfMeal.toString() + "_" + mealId) >> {
            return Optional.of(new Meal(location: location, venueName: name))
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
        Order order = orderService.convertCreateOrderRequestToOrder(createOrderRequest, authentication)

        then:
        order == new Order(mealId: mealId, dateOfMeal: dateOfMeal, uid: uid)
    }
}
