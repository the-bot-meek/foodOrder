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
    protected String menuName;
    protected MealConfig mealConfig;

    public Meal(String id, String organiserId, String name, Instant mealDate, String location, String menuName, MealConfig mealConfig) {
        this.id = id;
        this.uid = organiserId;
        this.name = name;
        this.mealDate = mealDate;
        this.location = location;
        this.menuName = menuName;
        this.mealConfig = mealConfig;
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
    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public MealConfig getMealConfig() {
        return mealConfig;
    }

    public void setMealConfig(MealConfig mealConfig) {
        this.mealConfig = mealConfig;
    }


    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof Meal meal)) return false;

        if (!Objects.equals(id, meal.id)) return false;
        if (!Objects.equals(uid, meal.uid)) return false;
        if (!Objects.equals(name, meal.name)) return false;
        if (!Objects.equals(mealDate, meal.mealDate)) return false;
        if (!Objects.equals(location, meal.location)) return false;
        return Objects.equals(menuName, meal.menuName);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (uid != null ? uid.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (mealDate != null ? mealDate.hashCode() : 0);
        result = 31 * result + (location != null ? location.hashCode() : 0);
        result = 31 * result + (menuName != null ? menuName.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Meal{");
        sb.append("id='").append(id).append('\'');
        sb.append(", uid='").append(uid).append('\'');
        sb.append(", name='").append(name).append('\'');
        sb.append(", dateOfMeal=").append(mealDate);
        sb.append(", location='").append(location).append('\'');
        sb.append(", menuName='").append(menuName).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
