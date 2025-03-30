package com.foodorder.server.exceptions.missingExistingEntityException;

public abstract class MissingExistingEntityException extends Exception {
    public MissingExistingEntityException(String message) {
        super(message);
    }
}
