package com.foodorder.server.models;

import com.foodorder.server.dynamodbTypeConverters.MenuItemSetConverter;
import com.foodorder.server.dynamodbTypeConverters.OrderParticipantConverter;
import com.foodorder.server.models.meal.Meal;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.foodorder.server.models.orderParticipant.OrderParticipant;
import io.micronaut.serde.annotation.Serdeable;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.*;

import java.time.Instant;
import java.util.Objects;
import java.util.Set;

@DynamoDbBean
@Serdeable
public class Order implements Model {
    private String id;
    private Meal meal;
    private OrderParticipant orderParticipant;
    private Set<MenuItem> menuItems;
    private boolean submitted = false;

    public Order(String id, Meal meal, OrderParticipant orderParticipant, Set<MenuItem> menuItems, boolean submitted) {
        this.id = id;
        this.meal = meal;
        this.orderParticipant = orderParticipant;
        this.menuItems = menuItems;
        this.submitted = submitted;
    }

    public Order(Meal meal, OrderParticipant orderParticipant, Set<MenuItem> menuItems) {
        this.meal = meal;
        this.orderParticipant = orderParticipant;
        this.menuItems = menuItems;
    }

    public Order() {
        this.meal = new Meal();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Meal getMeal() {
        return meal;
    }

    public void setMeal(Meal meal) {
        this.meal = meal;
    }

    @Override
    @DynamoDbPartitionKey
    @DynamoDbAttribute("meal_id")
    @DynamoDbSecondarySortKey(indexNames = "uid_gsi")
    public String getPrimaryKey() {
        return "Order_" + meal.getId();
    }

    public void setPrimaryKey(String pk) {
        this.meal.setId(pk.split("_")[1]);
    }

    @Override
    @DynamoDbAttribute("date_of_meal")
    @DynamoDbSortKey
    public String getSortKey() {
        if (meal.getMealDate() == null) return null;
        return meal.getMealDate().toString();
    }

    public void setSortKey(String sk) {
        this.meal.setMealDate(Instant.parse(sk));
    }

    @DynamoDbAttribute("uid")
    @DynamoDbSecondaryPartitionKey(indexNames = "uid_gsi")
    public String getGSIPrimaryKey() {
        return "Order_" + orderParticipant.getUserId() + "_" + this.orderParticipant.getKey();
    }

    public void setGSIPrimaryKey(String primaryKeyGSI) {

    }

    @DynamoDbConvertedBy(MenuItemSetConverter.class)
    public Set<MenuItem> getMenuItems() {
        return menuItems;
    }

    public void setMenuItems(Set<MenuItem> menuItems) {
        this.menuItems = menuItems;
    }


    @DynamoDbConvertedBy(OrderParticipantConverter.class)
    public OrderParticipant getOrderParticipant() {
        return orderParticipant;
    }

    public void setOrderParticipant(OrderParticipant orderParticipant) {
        this.orderParticipant = orderParticipant;
    }

    public boolean isSubmitted() {
        return submitted;
    }

    public void setSubmitted(boolean submitted) {
        this.submitted = submitted;
    }


    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Order order)) return false;

        return submitted == order.submitted && Objects.equals(id, order.id) && Objects.equals(meal, order.meal) && Objects.equals(orderParticipant, order.orderParticipant) && Objects.equals(menuItems, order.menuItems);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(id);
        result = 31 * result + Objects.hashCode(meal);
        result = 31 * result + Objects.hashCode(orderParticipant);
        result = 31 * result + Objects.hashCode(menuItems);
        result = 31 * result + Boolean.hashCode(submitted);
        return result;
    }
}
