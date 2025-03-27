package com.thebotmeek.foodorder.server.integration

import com.foodorder.server.client.AnonymousOrderClient
import com.foodorder.server.client.MealClient
import com.foodorder.server.client.MenuClient
import com.foodorder.server.models.Order
import com.foodorder.server.request.CreateMealRequest
import com.foodorder.server.request.CreateOrderRequest
import com.foodorder.server.request.CreateMenuRequest

import com.foodorder.server.models.MenuItem
import com.foodorder.server.models.meal.Meal
import com.foodorder.server.models.meal.MealConfig
import io.micronaut.serde.ObjectMapper
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.IgnoreIf
import spock.lang.Specification

import java.time.Instant

@MicronautTest(environments = ["mock_auth"])
@IgnoreIf({System.getenv("requireIntegrationTests") != 'true'})
class AnonymousOrderControllerSpec extends Specification{
    @Inject
    MenuClient menuClient

    @Inject
    MealClient mealClient

    @Inject
    AnonymousOrderClient anonymousOrderClient

    @Inject
    ObjectMapper objectMapper

    def "test adding anonymous order"() {
        given:
        Instant dateOfMeal = Instant.ofEpochSecond(1711487392)
        String location = "London"
        String name = "MacD"
        String anonymousUserId = "3b0833d9-7e08-4ce4-8bd4-fba1291bef95"
        String phoneNumber = "+44 20 7123 4567"

        Set<MenuItem> menuItems = [new MenuItem(name: "name", description: "description", price: 1.01)]
        CreateMenuRequest createMenuRequest = new CreateMenuRequest(menuItems, location, name, "description", phoneNumber)
        CreateMealRequest createMealRequest = new CreateMealRequest(name: name, dateOfMeal: dateOfMeal, location: location, menuName: name, mealConfig: new MealConfig())

        when:
        menuClient.addMenu(createMenuRequest)
        Meal meal = mealClient.addMeal(createMealRequest)

        CreateOrderRequest createOrderRequest = new CreateOrderRequest(dateOfMeal, meal.getId(), menuItems, "steven")
        Order anonymousOrder = anonymousOrderClient.addAnonymousOrder(createOrderRequest, anonymousUserId)

        then:
        assert anonymousOrder.getOrderParticipant().getUserId() == anonymousUserId
        assert anonymousOrder.getSortKey() =="2024-03-26T21:09:52Z"
        assert anonymousOrder.getOrderParticipant().getName() == "AnonymousUser"
        assert anonymousOrder.getMenuItems() == menuItems
        assert anonymousOrder.getGSIPrimaryKey() == "Order_${anonymousUserId}_ANONYMOUS"
        assert anonymousOrder.getPrimaryKey() == "Order_${meal.getId()}"
        assert anonymousOrder.getMeal() == meal
    }

    def "test getting anonymous order"() {
        given:
        Instant dateOfMeal = Instant.ofEpochSecond(1711487392)
        String location = "London"
        String name = "MacD"
        String anonymousUserId = "3b0833d9-7e08-4ce4-8bd4-fba1291bef95"
        String phoneNumber = "+44 20 7123 4567"

        Set<MenuItem> menuItems = [new MenuItem(name: "name", description: "description", price: 1.01)]
        CreateMenuRequest createMenuRequest = new CreateMenuRequest(menuItems, location, name, "description", phoneNumber)
        CreateMealRequest createMealRequest = new CreateMealRequest(name: name, dateOfMeal: dateOfMeal, location: location, menuName: name, mealConfig: new MealConfig())

        when:
        menuClient.addMenu(createMenuRequest)
        Meal meal = mealClient.addMeal(createMealRequest)

        CreateOrderRequest createOrderRequest = new CreateOrderRequest(dateOfMeal, meal.getId(), menuItems, "steven")
        Order anonymousOrderCreated = anonymousOrderClient.addAnonymousOrder(createOrderRequest, anonymousUserId)
        Optional<Order> anonymousOrder = anonymousOrderClient.getAnonymousOrder(anonymousOrderCreated.getOrderParticipant().getUserId(), anonymousOrderCreated.getMeal().getId())

        then:
        assert anonymousOrder.isPresent()
        assert anonymousOrder.get().getOrderParticipant().getUserId() == anonymousUserId
        assert anonymousOrder.get().getSortKey() =="2024-03-26T21:09:52Z"
        assert anonymousOrder.get().getOrderParticipant().getName() == "AnonymousUser"
        assert anonymousOrder.get().getMenuItems() == menuItems
        assert anonymousOrder.get().getGSIPrimaryKey() == "Order_${anonymousUserId}_ANONYMOUS"
        assert anonymousOrder.get().getPrimaryKey() == "Order_${meal.getId()}"
        assert anonymousOrder.get().getMeal() == meal
    }
}
