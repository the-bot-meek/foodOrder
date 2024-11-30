package com.foodorder.server.services;

import com.foodorder.server.models.Venue;
import jakarta.inject.Named;
import jakarta.inject.Singleton;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;


@Singleton
public class VenueService {
    private final Logger log = LoggerFactory.getLogger(VenueService.class);
    private final IDynamoDBFacadeService dynamoDBFacadeService;

    public VenueService(
            @Named("primary-table") IDynamoDBFacadeService dynamoDBFacadeService
    ) {
        this.dynamoDBFacadeService = dynamoDBFacadeService;
    }

    public Optional<Venue> getVenue(String location, String name) {
        log.trace("Getting Venue location: {}, name: {}", location, name);
        return dynamoDBFacadeService.load(Venue.class,"Venue_" + location, name);
    }

    public List<Venue> listVenues(String location) {
        final String pk = "Venue_" + location;
        log.trace("Getting all venues for location:{}", location);
        Key key = Key.builder().partitionValue(pk).build();
        QueryConditional conditional = QueryConditional.keyEqualTo(key);
        return dynamoDBFacadeService.query(Venue.class, conditional);
    }



    public void addVenue(Venue venue) {
        log.trace("Adding Venue, venueId: {}", venue.getId());
        dynamoDBFacadeService.save(venue);
    }
}
