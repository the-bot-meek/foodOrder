package com.example.models.meal;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;

import java.time.Instant;

public class DraftMeal extends Meal {
    public DraftMeal(){}
    public DraftMeal(String id, String organiserId, String name, Instant mealDate, String location, String venueName, MealConfig mealConfig) {
        super(id, organiserId, name, mealDate, location, venueName, mealConfig);
    }

    public DraftMeal(String uid, Instant mealDate, String id) {
        super(uid, mealDate, id);
    }

    @DynamoDBHashKey(attributeName = "pk")
    public String getPrimaryKey() {
        return "DraftMeal_" + this.getUid();
    }
    public void setPrimaryKey(String value) {
        this.setUid(value.replace("DraftMeal_", ""));
    }
}
