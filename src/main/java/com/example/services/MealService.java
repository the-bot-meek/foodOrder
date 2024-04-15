package com.example.services;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.example.dto.request.CreateMealRequest;
import com.example.models.Meal;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.*;

@Singleton
public class MealService {
    private final Logger log = LoggerFactory.getLogger(MealService.class);
    private final IDynamoDBFacadeService dynamoDBFacadeService;

    public MealService(
            IDynamoDBFacadeService dynamoDBFacadeService
    ) {
        this.dynamoDBFacadeService = dynamoDBFacadeService;
    }


    public Meal newMeal(CreateMealRequest createMealRequest, String uid) {
        Meal meal = convertCreateMealRequestToNewMeal(createMealRequest, uid);
        dynamoDBFacadeService.save(meal);
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
        return dynamoDBFacadeService.list(Meal.class, dynamoDBQueryExpression);
    }

    public Optional<Meal> getMeal(String pk, String sk) {
        log.trace("Getting Meal PK: {}, SK: {}", pk, sk);
        return dynamoDBFacadeService.load(Meal.class, pk, sk);
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
