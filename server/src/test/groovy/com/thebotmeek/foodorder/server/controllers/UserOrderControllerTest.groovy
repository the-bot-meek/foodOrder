package com.thebotmeek.foodorder.server.controllers

import com.foodorder.server.controllers.UserOrderController
import com.foodorder.server.models.meal.Meal
import com.foodorder.server.models.Order
import com.foodorder.server.services.IDynamoDBFacadeService
import com.foodorder.server.services.OrderService
import io.micronaut.security.authentication.Authentication
import spock.lang.Specification
import java.time.Instant

class UserOrderControllerTest extends Specification {
    IDynamoDBFacadeService dynamoDBFacadeService
    OrderService orderService
    UserOrderController userOrderController
    Authentication authentication

    def "setup"() {
        dynamoDBFacadeService = Mock(IDynamoDBFacadeService)
        orderService = new OrderService(dynamoDBFacadeService)
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
        1 * dynamoDBFacadeService.query(Order.class, _) >> {
            List.of(new Order(meal: meal, uid: "d84e61c73fe7-de8f-47ed-833a-797b001f"))
        }
        orderList.get(0) == new Order(meal: meal, uid: "d84e61c73fe7-de8f-47ed-833a-797b001f")
    }
}
