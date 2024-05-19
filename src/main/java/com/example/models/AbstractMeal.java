package com.example.models;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.micronaut.serde.annotation.Serdeable;

import java.time.Instant;
import java.util.Objects;

@DynamoDBTable(tableName = "primary_table")
@DynamoDBDocument
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME)
@JsonSubTypes({@JsonSubTypes.Type(value = Meal.class, name = "Meal"), @JsonSubTypes.Type(value = DraftMeal.class, name = "DraftMeal")})
@Serdeable
public abstract class AbstractMeal implements Model {
    protected String id;
    protected String uid;
    protected String name;
    protected Instant mealDate;
    protected String location;
    protected String venueName;

    public AbstractMeal(String id, String uid, String name, Instant mealDate, String location, String venueName) {
        this.id = id;
        this.uid = uid;
        this.name = name;
        this.mealDate = mealDate;
        this.location = location;
        this.venueName = venueName;
    }

    public AbstractMeal() {

    }

    public AbstractMeal(String uid, Instant mealDate, String id) {
        this.uid = uid;
        this.mealDate = mealDate;
        this.id = id;
    }

    @DynamoDBHashKey(attributeName = "pk")
    public String getPrimaryKey() {
        return getPrimaryKeySuffix() + this.getUid();
    }
    public void setPrimaryKey(String value) {
        this.setUid(value.replace(getPrimaryKeySuffix(), ""));
    }

    @DynamoDBRangeKey(attributeName = "sk")
    public String getSortKey() {
        if (this.id == null) {
            throw new IllegalStateException("Can not get valid sort key while id is null");
        }

        if (this.mealDate == null) {
            throw new IllegalStateException("Can not get valid sort key while dateOfMeal is null");
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
        if (!(object instanceof AbstractMeal that)) return false;

        if (!Objects.equals(id, that.id)) return false;
        if (!Objects.equals(uid, that.uid)) return false;
        if (!Objects.equals(name, that.name)) return false;
        if (!Objects.equals(mealDate, that.mealDate)) return false;
        if (!Objects.equals(location, that.location)) return false;
        return Objects.equals(venueName, that.venueName);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (uid != null ? uid.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (mealDate != null ? mealDate.hashCode() : 0);
        result = 31 * result + (location != null ? location.hashCode() : 0);
        result = 31 * result + (venueName != null ? venueName.hashCode() : 0);
        return result;
    }
}
