package com.example.services;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.example.dto.CreateVenueRequest;
import com.example.models.Venue;
import io.micronaut.context.annotation.Value;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.exceptions.HttpStatusException;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

@Singleton
public class VenueService {

    public final AmazonDynamoDB dynamoDbClient;
    private final DynamoDBMapper dynamoDBMapper;
    private final Logger log = LoggerFactory.getLogger(VenueService.class);

    private final LocationService locationService;

    public VenueService(
            @Value("${micronaut.dynamodb.primary_table.region}") String endpoint,
            @Value("${micronaut.dynamodb.primary_table.endpoint}") String region,
            LocationService locationService
    ) {
        this.dynamoDbClient = AmazonDynamoDBClientBuilder.standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(endpoint, region))
                .build();
        this.dynamoDBMapper = new DynamoDBMapper(dynamoDbClient);
        this.locationService = locationService;
    }

    public Optional<Venue> getValue(String location, String name) {
        Venue venue = dynamoDBMapper.load(Venue.class,"Venue_" + location, name);
        if (venue == null) {
            log.trace("Getting Venue location: {}, name:{}", location, name);
            return Optional.empty();
        }
        log.trace("Getting Venue id:{}, location: {}, name:{}", venue.getId(), location, name);
        return Optional.of(venue);
    }

    public List<Venue> listVenues(String location) {
        final String pk = "Venue_" + location;
        log.trace("Getting all venues for location:{}", location);
        Map<String, AttributeValue> eav = new HashMap<>();
        eav.put(":PK", new AttributeValue().withS(pk));
        DynamoDBQueryExpression<Venue> dynamoDBQueryExpression = new DynamoDBQueryExpression<Venue>()
                .withKeyConditionExpression("pk = :PK")
                .withExpressionAttributeValues(eav);
        return dynamoDBMapper.query(Venue.class, dynamoDBQueryExpression);
    }

    public Venue convertMealRequestIntoMeal(CreateVenueRequest createVenueRequest) {
        final String id = UUID.randomUUID().toString();
        return new Venue(
                id,
                createVenueRequest.name(),
                createVenueRequest.location(),
                createVenueRequest.description(),
                createVenueRequest.menuItems()
        );
    }

    public Venue addVenue(CreateVenueRequest createVenueRequest) {
        if (!locationService.listLocation().contains(createVenueRequest.location())) {
            throw new HttpStatusException(HttpStatus.BAD_REQUEST, String.format("Invalid Location %s", createVenueRequest.location()));
        }
        Venue venue = convertMealRequestIntoMeal(createVenueRequest);
        dynamoDBMapper.save(venue);
        return venue;
    }
}
