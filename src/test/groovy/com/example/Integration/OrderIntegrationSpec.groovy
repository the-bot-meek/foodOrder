package com.example.Integration

import com.example.client.MealClient
import com.example.client.OrderClient
import com.example.client.VenueClient
import com.example.dto.request.CreateMealRequest
import com.example.dto.request.CreateOrderRequest
import com.example.dto.request.CreateVenueRequest
import com.example.models.Meal
import com.example.models.MenuItem
import com.example.models.Order
import com.example.models.Venue
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.Ignore
import spock.lang.IgnoreIf
import spock.lang.Specification

import java.time.Instant


// 2024-03-26T21:09:52Z_e44bb7fc-7164-45e9-8c28-3c561baa7d90

@MicronautTest(environments = ["integration"])
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
        CreateMealRequest createMealRequest = new CreateMealRequest(name, dateOfMeal, location, name)


        when:
        Venue createVenueResp = venueClient.addVenue(createVenueRequest)
        Meal meal = mealClient.addMeal(createMealRequest);

        CreateOrderRequest createOrderRequest = new CreateOrderRequest(dateOfMeal, meal.getId(), menuItems, "steven")
        Order order = orderClient.addOrder(createOrderRequest)

        then:
        assert order.getUid() == "steven"
        assert order.getSortKey() =="2024-03-26T21:09:52Z"
        assert order.getParticipantsName() == "The bot meek"
        assert order.getMenuItems() == menuItems
        assert order.getGSIPrimaryKey() == "Order_steven"
        assert order.getPrimaryKey() == "Order_${meal.getId()}"
        assert order.getMeal() == meal
    }
}
