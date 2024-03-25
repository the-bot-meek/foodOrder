package com.example.services;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.example.dto.CreateOrderRequest;
import com.example.models.Order;
import io.micronaut.context.annotation.Value;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Singleton
public class OrderService {
    public final AmazonDynamoDB dynamoDbClient;
    private final DynamoDBMapper dynamoDBMapper;
    private final Logger log = LoggerFactory.getLogger(MealService.class);

    public OrderService(
            @Value("${micronaut.dynamodb.primary_table.region}") String endpoint,
            @Value("${micronaut.dynamodb.primary_table.endpoint}") String region
    ) {
        this.dynamoDbClient = AmazonDynamoDBClientBuilder.standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(endpoint, region))
                .build();
        this.dynamoDBMapper = new DynamoDBMapper(dynamoDbClient);
    }

    public Order convertCreateOrderRequestToOrder(CreateOrderRequest createOrderRequest) {
        return new Order(
                createOrderRequest.mealId(),
                createOrderRequest.dateOfMeal(),
                createOrderRequest.uid()
        );
    }

    public Order addOrder(CreateOrderRequest createOrderRequest) {
        Order order = convertCreateOrderRequestToOrder(createOrderRequest);
        dynamoDBMapper.save(order);
        return order;
    }

    public List<Order> getOrderFromMealId(String mealId) {
        final String pk = "Order_" + mealId;
        log.trace("Getting all orders for meal:{}", mealId);
        Map<String, AttributeValue> eav = new HashMap<>();
        eav.put(":PK", new AttributeValue().withS(pk));
        DynamoDBQueryExpression<Order> dynamoDBQueryExpression = new DynamoDBQueryExpression<Order>()
                .withKeyConditionExpression("meal_id = :PK")
                .withExpressionAttributeValues(eav);
        return dynamoDBMapper.query(Order.class, dynamoDBQueryExpression);
    }
}
