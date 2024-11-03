package com.example.exceptions.missingRequredLinkedEntityExceptions;

import java.time.Instant;

public class MissingMealLinkedEntityException extends MissingRequredLinkedEntityException {
    public MissingMealLinkedEntityException(String meal_uid, Instant mealDate, String mealId) {
        super("Can not find meal uid: " + meal_uid + " id: " + mealId + " date: " + mealDate);
    }

    public MissingMealLinkedEntityException(String meal_uid, String sk) {
        super("Can not find meal uid: " + meal_uid + " sk: " + sk);
    }
}
