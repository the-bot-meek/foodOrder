package com.thebotmeek.api.integration

import com.thebotmeek.api.client.MealClient
import com.thebotmeek.api.client.MenuClient
import com.thebotmeek.api.client.OrderClient
import com.thebotmeek.api.client.UserClient
import com.foodorder.server.models.MenuItem
import com.foodorder.server.models.MenuItemCategory
import com.foodorder.server.models.Order
import com.foodorder.server.models.meal.Meal
import com.foodorder.server.models.meal.MealConfig
import com.thebotmeek.api.request.CreateMealRequest
import com.thebotmeek.api.request.CreateMenuRequest
import com.thebotmeek.api.request.CreateOrderRequest
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
