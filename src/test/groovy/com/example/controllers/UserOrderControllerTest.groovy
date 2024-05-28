package com.example.controllers

import com.example.models.Meal.Meal
import com.example.models.Order
import com.example.services.IDynamoDBFacadeService
import com.example.services.OrderService
import io.micronaut.security.authentication.Authentication
import spock.lang.Specification
import java.time.Instant

class UserOrderControllerTest extends Specification {
    def "ListOrders"() {
        given:
        IDynamoDBFacadeService dynamoDBFacadeService = Mock(IDynamoDBFacadeService)
        OrderService orderService = new OrderService(dynamoDBFacadeService)
        UserOrderController userOrderController = new UserOrderController(orderService)
        Authentication authentication = Mock(Authentication)
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
