package com.example.services;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.example.exceptions.missingRequredLinkedEntityExceptions.MissingOrderLinkedEntityException;
import com.example.exceptions.missingRequredLinkedEntityExceptions.MissingRequredLinkedEntityException;
import com.example.models.AnonymousOrder;
import com.example.models.Order;
import com.example.models.Venue;
import com.example.models.meal.Meal;
import jakarta.inject.Singleton;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

@Singleton
public class OrderService {
    private final Logger log = LoggerFactory.getLogger(MealService.class);
    private final IDynamoDBFacadeService dynamoDBFacadeService;
    private final VenueService venueService;

    public OrderService(
            IDynamoDBFacadeService dynamoDBFacadeService,
            VenueService venueService
    ) {
        this.dynamoDBFacadeService = dynamoDBFacadeService;
        this.venueService = venueService;
    }

    public void addOrder(Order order) {
        dynamoDBFacadeService.save(order);
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

    public void deleteAllOrdersForMeal(String mealId) {
        log.trace("Deleting all Orders for mealId: {}", mealId);
        List<Order> orders = getOrderFromMealId(mealId);
        dynamoDBFacadeService.batchDelete(orders);
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

    public Optional<AnonymousOrder> getAnonymousOrder(String uid, String mealId) {
        final String pk = "AnonymousOrder_" + uid;
        log.trace("Getting all AnonymousOrders for meal:{}", uid);
        Map<String, AttributeValue> eav = new HashMap<>();
        eav.put(":uid", new AttributeValue().withS(pk));
        eav.put(":meal_id", new AttributeValue().withS("Order_" + mealId));
        DynamoDBQueryExpression<AnonymousOrder> dynamoDBQueryExpression = new DynamoDBQueryExpression<AnonymousOrder>()
                .withIndexName("uid_gsi")
                .withConsistentRead(false)
                .withKeyConditionExpression("uid = :uid and meal_id = :meal_id")
                .withExpressionAttributeValues(eav);

        List<AnonymousOrder> orders = dynamoDBFacadeService.query(AnonymousOrder.class, dynamoDBQueryExpression);
        if (orders.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(orders.get(0));
    }

    public void batchSave(List<Order> orders) {
        dynamoDBFacadeService.batchSave(orders);
    }

    public void addOrdersForPrivateMeal(@NotNull Meal meal, Set<String> recipientIds) throws MissingRequredLinkedEntityException {
        Optional<Venue> venueOptional = venueService.getVenue(meal.getLocation(), meal.getVenueName());
        if (venueOptional.isPresent()) {
            final List<Order> orders = recipientIds.stream().map(
                    uid -> new Order(meal, uid, "AnonymousUser", venueOptional.get().getMenuItems())
            ).toList();
            batchSave(orders);
        } else {
            throw new MissingOrderLinkedEntityException(meal.getLocation(), meal.getName());
        }
    }
}
