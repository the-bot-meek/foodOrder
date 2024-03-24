package com.example.services;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.*;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.example.dto.CreateMealRequest;
import com.example.models.Meal;
import io.micronaut.context.annotation.Requires;
import io.micronaut.context.annotation.Value;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.*;

@Singleton
@Requires(property = "micronaut.dynamodb.primary_table.region")
@Requires(property = "micronaut.dynamodb.primary_table.endpoint")
public class MealService {
    public final AmazonDynamoDB dynamoDbClient;
    private final DynamoDBMapper dynamoDBMapper;
    private final Logger log = LoggerFactory.getLogger(MealService.class);

    public MealService(
            @Value("${micronaut.dynamodb.primary_table.region}") String endpoint,
            @Value("${micronaut.dynamodb.primary_table.endpoint}") String region
    ) {
        this.dynamoDbClient = AmazonDynamoDBClientBuilder.standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(endpoint, region))
                .build();
        this.dynamoDBMapper = new DynamoDBMapper(dynamoDbClient);
    }


    public Meal newMeal(CreateMealRequest createMealRequest, String uid) {
        Meal meal = convertCreateMealRequestToNewMeal(createMealRequest, uid);
        dynamoDBMapper.save(meal);
        return meal;
    }

    // ToDo: add pagination based on the meal date in the sort key
    public List<Meal> getListOfMeals(String uid) {
        final String pk = "Meal_" + uid;
        log.trace("Getting all meals for uid:{}", pk);
        Map<String, AttributeValue> eav = new HashMap<>();
        eav.put(":PK", new AttributeValue().withS(pk));
        DynamoDBQueryExpression<Meal> dynamoDBQueryExpression = new DynamoDBQueryExpression<Meal>()
                .withKeyConditionExpression("pk = :PK")
                .withExpressionAttributeValues(eav);
        return dynamoDBMapper.query(Meal.class, dynamoDBQueryExpression);
    }

    public Optional<Meal> getMeal(String pk, String sk) {
        log.trace("Getting Meal PK: {}, SK: {}", pk, sk);
        return Optional.ofNullable(
                dynamoDBMapper.load(Meal.class, pk, sk)
        );
    }

    private Meal convertCreateMealRequestToNewMeal(CreateMealRequest createMealRequest, String uid) {
        final String id = UUID.randomUUID().toString();
        return convertCreateMealRequestToNewMeal(createMealRequest, uid, id);
    }

    private Meal convertCreateMealRequestToNewMeal(CreateMealRequest createMealRequest, String uid, String id) {
        return new Meal(
                id,
                uid,
                createMealRequest.name(),
                createMealRequest.venueMenuId(),
                Instant.ofEpochSecond(createMealRequest.mealDate())
        );
    }


}
