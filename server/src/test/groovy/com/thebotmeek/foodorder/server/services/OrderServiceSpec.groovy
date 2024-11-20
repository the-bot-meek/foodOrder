package com.thebotmeek.foodorder.server.services

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression
import com.foodorder.server.models.AnonymousOrder
import com.foodorder.server.models.Order
import com.foodorder.server.models.meal.Meal
import com.foodorder.server.services.DynamoDBFacadeService
import com.foodorder.server.services.OrderService
import spock.lang.Specification


class OrderServiceSpec extends Specification {
    DynamoDBFacadeService dynamoDBFacadeService
    OrderService orderService

    def "setup"() {
        dynamoDBFacadeService = Mock(DynamoDBFacadeService)
        orderService = new OrderService(dynamoDBFacadeService)
    }

    def "test add addOrdersForPrivateMeal happy path"() {
        given:
        Meal meal = new Meal(location: "location", venueName: "venueName")
        Set<String> recipientIds = ["18e23aeb-cc62-4f87-8820-71d77612a42a"]


        when:
        orderService.addOrdersForPrivateMeal(meal, recipientIds)

        then:
        1 * dynamoDBFacadeService.batchSave(*_)
    }

    def "getAnonymousOrder returns value"() {
        given:
        String uid = "82cac417-9fde-4820-b607-8b5f56f68f90"
        String mealId = "1945c5b6-7aad-4b3e-9a6a-997101e51179"
        List<Order> anonymousOrderList = [new AnonymousOrder()]
        when:
        Optional<AnonymousOrder> anonymousOrder = orderService.getAnonymousOrder(uid, mealId)

        then:
        1 * dynamoDBFacadeService.query(AnonymousOrder.class, _ as DynamoDBQueryExpression) >> anonymousOrderList
        assert anonymousOrder.isPresent()
        assert anonymousOrder.get() == anonymousOrderList[0]
    }

    def "getAnonymousOrder does not return value"() {
        given:
        String uid = "82cac417-9fde-4820-b607-8b5f56f68f90"
        String mealId = "1945c5b6-7aad-4b3e-9a6a-997101e51179"
        List<Order> anonymousOrderList = []
        when:
        Optional<AnonymousOrder> anonymousOrder = orderService.getAnonymousOrder(uid, mealId)

        then:
        1 * dynamoDBFacadeService.query(AnonymousOrder.class, _ as DynamoDBQueryExpression) >> anonymousOrderList
        assert anonymousOrder.isEmpty()
    }

    def "list all orders for user id"() {
        given:
        String userId = "063003d8-f419-4cf7-8fda-dd82e69821ec"
        List<Order> orderList = [new Order()]

        when:
        List<Order> orders = orderService.listOrdersFromUserID(userId)

        then:
        1 * dynamoDBFacadeService.query(Order, _ as DynamoDBQueryExpression) >> orderList
        assert orders == orderList
    }

    def "deleteAllOrdersForMeal"() {
        given:
        String mealId = "117f72a2-1e97-4fe4-adcc-fa3d28c561c6"
        List<Order> orders = [new Order()]
        when:
        orderService.deleteAllOrdersForMeal(mealId)

        then:
        1 * dynamoDBFacadeService.query(Order, _ as DynamoDBQueryExpression) >> orders
        1 * dynamoDBFacadeService.batchDelete(orders)
    }
}
