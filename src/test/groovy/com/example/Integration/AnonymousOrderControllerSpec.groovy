package com.example.Integration

import com.example.client.AnonymousOrderClient
import com.example.client.MealClient
import com.example.client.VenueClient
import com.example.dto.request.CreateMealRequest
import com.example.dto.request.CreateOrderRequest
import com.example.dto.request.CreateVenueRequest
import com.example.models.AnonymousOrder
import com.example.models.MenuItem
import com.example.models.meal.Meal
import com.example.models.meal.MealConfig
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.IgnoreIf
import spock.lang.Specification

import java.time.Instant

@MicronautTest(environments = ["mock_auth"])
@IgnoreIf({System.getenv("requireIntegrationTests") != 'true'})
class AnonymousOrderControllerSpec extends Specification{
    @Inject
    VenueClient venueClient

    @Inject
    MealClient mealClient

    @Inject
    AnonymousOrderClient anonymousOrderClient

    def "test adding anonymous order"() {
        given:
        Instant dateOfMeal = Instant.ofEpochSecond(1711487392)
        String location = "London"
        String name = "MacD"
        String anonymousUserId = "3b0833d9-7e08-4ce4-8bd4-fba1291bef95"

        Set<MenuItem> menuItems = [new MenuItem(name: "name", description: "description", price: 1.01)]
        CreateVenueRequest createVenueRequest = new CreateVenueRequest(menuItems, location, name, "description")
        CreateMealRequest createMealRequest = new CreateMealRequest(name: name, dateOfMeal: dateOfMeal, location: location, venueName: name, mealConfig: new MealConfig())

        when:
        venueClient.addVenue(createVenueRequest)
        Meal meal = mealClient.addMeal(createMealRequest)

        CreateOrderRequest createOrderRequest = new CreateOrderRequest(dateOfMeal, meal.getId(), menuItems, "steven")
        AnonymousOrder anonymousOrder = anonymousOrderClient.addAnonymousOrder(createOrderRequest, anonymousUserId)

        then:
        assert anonymousOrder.getUid() == anonymousUserId
        assert anonymousOrder.getSortKey() =="2024-03-26T21:09:52Z"
        assert anonymousOrder.getParticipantsName() == "AnonymousUser"
        assert anonymousOrder.getMenuItems() == menuItems
        assert anonymousOrder.getGSIPrimaryKey() == "AnonymousOrder_${anonymousUserId}"
        assert anonymousOrder.getPrimaryKey() == "Order_${meal.getId()}"
        assert anonymousOrder.getMeal() == meal
    }

    def "test getting anonymous order"() {
        given:
        Instant dateOfMeal = Instant.ofEpochSecond(1711487392)
        String location = "London"
        String name = "MacD"
        String anonymousUserId = "3b0833d9-7e08-4ce4-8bd4-fba1291bef95";

        Set<MenuItem> menuItems = [new MenuItem(name: "name", description: "description", price: 1.01)]
        CreateVenueRequest createVenueRequest = new CreateVenueRequest(menuItems, location, name, "description")
        CreateMealRequest createMealRequest = new CreateMealRequest(name: name, dateOfMeal: dateOfMeal, location: location, venueName: name, mealConfig: new MealConfig())

        when:
        venueClient.addVenue(createVenueRequest)
        Meal meal = mealClient.addMeal(createMealRequest)

        CreateOrderRequest createOrderRequest = new CreateOrderRequest(dateOfMeal, meal.getId(), menuItems, "steven")
        AnonymousOrder anonymousOrderCreated = anonymousOrderClient.addAnonymousOrder(createOrderRequest, anonymousUserId)
        Optional<AnonymousOrder> anonymousOrder = anonymousOrderClient.getAnonymousOrder(anonymousOrderCreated.getUid(), anonymousOrderCreated.getMeal().getId())

        then:
        assert anonymousOrder.isPresent()
        assert anonymousOrder.get().getUid() == anonymousUserId
        assert anonymousOrder.get().getSortKey() =="2024-03-26T21:09:52Z"
        assert anonymousOrder.get().getParticipantsName() == "AnonymousUser"
        assert anonymousOrder.get().getMenuItems() == menuItems
        assert anonymousOrder.get().getGSIPrimaryKey() == "AnonymousOrder_${anonymousUserId}"
        assert anonymousOrder.get().getPrimaryKey() == "Order_${meal.getId()}"
        assert anonymousOrder.get().getMeal() == meal
    }
}
