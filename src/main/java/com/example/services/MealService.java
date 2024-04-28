package com.example.services;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.example.Exceptions.MealRequestConverterException;
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
    private final LocationService locationService;

    public MealService(IDynamoDBFacadeService dynamoDBFacadeService, LocationService locationService) {
        this.dynamoDBFacadeService = dynamoDBFacadeService;
        this.locationService = locationService;
    }

    public Meal newMeal(CreateMealRequest createMealRequest, String uid) throws MealRequestConverterException {
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

    public Optional<Meal> getMeal(String originatorUid, Instant mealDate, String mealId) {
        return dynamoDBFacadeService.load(Meal.class, originatorUid, mealDate + "_" + mealId);
    }

    public Optional<Meal> getMeal(String pk, String sk) {
        log.trace("Getting Meal PK: {}, SK: {}", pk, sk);
        return dynamoDBFacadeService.load(Meal.class, pk, sk);
    }

    private Meal convertCreateMealRequestToNewMeal(CreateMealRequest createMealRequest, String uid) throws MealRequestConverterException {
        final String id = UUID.randomUUID().toString();
        return convertCreateMealRequestToNewMeal(createMealRequest, uid, id);
    }

    private Meal convertCreateMealRequestToNewMeal(CreateMealRequest createMealRequest, String uid, String id) throws MealRequestConverterException {
        log.trace("Converting CreateMealRequest into Meal");
        if (!locationService.listLocation().contains(createMealRequest.location())) {
            throw new MealRequestConverterException("Invalid Location");
        }
        return new Meal(
                id,
                uid,
                createMealRequest.name(),
                Instant.ofEpochSecond(createMealRequest.mealDate()),
                createMealRequest.location(),
                createMealRequest.venueName()
        );
    }
}
