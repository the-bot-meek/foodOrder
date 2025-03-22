package com.thebotmeek.foodorder.server.integration

import com.foodorder.server.client.MealClient
import com.foodorder.server.client.OrderClient
import com.foodorder.server.client.MenuClient
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


// 2024-03-26T21:09:52Z_e44bb7fc-7164-45e9-8c28-3c561baa7d90

@MicronautTest(environments = ["mock_auth"])
@IgnoreIf({System.getenv("requireIntegrationTests") != 'true'})
class OrderIntegrationSpec extends Specification {
    @Inject
    OrderClient orderClient

    @Inject
    MenuClient menuClient

    @Inject
    MealClient mealClient

    // 2024-03-26T21:09:52Z_0e48ce76-9714-47a5-ba02-9eb90cbe15c0
    def "Add order"() {
        given:
        Instant dateOfMeal = Instant.ofEpochSecond(1711487392)
        String location = "London"
        String name = "MacD"

        Set<MenuItem> menuItems = [new MenuItem()]
        CreateMenuRequest createMenuRequest = new CreateMenuRequest(menuItems, location, name, "description", "+44 20 7123 4567")
        CreateMealRequest createMealRequest = new CreateMealRequest(name: name, dateOfMeal: dateOfMeal, location: location, menuName: name, mealConfig: new MealConfig())


        when:
        menuClient.addMenu(createMenuRequest)
        Meal meal = mealClient.addMeal(createMealRequest)

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
