package com.thebotmeek.foodorder.server.controllers

import com.foodorder.server.controllers.OrderController
import com.foodorder.server.converters.CreateOrderRequestConverter
import com.foodorder.server.request.CreateOrderRequest
import com.foodorder.server.models.meal.Meal
import com.foodorder.server.models.MenuItem
import com.foodorder.server.models.Order
import com.foodorder.server.models.Venue
import com.foodorder.server.models.meal.MealConfig
import com.foodorder.server.repository.IDynamoDBFacadeRepository
import com.foodorder.server.repository.MealRepository
import com.foodorder.server.repository.OrderRepository
import com.foodorder.server.repository.VenueRepository
import io.micronaut.security.authentication.Authentication
import spock.lang.Specification
import java.time.Instant

class OrderControllerSpec extends Specification {
    private Authentication mockAuthentication(String name) {
        Authentication authentication = Mock(Authentication)
        authentication.getName() >> name
        return authentication
    }

    def "AddOrder"() {
        given:
        String mealId = "797b001f-de8f-47ed-833a-d84e61c73fe7"
        Instant dateOfMeal = Instant.ofEpochSecond(1711487392)
        String uid = "d84e61c73fe7-de8f-47ed-833a-797b001f"
        String organizerUid = "d02761c73fe7-de8f-47ed-833a-739b001f"
        String location = "London"
        String name = "MacD"


        IDynamoDBFacadeRepository mealServiceIDynamoDBFacadeService = Mock(IDynamoDBFacadeRepository)
        MealRepository mealService = new MealRepository(mealServiceIDynamoDBFacadeService)
        mealServiceIDynamoDBFacadeService.load(Meal, "Meal_" + organizerUid, (dateOfMeal.toString() + "_" + mealId)) >> {
            return Optional.of(new Meal(location: location, venueName: name, id: mealId, mealDate: dateOfMeal, mealConfig: new MealConfig()))
        }

        IDynamoDBFacadeRepository venueServiceIDynamoDBFacadeService = Mock(IDynamoDBFacadeRepository)
        VenueRepository venueService = new VenueRepository(venueServiceIDynamoDBFacadeService)
        venueServiceIDynamoDBFacadeService.load(Venue.class, "Venue_" + location, name) >> {
            return Optional.of(
                    new Venue(menuItems: List.of(
                            new MenuItem(name: "name", description: "description", price: 1.0)
                    )
                )
            )
        }

        IDynamoDBFacadeRepository dynamoDBFacadeService = Mock(IDynamoDBFacadeRepository)
        CreateOrderRequestConverter createOrderRequestConverter = new CreateOrderRequestConverter(mealService, venueService)
        OrderRepository orderService = new OrderRepository(dynamoDBFacadeService)
        OrderController orderController = new OrderController(orderService, createOrderRequestConverter)
        Authentication authentication = Mock(Authentication)
        authentication.getAttributes() >> Map.of("name", "usename")
        authentication.getName() >> uid

        Set<MenuItem> menuItems = Set.of(
                new MenuItem(name: "name", description: "description", price: 1.0)
        )
        CreateOrderRequest createOrderRequest = new CreateOrderRequest(
                dateOfMeal,
                mealId,
                menuItems,
                organizerUid
        )

        when:
        orderController.addOrder(createOrderRequest, authentication)

        then:
        1 * dynamoDBFacadeService.save({Order order ->
            assert order.getUid() == uid
            assert order.getMenuItems() == menuItems
            assert order.getParticipantsName() == "usename"
            order.getMeal().with {Meal meal ->
                assert meal.getId() == mealId
                assert meal.getMealDate() == dateOfMeal
            }

        })
    }
}
