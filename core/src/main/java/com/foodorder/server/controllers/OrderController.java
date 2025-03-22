package com.foodorder.server.controllers;

import com.foodorder.server.converters.CreateOrderRequestConverter;
import com.foodorder.server.exceptions.OrderRequestConverterException;
import com.foodorder.server.request.CreateOrderRequest;
import com.foodorder.server.models.Order;
import com.foodorder.server.repository.OrderRepository;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Error;
import io.micronaut.http.annotation.Post;
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
    private final OrderRepository orderRepository;
    private final CreateOrderRequestConverter createOrderRequestConverter;
    OrderController(OrderRepository orderRepository, CreateOrderRequestConverter createOrderRequestConverter) {
        this.orderRepository = orderRepository;
        this.createOrderRequestConverter = createOrderRequestConverter;
    }
    @Post
    public Order addOrder(@Valid @Body CreateOrderRequest createOrderRequest, Authentication authentication) throws OrderRequestConverterException {
        log.info("Adding Order createOrderRequest: {}, uid: {}", createOrderRequest, authentication.getName());
        Order order = createOrderRequestConverter.convertCreateOrderRequestToOrder(
                createOrderRequest,
                authentication.getName(),
                (String) authentication.getAttributes().get("name")
        );
        orderRepository.addOrder(order);
        return order;
    }

    @Error(OrderRequestConverterException.class)
    <T> HttpResponse<T> handleOrderRequestConverterException(OrderRequestConverterException e) {
        log.error("Failed to add Order", e);
        return HttpResponse.status(HttpStatus.BAD_REQUEST);
    }
}
