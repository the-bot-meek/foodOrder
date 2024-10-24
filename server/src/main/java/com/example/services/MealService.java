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

    public List<DraftMeal> getListOfDraftMeals(String uid) {
        final String pk = "DraftMeal_" + uid;
        log.trace("Getting all meals for uid:{}", pk);
        Map<String, AttributeValue> eav = new HashMap<>();
        eav.put(":PK", new AttributeValue().withS(pk));
        DynamoDBQueryExpression<DraftMeal> dynamoDBQueryExpression = new DynamoDBQueryExpression<DraftMeal>()
                .withKeyConditionExpression("pk = :PK")
                .withExpressionAttributeValues(eav);
        return dynamoDBFacadeService.query(DraftMeal.class, dynamoDBQueryExpression);
    }

    public Optional<Meal> getMealByVenueNameAndMealId(String venueName, String mealId) {
        Map<String, AttributeValue> eav = new HashMap<>();
        eav.put(":venueName", new AttributeValue().withS(venueName));
        eav.put(":mealId", new AttributeValue().withS(mealId));
        DynamoDBQueryExpression<Meal> dynamoDBQueryExpression = new DynamoDBQueryExpression<Meal>()
                .withIndexName("gsi")
                .withConsistentRead(false)
                .withKeyConditionExpression("gsi_pk = :venueName and gsi_sk = :mealId")
                .withExpressionAttributeValues(eav);
        List<Meal> meals = dynamoDBFacadeService.query(Meal.class, dynamoDBQueryExpression);
        if (meals.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(meals.get(0));
    }

    public Optional<Meal> getMeal(String originatorUid, Instant mealDate, String mealId) {
        log.trace("Getting Meal originatorUid: {}, mealDate: {}, mealId: {}", originatorUid, mealDate, mealId);
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
        log.trace("Deleting Meal uid: {}, mealDate: {}, id: {}", uid, mealDate, id);
        dynamoDBFacadeService.delete(new Meal(uid, mealDate, id));
    }

    public void deleteDraftMeal(String uid, Instant mealDate, String id) {
        log.trace("Deleting DraftMeal uid: {}, mealDate: {}, id: {}", uid, mealDate, id);
        dynamoDBFacadeService.delete(new DraftMeal(uid, mealDate, id));
    }
}
