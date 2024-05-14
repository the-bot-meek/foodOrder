package com.example.services;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.example.Exceptions.OrderRequestConverterException;
import com.example.dto.request.CreateOrderRequest;
import com.example.models.Meal;
import com.example.models.MenuItem;
import com.example.models.Order;
import com.example.models.Venue;
import io.micronaut.security.authentication.Authentication;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

@Singleton
public class OrderService {
    private final Logger log = LoggerFactory.getLogger(MealService.class);
    private final IDynamoDBFacadeService dynamoDBFacadeService;
    private final VenueService venueService;
    private final MealService mealService;

    public OrderService(
            IDynamoDBFacadeService dynamoDBFacadeService,
            VenueService venueService,
            MealService mealService
    ) {
        this.dynamoDBFacadeService = dynamoDBFacadeService;
        this.venueService = venueService;
        this.mealService = mealService;
    }

    private List<MenuItem> getInvalidMenuItemsForVenue(Set<MenuItem> menuItems, Venue venue) {
        return menuItems.stream().filter(menuItem -> !venue.getMenuItems().contains(menuItem)).toList();
    }

    public Order convertCreateOrderRequestToOrder(CreateOrderRequest createOrderRequest, Authentication authentication) throws OrderRequestConverterException {
        final Optional<Meal> mealOptional = mealService.getMeal(
                createOrderRequest.organizerUid(),
                createOrderRequest.dateOfMeal(),
                createOrderRequest.mealId()
        );

        if (mealOptional.isEmpty()) {
            log.trace("Could not find Meal organizerUid: {}, dateOfMeal: {}, mealId: {}.",
                    createOrderRequest.organizerUid(),
                    createOrderRequest.dateOfMeal(),
                    createOrderRequest.mealId()
            );
            throw new OrderRequestConverterException(
                    String.format("Could not find Meal organizerUid: %s, dateOfMeal: %s, mealId: %s.", createOrderRequest.organizerUid(),
                    createOrderRequest.dateOfMeal(),
                    createOrderRequest.mealId()
                )
            );
        }

        Meal meal = mealOptional.get();
        final Optional<Venue> venueOptional = venueService.getVenue(
                meal.getLocation(),
                meal.getVenueName()
        );

        if (venueOptional.isEmpty()) {
            log.trace("Could not find Venue location: {}, name: {}", meal.getLocation(), meal.getVenueName());
            throw new OrderRequestConverterException(
                    String.format("Could not find Venue with location: %s, name: %s", mealOptional.get().getLocation(), mealOptional.get().getVenueName())
            );
        }
        Venue venue = venueOptional.get();

        List<MenuItem> invalidMenuItems = getInvalidMenuItemsForVenue(createOrderRequest.menuItems(), venue);

        if (!invalidMenuItems.isEmpty()) {
            log.trace("Found {} invalid invalidMenuItems for venue id: {}, location: {}, name: {}.",
                    invalidMenuItems.size(),
                    venue.getId(),
                    venue.getLocation(),
                    venue.getName()
            );
            throw new OrderRequestConverterException("Invalid MenuItems");
        }

        String orderId = UUID.randomUUID().toString();
        return new Order(
                orderId,
                meal,
                authentication.getName(),
                (String) authentication.getAttributes().get("preferred_username"),
                createOrderRequest.menuItems()
        );
    }

    public Order addOrder(CreateOrderRequest createOrderRequest, Authentication authentication) throws OrderRequestConverterException {
        Order order = convertCreateOrderRequestToOrder(createOrderRequest, authentication);
        dynamoDBFacadeService.save(order);
        return order;
    }

    public List<Order> getOrderFromMealId(String mealId) {
        final String pk = "Order_" + mealId;
        log.trace("Getting all orders for meal:{}", mealId);
        Map<String, AttributeValue> eav = new HashMap<>();
        eav.put(":PK", new AttributeValue().withS(pk));
        DynamoDBQueryExpression<Order> dynamoDBQueryExpression = new DynamoDBQueryExpression<Order>()
                .withKeyConditionExpression("meal_id = :PK")
                .withExpressionAttributeValues(eav);
        return dynamoDBFacadeService.query(Order.class, dynamoDBQueryExpression);
    }

    public List<Order> listOrdersFromUserID(String uid) {
        final String pk = "Order_" + uid;
        log.trace("Getting all orders for meal:{}", uid);
        Map<String, AttributeValue> eav = new HashMap<>();
        eav.put(":PK", new AttributeValue().withS(pk));
        DynamoDBQueryExpression<Order> dynamoDBQueryExpression = new DynamoDBQueryExpression<Order>()
                .withIndexName("uid_gsi")
                .withConsistentRead(false)
                .withKeyConditionExpression("uid = :PK")
                .withExpressionAttributeValues(eav);
        return dynamoDBFacadeService.query(Order.class, dynamoDBQueryExpression);
    }
}
