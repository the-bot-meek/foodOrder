package com.foodorder.server.exceptions.missingExistingEntityException;

import com.foodorder.server.exceptions.missingRequredLinkedEntityExceptions.MissingMealLinkedEntityException;

public class MissingExistingOrderException extends MissingExistingEntityException {
    public MissingExistingOrderException(String userId, String mealId) {
        super(String.format("Can't find Order. user id: %s, meal id %s", userId, mealId));
    }
}
