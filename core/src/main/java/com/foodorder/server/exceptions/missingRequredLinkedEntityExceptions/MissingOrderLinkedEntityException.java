package com.foodorder.server.exceptions.missingRequredLinkedEntityExceptions;

public class MissingOrderLinkedEntityException extends MissingRequredLinkedEntityException {
    public MissingOrderLinkedEntityException(String menuLocation, String menuName) {
        super("Could not find Menu. Location: " + menuLocation + ", Name: " + menuName);
    }
}
