package com.example.controllers;

import com.example.converters.CreateOrderRequestConverter;
import com.example.dto.request.CreateOrderRequest;
import com.example.exceptions.OrderRequestConverterException;
import com.example.models.AnonymousOrder;
import com.example.services.OrderService;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Optional;

@Controller("/AnonymousOrder")
@Secured(SecurityRule.IS_ANONYMOUS)
public class AnonymousOrderController {
    private final OrderService orderService;
    private final CreateOrderRequestConverter createOrderRequestConverter;
    private final Logger log = LoggerFactory.getLogger(AnonymousOrderController.class);

    AnonymousOrderController(OrderService orderService, CreateOrderRequestConverter createOrderRequestConverter) {
        this.orderService = orderService;
        this.createOrderRequestConverter = createOrderRequestConverter;
    }

    @Get("{uid}/{mealId}")
    public Optional<AnonymousOrder> getAnonymousOrder(@PathVariable String uid, @PathVariable String mealId) {
        return this.orderService.getAnonymousOrder(uid, mealId);
    }

    @Put("{uid}")
    public HttpResponse<AnonymousOrder> addAnonymousOrder(@Body CreateOrderRequest createOrderRequest, @PathVariable String uid) {
        try {
            AnonymousOrder order = (AnonymousOrder) this.createOrderRequestConverter.convertCreateOrderRequestToOrder(createOrderRequest, uid, "AnonymousUser", true);
            orderService.addOrder(order);
            return HttpResponse.ok(order);
        } catch (OrderRequestConverterException orderRequestConverterException) {
            log.error("Error adding AnonymousOrder uid: {}", uid);
            return HttpResponse.badRequest();
        }
    }
}
