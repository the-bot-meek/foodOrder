package com.foodorder.server.repository;

import com.foodorder.server.models.meal.DraftMeal;
import com.foodorder.server.models.meal.Meal;
import jakarta.inject.Named;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;

import java.time.Instant;
import java.util.*;

@Singleton
public class MealRepository {
    private final Logger log = LoggerFactory.getLogger(MealRepository.class);
    private final IDynamoDBFacadeRepository dynamoDBFacadeRepository;
    public MealRepository(@Named("primary-table") IDynamoDBFacadeRepository dynamoDBFacadeRepository) {
        this.dynamoDBFacadeRepository = dynamoDBFacadeRepository;
    }

    public void saveMeal(Meal meal) {
        dynamoDBFacadeRepository.save(meal);
    }

    // ToDo: add pagination based on the meal date in the sort key
    public List<Meal> getListOfMeals(String uid) {
        final String pk = "Meal_" + uid;
        log.trace("Getting all meals for uid:{}", pk);
        Key key = Key.builder().partitionValue(pk).build();
        QueryConditional queryConditional = QueryConditional.keyEqualTo(key);
        return dynamoDBFacadeRepository.query(Meal.class, queryConditional);
    }

    public List<DraftMeal> getListOfDraftMeals(String uid) {
        final String pk = "DraftMeal_" + uid;
        log.trace("Getting all meals for uid:{}", pk);
        Key key = Key.builder().partitionValue(pk).build();
        QueryConditional queryConditional = QueryConditional.keyEqualTo(key);
        return dynamoDBFacadeRepository.query(DraftMeal.class, queryConditional);
    }

    public Optional<Meal> getMealByMenuNameAndMealId(String menuName, String mealId) {
        Key key = Key.builder().partitionValue(menuName).sortValue(mealId).build();
        QueryConditional queryConditional = QueryConditional.keyEqualTo(key);
        List<Meal> meals = dynamoDBFacadeRepository.queryWithIndex(Meal.class, queryConditional, "gsi");
        if (meals.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(meals.get(0));
    }

    public Optional<Meal> getMeal(String originatorUid, Instant mealDate, String mealId) {
        log.trace("Getting Meal originatorUid: {}, mealDate: {}, mealId: {}", originatorUid, mealDate, mealId);
        return this.getMeal(originatorUid, mealDate + "_" + mealId);
    }

    public Optional<DraftMeal> getDraftMeal(String pk, String sk) {
        log.trace("Getting Meal PK: {}, SK: {}", pk, sk);
        return dynamoDBFacadeRepository.load(DraftMeal.class, "DraftMeal_" + pk, sk);
    }

    public Optional<Meal> getMeal(String uid, String sk) {
        log.trace("Getting Meal UID: {}, SK: {}", uid, sk);
        return dynamoDBFacadeRepository.load(Meal.class, "Meal_" + uid, sk);
    }

    public void deleteMeal(String uid, Instant mealDate, String id) {
        log.trace("Deleting Meal uid: {}, mealDate: {}, id: {}", uid, mealDate, id);
        dynamoDBFacadeRepository.delete(new Meal(uid, mealDate, id));
    }

    public void deleteDraftMeal(String uid, Instant mealDate, String id) {
        log.trace("Deleting DraftMeal uid: {}, mealDate: {}, id: {}", uid, mealDate, id);
        dynamoDBFacadeRepository.delete(new DraftMeal(uid, mealDate, id));
    }
}
