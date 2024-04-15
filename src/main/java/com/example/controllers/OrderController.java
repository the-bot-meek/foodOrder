package com.example.controllers;

import com.example.dto.request.CreateOrderRequest;
import com.example.models.Order;
import com.example.services.OrderService;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Put;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import jakarta.validation.Valid;
import java.security.Principal;

@Controller("order")
@Secured(SecurityRule.IS_AUTHENTICATED)
public class OrderController {
    OrderService orderService;
    OrderController(OrderService orderService) {
        this.orderService = orderService;
    }
    @Put
    public Order addOrder(@Valid @Body CreateOrderRequest createOrderRequest, Principal principal) {
        return orderService.addOrder(createOrderRequest, principal.getName());
    }
}
