package com.foodorder.server.controllers;

import com.foodorder.server.models.Order;
import com.foodorder.server.repository.OrderRepository;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.rules.SecurityRule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;

@Controller("/meal")
@Secured(SecurityRule.IS_AUTHENTICATED)
public class MealOrderController {
    private final OrderRepository orderRepository;
    private final Logger log = LoggerFactory.getLogger(MealOrderController.class);
    MealOrderController(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Get("/{mealId}/orders")
    public HttpResponse<List<Order>> listAllOrdersForMeal(String mealId, Authentication authentication) {
        log.info("Getting all Orders for mealId: {}", mealId);
        List<Order> orders = orderRepository.getOrderFromMealId(mealId);
        if (!validateListOfOrders(orders, authentication)) {
            log.error("Not all meals returned belong to this user.");
            return HttpResponse.serverError();
        }
        return HttpResponse.ok(orders);
    }

    private boolean validateListOfOrders(List<Order> orders, Authentication authentication) {
        if (orders.isEmpty()) return true;
        Order order = orders.getFirst();

        System.out.println("look at me");
        boolean uniform = orders.stream().allMatch(it ->
                Objects.equals(it.getMeal().getId(), order.getMeal().getId())
                && it.getMeal().getMealDate().equals(order.getMeal().getMealDate())
        );
        if (!uniform) return false;
        return (authentication.getName().equals(order.getMeal().getUid()));
    }
}
