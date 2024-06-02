package com.example.services;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.example.models.meal.DraftMeal;
import com.example.models.meal.Meal;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.*;

@Singleton
public class MealService {
    private final Logger log = LoggerFactory.getLogger(MealService.class);
    private final IDynamoDBFacadeService dynamoDBFacadeService;
    public MealService(IDynamoDBFacadeService dynamoDBFacadeService) {
        this.dynamoDBFacadeService = dynamoDBFacadeService;
    }

    public void saveMeal(Meal meal) {
        dynamoDBFacadeService.save(meal);
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
        return dynamoDBFacadeService.query(Meal.class, dynamoDBQueryExpression);
    }

    public Optional<Meal> getMeal(String originatorUid, Instant mealDate, String mealId) {
        return dynamoDBFacadeService.load(Meal.class, originatorUid, mealDate + "_" + mealId);
    }

    public Optional<DraftMeal> getDraftMeal(String pk, String sk) {
        log.trace("Getting Meal PK: {}, SK: {}", pk, sk);
        return dynamoDBFacadeService.load(DraftMeal.class, pk, sk);
    }

    public Optional<Meal> getMeal(String pk, String sk) {
        log.trace("Getting Meal PK: {}, SK: {}", pk, sk);
        return dynamoDBFacadeService.load(Meal.class, pk, sk);
    }

    public void deleteMeal(String uid, Instant mealDate, String id) {
        dynamoDBFacadeService.delete(new Meal(uid, mealDate, id));
    }

    public void deleteDraftMeal(String uid, Instant mealDate, String id) {
        dynamoDBFacadeService.delete(new DraftMeal(uid, mealDate, id));
    }
}
