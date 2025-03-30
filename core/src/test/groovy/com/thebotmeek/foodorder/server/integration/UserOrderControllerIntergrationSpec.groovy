package com.thebotmeek.foodorder.server.integration

import com.foodorder.server.client.MealClient
import com.foodorder.server.client.OrderClient
import com.foodorder.server.client.UserClient
import com.foodorder.server.client.MenuClient
import com.foodorder.server.models.MenuItemCategory
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
class UserOrderControllerIntergrationSpec extends Specification {
    @Inject
    MealClient mealClient

    @Inject
    UserClient userClient

    @Inject
    OrderClient orderClient

    @Inject
    MenuClient menuClient

    def "Get all meals for User"() {
        given:
        Instant dateOfMeal = Instant.ofEpochSecond(1711487392)
        String location = "London"
        String name = "MacD"

        Set<MenuItem> menuItems = [new MenuItem(name: "name", description: "description", price: 1.01, menuItemCategory: MenuItemCategory.MAIN)]
        CreateMenuRequest createMenuRequest = new CreateMenuRequest(menuItems, location, name, "description", "+44 20 7123 4567")
        CreateMealRequest createMealRequest = new CreateMealRequest(name: name, dateOfMeal: dateOfMeal, location: location, menuName: name, mealConfig: new MealConfig())


        when:
        menuClient.addMenu(createMenuRequest)
        Meal meal = mealClient.addMeal(createMealRequest)

        CreateOrderRequest createOrderRequest = new CreateOrderRequest(dateOfMeal, meal.getId(), menuItems, "steven")
        Order order = orderClient.addOrder(createOrderRequest)
        List<Order> orderList = userClient.listOrders()

        then:
        assert orderList.find{Order it -> it.getId() == order.getId()} == order
    }
}
