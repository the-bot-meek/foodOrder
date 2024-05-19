package com.example.models.Meal;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIgnore;

import java.time.Instant;

public class DraftMeal extends AbstractMeal {
    public DraftMeal(){
        super();
    }
    public DraftMeal(String id, String organiserId, String name, Instant mealDate, String location, String venueName) {
        super(id, organiserId, name, mealDate, location, venueName);
    }

    @Override
    @DynamoDBIgnore
    public String getPrimaryKeySuffix() {
        return "DraftMeal_";
    }
}
