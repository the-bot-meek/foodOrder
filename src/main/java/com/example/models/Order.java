package com.example.models;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import io.micronaut.serde.annotation.Serdeable;

import java.time.Instant;

@DynamoDBTable(tableName = "order_table")
@Serdeable
public class Order {
    private String mealId;
    private Instant dateOfMeal;
    private String uid;

    public Order(String mealId, Instant dateOfMeal, String uid) {
        this.mealId = mealId;
        this.dateOfMeal = dateOfMeal;
        this.uid = uid;
    }

    public Order() {

    }

    @DynamoDBHashKey(attributeName = "meal_id")
    public String getPrimaryKey() {
        return "Order_" + mealId;
    }

    public void setPrimaryKey(String pk) {
        this.mealId = pk.replace("Order_", "");
    }

    @DynamoDBRangeKey(attributeName = "date_of_meal")
    public String getSortKey() {
        return dateOfMeal.toString();
    }

    public void setSortKey(String sk) {
        this.dateOfMeal = Instant.parse(sk);
    }

    @DynamoDBIndexHashKey(globalSecondaryIndexName = "uid_gsi", attributeName = "uid")
    public String getGSIPrimaryKey() {
        return "Order_" + uid;
    }

    public void setGSIPrimaryKey(String primaryKeyGSI) {
        uid = primaryKeyGSI.replace("Order_", "");
    }

    public String getMealId() {
        return mealId;
    }

    public void setMealId(String mealId) {
        this.mealId = mealId;
    }

    @DynamoDBIgnore
    public Instant getDateOfMeal() {
        return dateOfMeal;
    }

    @DynamoDBIgnore
    public void setDateOfMeal(Instant dateOfMeal) {
        this.dateOfMeal = dateOfMeal;
    }

    @DynamoDBIgnore
    public String getUid() {
        return uid;
    }

    @DynamoDBIgnore
    public void setUid(String uid) {
        this.uid = uid;
    }
}
