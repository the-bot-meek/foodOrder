package com.thebotmeek.foodorder.server.integration

import com.foodorder.server.client.AnonymousOrderClient
import com.foodorder.server.client.MealClient
import com.foodorder.server.client.OrderClient
import com.foodorder.server.client.MenuClient
import com.foodorder.server.models.MenuItemCategory
import com.foodorder.server.request.CreateMealConfig
import com.foodorder.server.request.CreateMealRequest
import com.foodorder.server.request.CreateOrderRequest
import com.foodorder.server.request.CreateMenuRequest
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
    MenuClient menuClient

    @Inject
    MealClient mealClient

    @Inject
    AnonymousOrderClient anonymousOrderClient

    Instant dateOfMeal
    String location
    String name
    Set<MenuItem> menuItems
    CreateMenuRequest createMenuRequest
    CreateMealRequest createMealRequest

    def "setup"() {
        dateOfMeal = Instant.ofEpochSecond(1711487392)
        location = "London"
        name = "MacD"
        menuItems = [new MenuItem(name: "name", description: "description", price: 1.01, menuItemCategory: MenuItemCategory.MAIN)]
        createMenuRequest = new CreateMenuRequest(menuItems, location, name, "description", "+44 20 7123 4567")
        createMealRequest = new CreateMealRequest(name: name, dateOfMeal: dateOfMeal, location: location, menuName: name, createMealConfig: new CreateMealConfig())
    }

    def "Get all authenticated orders for meal"() {
        given:
        menuClient.addMenu(createMenuRequest)
        Meal meal = mealClient.addMeal(createMealRequest)

        CreateOrderRequest createOrderRequest = new CreateOrderRequest(dateOfMeal, meal.getId(), menuItems, "steven")
        Order order = orderClient.addOrder(createOrderRequest)

        when:
        List<Order> orderList = mealClient.listAllOrdersForMeal(meal.getId())

        then:
        assert orderList.find{Order it -> it.getId() == order.getId()} == order
    }

    def "Get all anonymous orders for meal"() {
        given:
        menuClient.addMenu(createMenuRequest)
        Meal meal = mealClient.addMeal(createMealRequest)

        CreateOrderRequest createOrderRequest = new CreateOrderRequest(dateOfMeal, meal.getId(), menuItems, "steven")
        Order order = anonymousOrderClient.addAnonymousOrder(createOrderRequest, "UUID")

        when:
        List<Order> orderList = mealClient.listAllOrdersForMeal(meal.getId())

        then:
        assert orderList.find{Order it -> it.getId() == order.getId()} == order
    }
}
