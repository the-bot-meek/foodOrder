package com.example.models;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;

import java.time.Instant;

public class DraftMeal extends Meal {
    DraftMeal(){}
    public DraftMeal(String id, String organiserId, String name, Instant mealDate, String location, String venueName) {
        super(id, organiserId, name, mealDate, location, venueName);
    }
    @DynamoDBHashKey(attributeName = "pk")
    public String getPrimaryKey() {
        return "DraftMeal_" + this.getUid();
    }
    public void setPrimaryKey(String value) {
        this.setUid(value.replace("DraftMeal_", ""));
    }
}
