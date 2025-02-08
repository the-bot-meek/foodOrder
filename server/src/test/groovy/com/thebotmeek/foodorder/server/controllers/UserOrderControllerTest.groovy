package com.thebotmeek.foodorder.server.controllers

import com.foodorder.models.models.Order
import com.foodorder.models.models.meal.Meal
import com.foodorder.server.controllers.UserOrderController
import com.foodorder.server.repository.IDynamoDBFacadeRepository
import com.foodorder.server.repository.OrderRepository
import io.micronaut.security.authentication.Authentication
import spock.lang.Specification

import java.time.Instant

class UserOrderControllerTest extends Specification {
    IDynamoDBFacadeRepository dynamoDBFacadeService
    OrderRepository orderService
    UserOrderController userOrderController
    Authentication authentication

    def "setup"() {
        dynamoDBFacadeService = Mock(IDynamoDBFacadeRepository)
        orderService = new OrderRepository(dynamoDBFacadeService)
        userOrderController = new UserOrderController(orderService)
        authentication = Mock(Authentication)
    }

    def "ListOrders"() {
        given:
        authentication.getName() >> "d84e61c73fe7-de8f-47ed-833a-797b001f"
        Meal meal = new Meal(id: "797b001f-de8f-47ed-833a-d84e61c73fe7", mealDate: Instant.ofEpochSecond(1711487392))


        when:
        List<Order> orderList = userOrderController.listOrders(authentication)

        then:
        1 * dynamoDBFacadeService.queryWithIndex(Order.class, _, "uid_gsi") >> {
            List.of(new Order(meal: meal, uid: "d84e61c73fe7-de8f-47ed-833a-797b001f"))
        }
        orderList.get(0) == new Order(meal: meal, uid: "d84e61c73fe7-de8f-47ed-833a-797b001f")
    }
}
