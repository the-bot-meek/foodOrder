package com.example.controllers;

import com.example.models.Order;
import com.example.services.OrderService;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Controller("/meal")
@Secured(SecurityRule.IS_AUTHENTICATED)
public class MealOrderController {
    private final OrderService orderService;
    private final Logger log = LoggerFactory.getLogger(MealOrderController.class);
    MealOrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @Get("/{mealId}/orders")
    public List<Order> listAllOrdersForMeal(String mealId) {
        log.info("Getting all Orders for mealId: {}", mealId);
        return orderService.getOrderFromMealId(mealId);
    }
}
