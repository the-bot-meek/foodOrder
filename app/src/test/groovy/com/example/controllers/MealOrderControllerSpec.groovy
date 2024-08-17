package com.example.controllers

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression
import com.example.models.meal.Meal
import com.example.models.Order
import com.example.services.IDynamoDBFacadeService
import com.example.services.OrderService
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.security.authentication.Authentication
import spock.lang.Specification
import java.time.Instant

class MealOrderControllerSpec extends Specification {
    private Authentication mockAuthentication(String name) {
        Authentication authentication = Mock(Authentication)
        authentication.getName() >> name
        return authentication
    }
    def "ListAllOrdersForMeal"() {
        given:
        String mealId = "797b001f-de8f-47ed-833a-d84e61c73fe7"
        IDynamoDBFacadeService dynamoDBFacadeService = Mock(IDynamoDBFacadeService)
        OrderService orderService = new OrderService(dynamoDBFacadeService)
        Meal meal = new Meal(id: mealId, mealDate: Instant.ofEpochSecond(1711487392), uid: "d84e61c73fe7-de8f-47ed-833a-797b001f")
        List<Order> orders = [new Order(
                meal: meal,
                uid: "d84e61c73fe7-de8f-47ed-833a-797b001f"
        )]
        dynamoDBFacadeService.query(Order.class, _ as DynamoDBQueryExpression<Order>) >> orders
        Authentication authentication = mockAuthentication( "d84e61c73fe7-de8f-47ed-833a-797b001f")
        MealOrderController mealOrderController = new MealOrderController(orderService)

        when:
        List<Order> orderList = mealOrderController.listAllOrdersForMeal(mealId, authentication).getBody().get()

        then:
        orderList == orders
    }

    def "Reject request where participant is not the owner of the meal"() {
        given:
        String mealId = "797b001f-de8f-47ed-833a-d84e61c73fe7"
        IDynamoDBFacadeService dynamoDBFacadeService = Mock(IDynamoDBFacadeService)
        OrderService orderService = new OrderService(dynamoDBFacadeService)
        Meal meal = new Meal(id: mealId, mealDate: Instant.ofEpochSecond(1711487392))
        List<Order> orders = [new Order(
                meal: meal,
                uid: "d84e61c73fe7-de8f-47ed-833a-797b001f",
                participantsName: "principal_name"
        )]
        dynamoDBFacadeService.query(Order.class, _ as DynamoDBQueryExpression<Order>) >> orders
        Authentication authentication = mockAuthentication( "invalid_principal_name")
        MealOrderController mealOrderController = new MealOrderController(orderService)

        when:
        HttpResponse<List<Order>> httpResponse = mealOrderController.listAllOrdersForMeal(mealId, authentication)

        then:
        httpResponse.getStatus() == HttpStatus.INTERNAL_SERVER_ERROR
    }

    def "Reject request where the not all orders belong to the same meal meal"() {
        given:
        String mealId = "797b001f-de8f-47ed-833a-d84e61c73fe7"
        IDynamoDBFacadeService dynamoDBFacadeService = Mock(IDynamoDBFacadeService)
        OrderService orderService = new OrderService(dynamoDBFacadeService)
        Meal meal = new Meal(id: mealId, mealDate: Instant.ofEpochSecond(1711487392))
        List<Order> orders = [new Order(
                meal: meal,
                uid: "d84e61c73fe7-de8f-47ed-833a-797b001f",
                participantsName: "principal_name"
        ), new Order()]
        dynamoDBFacadeService.query(Order.class, _ as DynamoDBQueryExpression<Order>) >> orders
        Authentication authentication = mockAuthentication( "invalid_principal_name")
        MealOrderController mealOrderController = new MealOrderController(orderService)

        when:
        HttpResponse<List<Order>> httpResponse = mealOrderController.listAllOrdersForMeal(mealId, authentication)

        then:
        httpResponse.getStatus() == HttpStatus.INTERNAL_SERVER_ERROR
    }
}
