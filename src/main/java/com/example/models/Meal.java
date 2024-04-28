package com.example.models;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.micronaut.serde.annotation.Serdeable;

import java.time.Instant;
import java.util.Objects;

@DynamoDBTable(tableName = "primary_table")
@Serdeable
public class Meal {
    private String id;
    private String uid;
    private String name;
    private Instant mealDate;
    private String location;
    private String venueName;

    public Meal(String id, String organiserId, String name, Instant mealDate, String location, String venueName) {
        this.id = id;
        this.uid = organiserId;
        this.name = name;
        this.mealDate = mealDate;
        this.location = location;
        this.venueName = venueName;
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

        return this.mealDate + "_" + this.id;
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

    @DynamoDBAttribute
    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @DynamoDBAttribute
    public String getVenueName() {
        return venueName;
    }

    public void setVenueName(String venueName) {
        this.venueName = venueName;
    }


    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof Meal meal)) return false;

        if (!Objects.equals(id, meal.id)) return false;
        if (!Objects.equals(uid, meal.uid)) return false;
        if (!Objects.equals(name, meal.name)) return false;
        return Objects.equals(mealDate, meal.mealDate);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (uid != null ? uid.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (mealDate != null ? mealDate.hashCode() : 0);
        return result;
    }
}
