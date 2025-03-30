package com.thebotmeek.foodorder.server.services


import com.foodorder.server.models.Order
import com.foodorder.server.models.meal.Meal
import com.foodorder.server.models.meal.MealConfig
import com.foodorder.server.models.meal.PrivateMealConfig
import com.foodorder.server.repository.IDynamoDBFacadeRepository
import com.foodorder.server.repository.OrderRepository
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional
import spock.lang.Specification


class OrderRepositorySpec extends Specification {
    IDynamoDBFacadeRepository dynamoDBFacadeService
    OrderRepository orderService

    def "setup"() {
        dynamoDBFacadeService = Mock(IDynamoDBFacadeRepository)
        orderService = new OrderRepository(dynamoDBFacadeService)
    }

    def "test add addOrdersForPrivateMeal happy path"() {
        given:
        Meal meal = new Meal(location: "location", menuName: "menuName", mealConfig: new MealConfig(privateMealConfig: new PrivateMealConfig()))
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
        List<Order> anonymousOrderList = [new Order()]
        when:
        Optional<Order> anonymousOrder = orderService.getAnonymousOrder(uid, mealId)

        then:
        1 * dynamoDBFacadeService.queryWithIndex(Order.class, _ as QueryConditional, "uid_gsi") >> anonymousOrderList
        assert anonymousOrder.isPresent()
        assert anonymousOrder.get() == anonymousOrderList[0]
    }

    def "getAnonymousOrder does not return value"() {
        given:
        String uid = "82cac417-9fde-4820-b607-8b5f56f68f90"
        String mealId = "1945c5b6-7aad-4b3e-9a6a-997101e51179"
        List<Order> anonymousOrderList = []
        when:
        Optional<Order> anonymousOrder = orderService.getAnonymousOrder(uid, mealId)

        then:
        1 * dynamoDBFacadeService.queryWithIndex(Order.class, _ as QueryConditional, "uid_gsi")>> anonymousOrderList
        assert anonymousOrder.isEmpty()
    }

    def "list all orders for user id"() {
        given:
        String userId = "063003d8-f419-4cf7-8fda-dd82e69821ec"
        List<Order> orderList = [new Order()]

        when:
        List<Order> orders = orderService.listOrdersFromUserID(userId)

        then:
        1 * dynamoDBFacadeService.queryWithIndex(Order.class, _ as QueryConditional, "uid_gsi") >> orderList
        assert orders == orderList
    }

    def "deleteAllOrdersForMeal"() {
        given:
        String mealId = "117f72a2-1e97-4fe4-adcc-fa3d28c561c6"
        List<Order> orders = [new Order()]
        when:
        orderService.deleteAllOrdersForMeal(mealId)

        then:
        1 * dynamoDBFacadeService.query(Order, _ as QueryConditional) >> orders
        1 * dynamoDBFacadeService.batchDelete(orders)
    }
}
