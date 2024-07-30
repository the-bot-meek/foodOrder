package com.example.services;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.example.models.AnonymousOrder;
import com.example.models.Order;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.*;

@Singleton
public class OrderService {
    private final Logger log = LoggerFactory.getLogger(MealService.class);
    private final IDynamoDBFacadeService dynamoDBFacadeService;

    public OrderService(
            IDynamoDBFacadeService dynamoDBFacadeService
    ) {
        this.dynamoDBFacadeService = dynamoDBFacadeService;
    }

    public void addOrder(Order order) {
        dynamoDBFacadeService.save(order);
    }

    public List<Order> getOrderFromMealId(String mealId) {
        final String pk = "Order_" + mealId;
        log.trace("Getting all orders for meal:{}", mealId);
        Map<String, AttributeValue> eav = new HashMap<>();
        eav.put(":PK", new AttributeValue().withS(pk));
        DynamoDBQueryExpression<Order> dynamoDBQueryExpression = new DynamoDBQueryExpression<Order>()
                .withKeyConditionExpression("meal_id = :PK")
                .withExpressionAttributeValues(eav);
        return dynamoDBFacadeService.query(Order.class, dynamoDBQueryExpression);
    }

    public void deleteAllOrdersForMeal(String mealId) {
        log.trace("Deleting all Orders for mealId: {}", mealId);
        List<Order> orders = getOrderFromMealId(mealId);
        dynamoDBFacadeService.batchDelete(orders);
    }

    public List<Order> listOrdersFromUserID(String uid) {
        final String pk = "Order_" + uid;
        log.trace("Getting all orders for meal:{}", uid);
        Map<String, AttributeValue> eav = new HashMap<>();
        eav.put(":PK", new AttributeValue().withS(pk));
        DynamoDBQueryExpression<Order> dynamoDBQueryExpression = new DynamoDBQueryExpression<Order>()
                .withIndexName("uid_gsi")
                .withConsistentRead(false)
                .withKeyConditionExpression("uid = :PK")
                .withExpressionAttributeValues(eav);
        return dynamoDBFacadeService.query(Order.class, dynamoDBQueryExpression);
    }

    public Optional<AnonymousOrder> getAnonymousOrder(String uid, String mealId) {
        final String pk = "AnonymousOrder_" + uid;
        log.trace("Getting all AnonymousOrders for meal:{}", uid);
        Map<String, AttributeValue> eav = new HashMap<>();
        eav.put(":uid", new AttributeValue().withS(pk));
        eav.put(":meal_id", new AttributeValue().withS("Order_" + mealId));
        DynamoDBQueryExpression<AnonymousOrder> dynamoDBQueryExpression = new DynamoDBQueryExpression<AnonymousOrder>()
                .withIndexName("uid_gsi")
                .withConsistentRead(false)
                .withKeyConditionExpression("uid = :uid and meal_id = :meal_id")
                .withExpressionAttributeValues(eav);

        List<AnonymousOrder> orders = dynamoDBFacadeService.query(AnonymousOrder.class, dynamoDBQueryExpression);
        if (orders.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(orders.get(0));
    }
}
