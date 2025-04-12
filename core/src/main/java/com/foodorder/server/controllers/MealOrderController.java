package com.foodorder.server.controllers;

import com.foodorder.server.exceptions.missingExistingEntityException.MissingExistingMealException;
import com.foodorder.server.models.MealReportModels;
import com.foodorder.server.models.MenuItem;
import com.foodorder.server.models.Order;
import com.foodorder.server.models.meal.Meal;
import com.foodorder.server.repository.MealRepository;
import com.foodorder.server.repository.OrderRepository;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Produces;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.rules.SecurityRule;
import io.micronaut.views.View;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Controller("/meal")
@Secured(SecurityRule.IS_AUTHENTICATED)
public class MealOrderController {
    private final OrderRepository orderRepository;
    private final MealRepository mealRepository;
    private final Logger log = LoggerFactory.getLogger(MealOrderController.class);
    public MealOrderController(OrderRepository orderRepository, MealRepository mealRepository) {
        this.orderRepository = orderRepository;
        this.mealRepository = mealRepository;
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
        boolean uniform = orders.stream().allMatch(it ->
                Objects.equals(it.getMeal().getId(), order.getMeal().getId())
                && it.getMeal().getMealDate().equals(order.getMeal().getMealDate())
        );
        if (!uniform) return false;
        return (authentication.getName().equals(order.getMeal().getUid()));
    }

    @Produces(MediaType.TEXT_HTML)
    @View("meal/report.html")
    @Get("export/{mealDateTimeStamp}/{id}")
    public MealReportModels exportMealReport(long mealDateTimeStamp, @NotNull String id, Authentication authentication) throws MissingExistingMealException {
        Instant mealDate = Instant.ofEpochMilli(mealDateTimeStamp);
        Optional<Meal> optionalMeal = this.mealRepository.getMeal(authentication.getName(), mealDate + "_" + id);

        if (optionalMeal.isEmpty()) {
            throw new MissingExistingMealException(authentication.getName(), id, mealDateTimeStamp);
        }

        Meal meal = optionalMeal.get();

        List<Order> orders = orderRepository.getOrderFromMealId(meal.getId());

        Map<MenuItem, Long> countedOrders = orders.stream().map(Order::getMenuItems).flatMap(Set::stream)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        return new MealReportModels(orders, meal, countedOrders);
    }
}
