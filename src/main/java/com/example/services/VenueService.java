package com.example.services;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.example.models.Venue;
import jakarta.inject.Singleton;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Singleton
public class VenueService {
    private final Logger log = LoggerFactory.getLogger(VenueService.class);
    private final IDynamoDBFacadeService dynamoDBFacadeService;

    public VenueService(
            IDynamoDBFacadeService dynamoDBFacadeService
    ) {
        this.dynamoDBFacadeService = dynamoDBFacadeService;
    }

    public Optional<Venue> getVenue(String location, String name) {
        return dynamoDBFacadeService.load(Venue.class,"Venue_" + location, name);
    }

    public List<Venue> listVenues(String location) {
        final String pk = "Venue_" + location;
        log.trace("Getting all venues for location:{}", location);
        Map<String, AttributeValue> eav = new HashMap<>();
        eav.put(":PK", new AttributeValue().withS(pk));
        DynamoDBQueryExpression<Venue> dynamoDBQueryExpression = new DynamoDBQueryExpression<Venue>()
                .withKeyConditionExpression("pk = :PK")
                .withExpressionAttributeValues(eav);
        return dynamoDBFacadeService.query(Venue.class, dynamoDBQueryExpression);
    }



    public void addVenue(Venue venue) {
        dynamoDBFacadeService.save(venue);
    }
}
