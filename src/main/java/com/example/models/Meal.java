package com.example.models;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.Instant;

@DynamoDBTable(tableName = "primary_table")
public class Meal {
    private String id;
    private String uid;
    private String name;
    private String venueMenuId;
    private Instant mealDate;

    public Meal(String id, String organiserId, String name, String venueMenuId, Instant mealDate) {
        this.id = id;
        this.uid = organiserId;
        this.name = name;
        this.venueMenuId = venueMenuId;
        this.mealDate = mealDate;
    }

    public Meal() {

    }

    @DynamoDBHashKey(attributeName = "pk")
    public String getPrimaryKeyValue() {
        return "Meal_" + this.uid;
    }

    public void setPrimaryKeyValue(String value) {
        this.uid = value.replace("Meal_", "");
    }



    @DynamoDBRangeKey(attributeName = "sk")
    public String getSortKey() {
        if (this.id == null) {
            throw new IllegalStateException("Can not get valid sort key while id is null");
        }

        if (this.mealDate == null) {
            throw new IllegalStateException("Can not get valid sort key while mealDate is null");
        }

        return this.mealDate.toString() + "_" + this.id;
    }

    public void setSortKey(String sortKey) {
        this.mealDate = Instant.parse(sortKey.split("_")[0]);
        this.id = sortKey.split("_")[1];
    }

    @DynamoDBIgnore
    @JsonFormat(shape = JsonFormat.Shape.NUMBER_INT)
    public Instant getMealDate() {
        return mealDate;
    }

    @DynamoDBIgnore
    @JsonFormat(shape = JsonFormat.Shape.NUMBER_INT)
    public void setMealDate(Instant mealDate) {
        this.mealDate = mealDate;
    }

    @DynamoDBAttribute
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @DynamoDBAttribute
    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    @DynamoDBAttribute
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVenueMenuId() {
        return venueMenuId;
    }

    public void setVenueMenuId(String venueMenuId) {
        this.venueMenuId = venueMenuId;
    }
}
