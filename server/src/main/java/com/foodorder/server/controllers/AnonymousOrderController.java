package com.foodorder.server.controllers;

import com.foodorder.server.converters.CreateOrderRequestConverter;
import com.foodorder.server.request.CreateOrderRequest;
import com.foodorder.server.exceptions.OrderRequestConverterException;
import com.foodorder.server.models.AnonymousOrder;
import com.foodorder.server.services.MealService;
import com.foodorder.server.services.OrderService;
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
    private final static String DEFAULT_AnonymousUser_NAME = "AnonymousUser";

    public AnonymousOrderController(OrderService orderService, CreateOrderRequestConverter createOrderRequestConverter) {
        this.orderService = orderService;
        this.createOrderRequestConverter = createOrderRequestConverter;
    }

    @Get("{uid}/{mealId}")
    public Optional<AnonymousOrder> getAnonymousOrder(@PathVariable String uid, @PathVariable String mealId) {
        return this.orderService.getAnonymousOrder(uid, mealId);
    }

    @Post("{uid}")
    public HttpResponse<AnonymousOrder> addAnonymousOrder(@Body CreateOrderRequest createOrderRequest, @PathVariable String uid) {
        try {
            AnonymousOrder order = (AnonymousOrder) createOrderRequestConverter.convertCreateOrderRequestToOrder(createOrderRequest, uid, DEFAULT_AnonymousUser_NAME, true);
            orderService.addOrder(order);
            return HttpResponse.ok(order);
        } catch (OrderRequestConverterException orderRequestConverterException) {
            log.error("Error adding AnonymousOrder uid: {}", uid, orderRequestConverterException);
            return HttpResponse.badRequest();
        }
    }
}
