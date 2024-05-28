package com.example.services;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.example.Converters.CreateOrderRequestConverter;
import com.example.Exceptions.OrderRequestConverterException;
import com.example.dto.request.CreateOrderRequest;
import com.example.models.Order;
import io.micronaut.security.authentication.Authentication;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

@Singleton
public class OrderService {
    private final Logger log = LoggerFactory.getLogger(MealService.class);
    private final IDynamoDBFacadeService dynamoDBFacadeService;
    private final CreateOrderRequestConverter createOrderRequestConverter;

    public OrderService(
            IDynamoDBFacadeService dynamoDBFacadeService,
            CreateOrderRequestConverter createOrderRequestConverter
    ) {
        this.dynamoDBFacadeService = dynamoDBFacadeService;
        this.createOrderRequestConverter = createOrderRequestConverter;
    }

    public Order addOrder(CreateOrderRequest createOrderRequest, String uid, String preferredUsername) throws OrderRequestConverterException {
        Order order = createOrderRequestConverter.convertCreateOrderRequestToOrder(createOrderRequest, uid, preferredUsername);
        dynamoDBFacadeService.save(order);
        return order;
    }

    public Order addOrder(CreateOrderRequest createOrderRequest, Authentication authentication) throws OrderRequestConverterException {
        return addOrder(createOrderRequest, authentication.getName(), (String) authentication.getAttributes().get("preferred_username"));
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
}
