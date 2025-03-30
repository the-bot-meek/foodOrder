package com.foodorder.server.models;

import com.foodorder.server.models.meal.Meal;
import io.micronaut.serde.annotation.Serdeable;

import java.util.List;
import java.util.Map;

@Serdeable
public record MealReportModels(
        List<Order> orders,
        Meal meal,
        Map<MenuItem, Long> countedOrders
) {

}
