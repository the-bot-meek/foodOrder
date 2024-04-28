package com.example.services;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.example.Exceptions.OrderRequestConverterException;
import com.example.dto.request.CreateOrderRequest;
import com.example.models.Meal;
import com.example.models.MenuItem;
import com.example.models.Order;
import com.example.models.Venue;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.exceptions.HttpStatusException;
import io.micronaut.security.authentication.Authentication;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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

    /**
     *
     * @param menuItems
     * @param venue
     * @return a list of MenuItems that don't belong to a Venue
     */
    private List<MenuItem> getInvalidMenuItemsForVenue(List<MenuItem> menuItems, Venue venue) {
        return menuItems.stream().filter(menuItem -> !venue.getMenuItems().contains(menuItem)).toList();
    }

    public Order convertCreateOrderRequestToOrder(CreateOrderRequest createOrderRequest, Authentication authentication) throws OrderRequestConverterException {
        final Optional<Meal> meal = mealService.getMeal(
                createOrderRequest.organizerUid(),
                createOrderRequest.dateOfMeal(),
                createOrderRequest.mealId()
        );

        // ToDo: Need to make a custom exception
        if (meal.isEmpty()) {
            throw new OrderRequestConverterException(String.format("Could not find Meal with id: %s", createOrderRequest.mealId()));
        }

        final Optional<Venue> venue = venueService.getVenue(
                meal.get().getLocation(),
                meal.get().getVenueName()
        );

        if (venue.isEmpty()) {
            throw new OrderRequestConverterException(
                    String.format("Could not find Venue with location: %s, name: %s", meal.get().getLocation(), meal.get().getVenueName())
            );
        }

        List<MenuItem> invalidMenuItems = getInvalidMenuItemsForVenue(createOrderRequest.menuItems(), venue.get());

        if (!invalidMenuItems.isEmpty()) {
            throw new OrderRequestConverterException("Invalid MenuItems");
        }


        return new Order(
                createOrderRequest.mealId(),
                createOrderRequest.dateOfMeal(),
                authentication.getName(),
                (String) authentication.getAttributes().get("preferred_username")
        );
    }

    public Order addOrder(CreateOrderRequest createOrderRequest, Authentication authentication) {
        Order order;
        try {
            order = convertCreateOrderRequestToOrder(createOrderRequest, authentication);
        } catch (OrderRequestConverterException e) {
            throw new HttpStatusException(HttpStatus.BAD_REQUEST, "Could not add order");
        }
        log.trace("Adding Order MealId: {}, uid: {}", order.getMealId(), order.getUid());
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
