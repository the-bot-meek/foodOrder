package com.foodorder.server.exceptions.missingExistingEntityException;

public class MissingExistingMealException extends MissingExistingEntityException{
    public MissingExistingMealException(String uid, String id, long mealDateTimeStamp) {
        super(
            String.format("Can't find meal for uid: %s, id: %s, date: %s", uid, id, mealDateTimeStamp)
        );
    }
}
