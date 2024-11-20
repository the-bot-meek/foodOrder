package com.thebotmeek.foodorder.server.controllers

import com.foodorder.server.controllers.MealOrderController
import com.foodorder.server.models.meal.Meal
import com.foodorder.server.models.Order
import com.foodorder.server.services.OrderService
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.security.authentication.Authentication
import spock.lang.Specification
import java.time.Instant

class MealOrderControllerSpec extends Specification {
    OrderService orderService

    def "setup"() {
        orderService = Mock(OrderService)
    }

    private Authentication mockAuthentication(String name) {
        Authentication authentication = Mock(Authentication)
        authentication.getName() >> name
        return authentication
    }
    def "ListAllOrdersForMeal"() {
        given:
        String mealId = "797b001f-de8f-47ed-833a-d84e61c73fe7"
        Meal meal = new Meal(id: mealId, mealDate: Instant.ofEpochSecond(1711487392), uid: "d84e61c73fe7-de8f-47ed-833a-797b001f")
        List<Order> orders = [new Order(
                meal: meal,
                uid: "d84e61c73fe7-de8f-47ed-833a-797b001f"
        )]
        Authentication authentication = mockAuthentication( "d84e61c73fe7-de8f-47ed-833a-797b001f")
        MealOrderController mealOrderController = new MealOrderController(orderService)

        when:
        List<Order> orderList = mealOrderController.listAllOrdersForMeal(mealId, authentication).getBody().get()

        then:
        1 * orderService.getOrderFromMealId(mealId) >> orders
        assert orderList == orders
    }

    def "Reject request where participant is not the owner of the meal"() {
        given:
        String mealId = "797b001f-de8f-47ed-833a-d84e61c73fe7"
        Meal meal = new Meal(id: mealId, mealDate: Instant.ofEpochSecond(1711487392))
        List<Order> orders = [new Order(
                meal: meal,
                uid: "d84e61c73fe7-de8f-47ed-833a-797b001f",
                participantsName: "principal_name"
        )]
        Authentication authentication = mockAuthentication( "invalid_principal_name")
        MealOrderController mealOrderController = new MealOrderController(orderService)

        when:
        HttpResponse<List<Order>> httpResponse = mealOrderController.listAllOrdersForMeal(mealId, authentication)

        then:
        1 * orderService.getOrderFromMealId(mealId) >> orders
        assert httpResponse.getStatus() == HttpStatus.INTERNAL_SERVER_ERROR
    }

    def "Reject request where the not all orders belong to the same meal meal"() {
        given:
        String mealId = "797b001f-de8f-47ed-833a-d84e61c73fe7"
        Meal meal = new Meal(id: mealId, mealDate: Instant.ofEpochSecond(1711487392))
        List<Order> orders = [new Order(
                meal: meal,
                uid: "d84e61c73fe7-de8f-47ed-833a-797b001f",
                participantsName: "principal_name"
        ), new Order()]
        Authentication authentication = mockAuthentication( "invalid_principal_name")
        MealOrderController mealOrderController = new MealOrderController(orderService)

        when:
        HttpResponse<List<Order>> httpResponse = mealOrderController.listAllOrdersForMeal(mealId, authentication)

        then:
        1 * orderService.getOrderFromMealId(mealId) >> orders
        assert httpResponse.getStatus() == HttpStatus.INTERNAL_SERVER_ERROR
    }
}
