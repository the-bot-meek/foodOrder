package com.foodorder.server.models.meal;

import com.foodorder.server.models.Model;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.micronaut.serde.annotation.Serdeable;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.*;

import java.time.Instant;
import java.util.Objects;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, defaultImpl = Meal.class)
@JsonSubTypes({@JsonSubTypes.Type(value = Meal.class, name = "Meal"), @JsonSubTypes.Type(value = DraftMeal.class, name = "DraftMeal")})
@DynamoDbBean
@Serdeable
public class Meal implements Model {
    protected String id;
    protected String uid;
    protected String name;
    protected Instant mealDate;
    protected String location;
    protected String venueName;
    protected Boolean isPrivate = false;

    public Meal(String id, String organiserId, String name, Instant mealDate, String location, String venueName, Boolean isPrivate) {
        this.id = id;
        this.uid = organiserId;
        this.name = name;
        this.mealDate = mealDate;
        this.location = location;
        this.venueName = venueName;
        this.isPrivate = isPrivate;
    }

    public Meal() {

    }

    public Meal(String uid, Instant mealDate, String id) {
        this.uid = uid;
        this.mealDate = mealDate;
        this.id = id;
    }

    @Override
    @DynamoDbPartitionKey
    @DynamoDbAttribute("pk")
    public String getPrimaryKey() {
        return "Meal_" + this.uid;
    }

    public void setPrimaryKey(String value) {
        this.uid = value.replace("Meal_", "");
    }



    @Override
    @DynamoDbAttribute("sk")
    @DynamoDbSortKey
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

    @DynamoDbIgnore
    public Instant getMealDate() {
        return mealDate;
    }

    public void setMealDate(Instant mealDate) {
        this.mealDate = mealDate;
    }

    @DynamoDbAttribute("gsi_sk")
    @DynamoDbSecondarySortKey(indexNames = "gsi")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @DynamoDbAttribute("gsi_pk")
    @DynamoDbSecondaryPartitionKey(indexNames = "gsi")
    public String getVenueName() {
        return venueName;
    }

    public void setVenueName(String venueName) {
        this.venueName = venueName;
    }

    public Boolean getPrivate() {
        return isPrivate;
    }

    public void setPrivate(Boolean aPrivate) {
        isPrivate = aPrivate;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Meal meal)) return false;

        return Objects.equals(id, meal.id) && Objects.equals(uid, meal.uid) && Objects.equals(name, meal.name) && Objects.equals(mealDate, meal.mealDate) && Objects.equals(location, meal.location) && Objects.equals(venueName, meal.venueName) && Objects.equals(isPrivate, meal.isPrivate);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(id);
        result = 31 * result + Objects.hashCode(uid);
        result = 31 * result + Objects.hashCode(name);
        result = 31 * result + Objects.hashCode(mealDate);
        result = 31 * result + Objects.hashCode(location);
        result = 31 * result + Objects.hashCode(venueName);
        result = 31 * result + Objects.hashCode(isPrivate);
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Meal{");
        sb.append("id='").append(id).append('\'');
        sb.append(", uid='").append(uid).append('\'');
        sb.append(", name='").append(name).append('\'');
        sb.append(", mealDate=").append(mealDate);
        sb.append(", location='").append(location).append('\'');
        sb.append(", venueName='").append(venueName).append('\'');
        sb.append(", isPrivate=").append(isPrivate);
        sb.append('}');
        return sb.toString();
    }
}
