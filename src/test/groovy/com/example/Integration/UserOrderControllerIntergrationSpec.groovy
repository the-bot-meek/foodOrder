package com.example.Integration

import com.example.client.MealClient
import com.example.client.OrderClient
import com.example.client.UserClient
import com.example.client.VenueClient
import com.example.dto.request.CreateMealRequest
import com.example.dto.request.CreateOrderRequest
import com.example.dto.request.CreateVenueRequest
import com.example.models.Meal
import com.example.models.MenuItem
import com.example.models.Order
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.IgnoreIf
import spock.lang.Specification

import java.time.Instant

@MicronautTest(environments = ["integration"])
@IgnoreIf({System.getenv("requireIntegrationTests") != 'true'})
class UserOrderControllerIntergrationSpec extends Specification {
    @Inject
    MealClient mealClient

    @Inject
    UserClient userClient

    @Inject
    OrderClient orderClient

    @Inject
    VenueClient venueClient

    def "Get all meals for User"() {
        given:
        Instant dateOfMeal = Instant.ofEpochSecond(1711487392)
        String location = "London"
        String name = "MacD"

        Set<MenuItem> menuItems = [new MenuItem()]
        CreateVenueRequest createVenueRequest = new CreateVenueRequest(menuItems, location, name, "description")
        CreateMealRequest createMealRequest = new CreateMealRequest(name, dateOfMeal, location, name)


        when:
        venueClient.addVenue(createVenueRequest)
        Meal meal = mealClient.addMeal(createMealRequest)

        CreateOrderRequest createOrderRequest = new CreateOrderRequest(dateOfMeal, meal.getId(), menuItems, "steven")
        Order order = orderClient.addOrder(createOrderRequest)
        List<Order> orderList = userClient.listOrders()

        then:
        assert orderList.find{Order it -> it.getId() == order.getId()} == order
    }
}
