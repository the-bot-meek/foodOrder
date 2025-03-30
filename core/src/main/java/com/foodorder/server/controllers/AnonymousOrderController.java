package com.foodorder.server.controllers;

import com.foodorder.server.converters.CreateOrderRequestConverter;
import com.foodorder.server.exceptions.missingExistingEntityException.MissingExistingOrderException;
import com.foodorder.server.models.Order;
import com.foodorder.server.request.CreateOrderRequest;
import com.foodorder.server.exceptions.OrderRequestConverterException;
import com.foodorder.server.exceptions.missingRequredLinkedEntityExceptions.MissingMealLinkedEntityException;
import com.foodorder.server.exceptions.missingRequredLinkedEntityExceptions.MissingRequredLinkedEntityException;
import com.foodorder.server.models.meal.Meal;
import com.foodorder.server.repository.MealRepository;
import com.foodorder.server.repository.OrderRepository;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.*;
import io.micronaut.http.exceptions.HttpStatusException;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.rules.SecurityRule;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.Optional;

@Controller("/anonymousOrder")
@Secured(SecurityRule.IS_ANONYMOUS)
public class AnonymousOrderController {
    private final OrderRepository orderRepository;
    private final MealRepository mealRepository;
    private final CreateOrderRequestConverter createOrderRequestConverter;
    private final Logger log = LoggerFactory.getLogger(AnonymousOrderController.class);
    private final static String DEFAULT_AnonymousUser_NAME = "AnonymousUser";

    public AnonymousOrderController(OrderRepository orderRepository, MealRepository mealRepository, CreateOrderRequestConverter createOrderRequestConverter) {
        this.orderRepository = orderRepository;
        this.createOrderRequestConverter = createOrderRequestConverter;
        this.mealRepository = mealRepository;
    }

    @Get("{uid}/{mealId}")
    public Optional<Order> getAnonymousOrder(@PathVariable String uid, @PathVariable String mealId) {
        return this.orderRepository.getAnonymousOrder(uid, mealId);
    }

    @Post("{uid}")
    public HttpResponse<Order> addAnonymousOrder(@Body CreateOrderRequest createOrderRequest, @PathVariable String uid) {
        try {
            Order order = createOrderRequestConverter.convertCreateOrderRequestToOrder(createOrderRequest, uid, DEFAULT_AnonymousUser_NAME, true);
            orderRepository.addOrder(order);
            return HttpResponse.ok(order);
        } catch (OrderRequestConverterException orderRequestConverterException) {
            log.error("Error adding AnonymousOrder uid: {}", uid, orderRequestConverterException);
            return HttpResponse.badRequest();
        }
    }

    @Put("{uid}")
    public Order updateAnonymousOrder(@Valid @Body Order order, @PathVariable String uid) throws  MissingExistingOrderException {
        Optional<Order> existingOrder = orderRepository.getAnonymousOrder(uid, order.getMeal().getId());
        if (existingOrder.isEmpty()) {
            throw new MissingExistingOrderException(uid, order.getMeal().getId());
        }
        orderRepository.addOrder(order);
        return order;
    }

    @Post("addBlankOrdersForMeal/{sortKey}")
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public void addOrdersForMeal(@PathVariable String sortKey, Authentication authentication) throws MissingRequredLinkedEntityException {
        Optional<Meal> mealOptional = mealRepository.getMeal(authentication.getName(), sortKey);
        if (mealOptional.isEmpty()) {
            throw new MissingMealLinkedEntityException(authentication.getName(), sortKey);
        }

        Meal meal = mealOptional.get();
        if (meal.getMealConfig() == null || meal.getMealConfig().getPrivateMealConfig() == null) {
            throw new HttpStatusException(HttpStatus.BAD_REQUEST, "Can not add blank orders for non anomalous meal");
        }
        orderRepository.addOrdersForPrivateMeal(meal, meal.getMealConfig().getPrivateMealConfig().getRecipientIds());
    }
}
