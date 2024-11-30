package com.foodorder.server.models.meal;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

import java.time.Instant;

@DynamoDbBean
public class DraftMeal extends Meal {
    public DraftMeal(){}
    public DraftMeal(String id, String organiserId, String name, Instant mealDate, String location, String venueName, MealConfig mealConfig) {
        super(id, organiserId, name, mealDate, location, venueName, mealConfig);
    }

    public DraftMeal(String uid, Instant mealDate, String id) {
        super(uid, mealDate, id);
    }

    @DynamoDbPartitionKey
    @DynamoDbAttribute("pk")
    public String getPrimaryKey() {
        return "DraftMeal_" + this.getUid();
    }
    public void setPrimaryKey(String value) {
        this.setUid(value.replace("DraftMeal_", ""));
    }
}
