package com.example.controllers

import com.example.models.Order
import com.example.services.IDynamoDBFacadeService
import com.example.services.OrderService
import spock.lang.Specification
import java.time.Instant

class MealOrderControllerSpec extends Specification {
    def "ListAllOrdersForMeal"() {
        given:
        String mealId = "797b001f-de8f-47ed-833a-d84e61c73fe7"
        IDynamoDBFacadeService dynamoDBFacadeService = Mock(IDynamoDBFacadeService)
        OrderService orderService = new OrderService(dynamoDBFacadeService, null, null)
        dynamoDBFacadeService.query(Order.class, _) >> [new Order(mealId: mealId, dateOfMeal: Instant.ofEpochSecond(1711487392), uid: "d84e61c73fe7-de8f-47ed-833a-797b001f")]
        MealOrderController mealOrderController = new MealOrderController(orderService)

        when:
        List<Order> orderList = mealOrderController.listAllOrdersForMeal(mealId)

        then:
        orderList == [new Order(mealId: mealId, dateOfMeal: Instant.ofEpochSecond(1711487392), uid: "d84e61c73fe7-de8f-47ed-833a-797b001f")]
    }
}
