package com.example.controllers;

import com.example.converters.CreateOrderRequestConverter;
import com.example.exceptions.OrderRequestConverterException;
import com.example.dto.request.CreateOrderRequest;
import com.example.models.Order;
import com.example.services.OrderService;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Put;
import io.micronaut.http.exceptions.HttpStatusException;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.rules.SecurityRule;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller("order")
@Secured(SecurityRule.IS_AUTHENTICATED)
public class OrderController {
    private final Logger log = LoggerFactory.getLogger(OrderController.class);
    private final OrderService  orderService;
    private final CreateOrderRequestConverter createOrderRequestConverter;
    OrderController(OrderService orderService, CreateOrderRequestConverter createOrderRequestConverter) {
        this.orderService = orderService;
        this.createOrderRequestConverter = createOrderRequestConverter;
    }
    @Put
    public Order addOrder(@Valid @Body CreateOrderRequest createOrderRequest, Authentication authentication) {
        log.info("Adding Order createOrderRequest: {}, uid: {}", createOrderRequest, authentication.getName());
        try {
            Order order = createOrderRequestConverter.convertCreateOrderRequestToOrder(
                    createOrderRequest,
                    authentication.getName(),
                    (String) authentication.getAttributes().get("preferred_username")
            );
            orderService.addOrder(order);
            return order;
        } catch (OrderRequestConverterException e) {
            log.error(String.valueOf(e));
            throw new HttpStatusException(HttpStatus.BAD_REQUEST, e);
        }
    }
}
