package com.example.services;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.example.models.ModelCacheKeyGenerator;
import com.example.models.meal.DraftMeal;
import com.example.models.meal.Meal;
import io.micronaut.cache.annotation.CacheInvalidate;
import io.micronaut.cache.annotation.CachePut;
import io.micronaut.cache.annotation.Cacheable;
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

    @CachePut(value = "singleMealCache", keyGenerator = ModelCacheKeyGenerator.class)
    public Meal saveMeal(Meal meal) {
        return dynamoDBFacadeService.save(meal);
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

    public Optional<Meal> getMeal(String originatorUid, Instant mealDate, String mealId) {
        log.trace("Getting Meal originatorUid: {}, mealDate: {}, mealId: {}", originatorUid, mealDate, mealId);
        return getMeal(originatorUid, mealDate + "_" + mealId);
    }

    @Cacheable("singleMealCache")
    public Optional<DraftMeal> getDraftMeal(String pk, String sk) {
        log.trace("Getting Meal PK: {}, SK: {}", pk, sk);
        return dynamoDBFacadeService.load(DraftMeal.class, pk, sk);
    }

    @Cacheable("singleMealCache")
    public Optional<Meal> getMeal(String pk, String sk) {
        log.trace("Getting Meal PK: {}, SK: {}", pk, sk);
        return dynamoDBFacadeService.load(Meal.class, pk, sk);
    }

    public void deleteMeal(String uid, Instant mealDate, String id) {
        log.trace("Deleting Meal uid: {}, mealDate: {}, id: {}", uid, mealDate, id);
        deleteMeal(new Meal(uid, mealDate, id));
    }

    public void deleteDraftMeal(String uid, Instant mealDate, String id) {
        log.trace("Deleting DraftMeal uid: {}, mealDate: {}, id: {}", uid, mealDate, id);
        deleteMeal(new DraftMeal(uid, mealDate, id));
    }

    @CacheInvalidate(value = "singleMealCache", keyGenerator = ModelCacheKeyGenerator.class)
    public void deleteMeal(Meal meal) {
        dynamoDBFacadeService.delete(meal);
    }
}
