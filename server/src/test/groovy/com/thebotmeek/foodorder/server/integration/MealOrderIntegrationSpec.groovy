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

@MicronautTest(environments = ["mock_auth"])
@IgnoreIf({System.getenv("requireIntegrationTests") != 'true'})
class MealOrderIntegrationSpec extends Specification {
    @Inject
    OrderClient orderClient

    @Inject
    VenueClient venueClient

    @Inject
    MealClient mealClient
    def "Get all orders for meal"() {
        given:
        Instant dateOfMeal = Instant.ofEpochSecond(1711487392)
        String location = "London"
        String name = "MacD"

        Set<MenuItem> menuItems = [new MenuItem(name: "name", description: "description", price: 1.01)]
        CreateVenueRequest createVenueRequest = new CreateVenueRequest(menuItems, location, name, "description", "+44 20 7123 4567")
        CreateMealRequest createMealRequest = new CreateMealRequest(name: name, dateOfMeal: dateOfMeal, location: location, venueName: name, mealConfig: new MealConfig())

        when:
        venueClient.addVenue(createVenueRequest)
        Meal meal = mealClient.addMeal(createMealRequest)

        CreateOrderRequest createOrderRequest = new CreateOrderRequest(dateOfMeal, meal.getId(), menuItems, "steven")
        Order order = orderClient.addOrder(createOrderRequest)

        List<Order> orderList = mealClient.listAllOrdersForMeal(meal.getId())

        then:
        assert orderList.find{Order it -> it.getId() == order.getId()} == order
    }
}
