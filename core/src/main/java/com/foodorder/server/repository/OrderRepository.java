package com.foodorder.server.repository;

import com.foodorder.server.models.Order;
import com.foodorder.server.models.meal.Meal;
import com.foodorder.server.models.orderParticipant.AnonomusOrderParticipant;
import jakarta.inject.Named;
import jakarta.inject.Singleton;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;

import java.util.*;

@Singleton
public class OrderRepository {
    private final Logger log = LoggerFactory.getLogger(MealRepository.class);
    private final IDynamoDBFacadeRepository dynamoDBFacadeRepository;

    public OrderRepository(
            @Named("order-table") IDynamoDBFacadeRepository dynamoDBFacadeRepository
    ) {
        this.dynamoDBFacadeRepository = dynamoDBFacadeRepository;
    }

    public void addOrder(Order order) {
        dynamoDBFacadeRepository.save(order);
    }

    public List<Order> getOrderFromMealId(String mealId) {
        final String pk = "Order_" + mealId;
        log.trace("Getting all orders for meal:{}", mealId);
        Key key = Key.builder().partitionValue(pk).build();
        QueryConditional queryConditional = QueryConditional.keyEqualTo(key);
        return dynamoDBFacadeRepository.query(Order.class, queryConditional);
    }

    public void deleteAllOrdersForMeal(String mealId) {
        log.trace("Deleting all Orders for mealId: {}", mealId);
        List<Order> orders = getOrderFromMealId(mealId);
        dynamoDBFacadeRepository.batchDelete(orders);
    }

    public List<Order> listOrdersFromUserID(String uid) {
        // Anonymise users can't have more than one meal, so we don't need to check for them
        final String pk = "Order_" + uid + "_AUTHENTICATED";
        log.trace("Getting all orders for meal:{}", uid);
        Key key = Key.builder().partitionValue(pk).build();
        QueryConditional queryConditional = QueryConditional.keyEqualTo(key);
        return dynamoDBFacadeRepository.queryWithIndex(Order.class, queryConditional, "uid_gsi");
    }

    public Optional<Order> getAnonymousOrder(String uid, String mealId) {
        final String pk = "Order_" + uid + "_ANONYMOUS";
        log.trace("Getting all AnonymousOrders for meal:{}", uid);
        Key key = Key.builder().partitionValue(pk).sortValue("Order_" + mealId).build();
        QueryConditional queryConditional = QueryConditional.keyEqualTo(key);
        List<Order> orders = dynamoDBFacadeRepository.queryWithIndex(Order.class, queryConditional, "uid_gsi");
        if (orders.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(orders.getFirst());
    }

    public void batchSave(List<Order> orders) {
        dynamoDBFacadeRepository.batchSave(orders);
    }

    public void addOrdersForPrivateMeal(@NotNull Meal meal, Set<String> recipientIds) {
        // Null private meal config to prevent leaking of other private order ids.
        meal.getMealConfig().setPrivateMealConfig(null);
        final List<Order> orders = recipientIds.stream().map(
                uid -> new Order(meal, new AnonomusOrderParticipant("AnonymousUser", uid), new HashSet<>())
        ).toList();
        batchSave(orders);
    }
}
