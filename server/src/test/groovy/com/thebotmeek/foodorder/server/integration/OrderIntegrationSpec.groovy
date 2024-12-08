package com.thebotmeek.foodorder.server.integration

import com.foodorder.server.client.MealClient
import com.foodorder.server.client.OrderClient
import com.foodorder.server.client.VenueClient
import com.foodorder.server.request.CreateMealRequest
import com.foodorder.server.request.CreateOrderRequest
import com.foodorder.server.request.CreateVenueRequest
import com.foodorder.server.models.meal.Meal
import com.foodorder.server.models.MenuItem
import com.foodorder.server.models.Order
import com.foodorder.server.models.meal.MealConfig
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.IgnoreIf
import spock.lang.Specification

import java.time.Instant


// 2024-03-26T21:09:52Z_e44bb7fc-7164-45e9-8c28-3c561baa7d90

@MicronautTest(environments = ["mock_auth"])
@IgnoreIf({System.getenv("requireIntegrationTests") != 'true'})
class OrderIntegrationSpec extends Specification {
    @Inject
    OrderClient orderClient

    @Inject
    VenueClient venueClient

    @Inject
    MealClient mealClient

    // 2024-03-26T21:09:52Z_0e48ce76-9714-47a5-ba02-9eb90cbe15c0
    def "Add order"() {
        given:
        Instant dateOfMeal = Instant.ofEpochSecond(1711487392)
        String location = "London"
        String name = "MacD"

        Set<MenuItem> menuItems = [new MenuItem()]
        CreateVenueRequest createVenueRequest = new CreateVenueRequest(menuItems, location, name, "description")
        CreateMealRequest createMealRequest = new CreateMealRequest(name: name, dateOfMeal: dateOfMeal, location: location, venueName: name)


        when:
        venueClient.addVenue(createVenueRequest)
        Meal meal = mealClient.addMeal(createMealRequest)

        CreateOrderRequest createOrderRequest = new CreateOrderRequest(dateOfMeal, meal.getId(), menuItems, "steven")
        Order order = orderClient.addOrder(createOrderRequest)

        then:
        assert order.getUid() == "steven"
        assert order.getSortKey() == "steven"
        assert order.getParticipantsName() == "The bot meek"
        assert order.getMenuItems() == menuItems
        assert order.getGSIPrimaryKey() == "Order_steven"
        assert order.getPrimaryKey() == "Order_${meal.getId()}"
        assert order.getMeal() == meal
    }
}
