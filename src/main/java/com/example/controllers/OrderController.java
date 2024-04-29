package com.example.controllers;

import com.example.dto.request.CreateOrderRequest;
import com.example.models.Order;
import com.example.services.OrderService;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Put;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.rules.SecurityRule;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller("order")
@Secured(SecurityRule.IS_AUTHENTICATED)
public class OrderController {
    private final OrderService  orderService;
    private final Logger log = LoggerFactory.getLogger(OrderController.class);
    OrderController(OrderService orderService) {
        this.orderService = orderService;
    }
    @Put
    public Order addOrder(@Valid @Body CreateOrderRequest createOrderRequest, Authentication authentication) {
        log.info("Adding Order createOrderRequest: {}, uid: {}", createOrderRequest, authentication.getName());
        return orderService.addOrder(createOrderRequest, authentication);
    }
}