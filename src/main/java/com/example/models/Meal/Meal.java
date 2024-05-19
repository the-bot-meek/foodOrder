package com.example.models.Meal;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import java.time.Instant;
import java.util.Objects;


public class Meal extends AbstractMeal {
    public Meal(String uid, Instant mealDate, String id) {
        super(uid, mealDate, id);
    }

    public Meal() {
        super();
    }

    public Meal(String id, String uid, String name, Instant dateOfMeal, String location, String venueName) {
        super(id, uid, name, dateOfMeal, location, venueName);
    }


    @Override
    @DynamoDBIgnore
    public String getPrimaryKeySuffix() {
        return "Meal_";
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
        return Objects.equals(venueName, meal.venueName);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Meal{");
        sb.append("id='").append(id).append('\'');
        sb.append(", uid='").append(uid).append('\'');
        sb.append(", name='").append(name).append('\'');
        sb.append(", dateOfMeal=").append(mealDate);
        sb.append(", location='").append(location).append('\'');
        sb.append(", venueName='").append(venueName).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
