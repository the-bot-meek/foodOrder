package com.example.controllers;

import com.example.models.Order;
import com.example.services.OrderService;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import java.util.List;

@Controller("/meal")
@Secured(SecurityRule.IS_AUTHENTICATED)
public class MealOrderController {
    private final OrderService orderService;
    MealOrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @Get("/{mealId}/orders")
    public List<Order> listAllOrdersForMeal(String mealId) {
        return orderService.getOrderFromMealId(mealId);
    }
}
