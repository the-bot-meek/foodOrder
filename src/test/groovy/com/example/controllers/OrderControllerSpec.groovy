package com.example.controllers

import com.example.dto.request.CreateOrderRequest
import com.example.models.Order
import com.example.services.IDynamoDBFacadeService
import com.example.services.OrderService
import spock.lang.Specification

import java.security.Principal
import java.time.Instant

class OrderControllerSpec extends Specification {
    def "AddOrder"() {
        given:
        IDynamoDBFacadeService dynamoDBFacadeService = Mock(IDynamoDBFacadeService)
        OrderService orderService = new OrderService(dynamoDBFacadeService)
        OrderController orderController = new OrderController(orderService)

        String mealId = "797b001f-de8f-47ed-833a-d84e61c73fe7"
        Instant dateOfMeal = Instant.ofEpochSecond(1711487392)
        String uid = "d84e61c73fe7-de8f-47ed-833a-797b001f"
        CreateOrderRequest createOrderRequest = new CreateOrderRequest(dateOfMeal, mealId)
        Principal principal = Mock(Principal)
        principal.getName() >> uid


        when:
        orderController.addOrder(createOrderRequest, principal)

        then:
        1 * dynamoDBFacadeService.save(new Order(mealId: mealId, dateOfMeal: dateOfMeal, uid: uid))
    }
}
