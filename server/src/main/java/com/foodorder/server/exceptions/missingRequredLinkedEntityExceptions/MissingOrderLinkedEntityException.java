package com.foodorder.server.exceptions.missingRequredLinkedEntityExceptions;

public class MissingOrderLinkedEntityException extends MissingRequredLinkedEntityException {
    public MissingOrderLinkedEntityException(String venueLocation, String venueName) {
        super("Could not find Venue. Location: " + venueLocation + ", Name: " + venueName);
    }
}
