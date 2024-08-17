package com.example.Integration

import com.example.client.MealClient
import com.example.client.OrderClient
import com.example.client.VenueClient
import com.example.dto.request.CreateMealRequest
import com.example.dto.request.CreateOrderRequest
import com.example.dto.request.CreateVenueRequest
import com.example.models.meal.Meal
import com.example.models.MenuItem
import com.example.models.Order
import com.example.models.meal.MealConfig
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
        CreateVenueRequest createVenueRequest = new CreateVenueRequest(menuItems, location, name, "description")
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
