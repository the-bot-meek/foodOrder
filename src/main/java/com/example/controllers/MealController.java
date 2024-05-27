package com.example.controllers;

import com.example.Exceptions.MealRequestConverterException;
import com.example.dto.request.CreateMealRequest;
import com.example.dto.request.DeleteMealRequest;
import com.example.models.Meal.Meal;
import com.example.services.MealService;
import com.example.services.OrderService;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.*;
import io.micronaut.http.exceptions.HttpStatusException;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.rules.SecurityRule;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
import java.util.Optional;


@Controller("meal")
@Secured(SecurityRule.IS_AUTHENTICATED)
public class MealController {
    private final MealService mealService;
    private final Logger log = LoggerFactory.getLogger(MealController.class);
    private final OrderService orderService;

    MealController(MealService mealService, OrderService orderService) {
        this.mealService = mealService;
        this.orderService = orderService;
    }
    @Put
    public HttpResponse<Meal> addMeal(@Valid @Body CreateMealRequest createMealRequest, Authentication authentication) {
        try {
            log.info("Adding new Meal. CreateMealRequest: {}, uid: {}", createMealRequest, authentication.getName());
            return HttpResponse.ok(mealService.newMeal(createMealRequest, authentication.getName()));
        } catch (MealRequestConverterException e) {
            throw new HttpStatusException(HttpStatus.BAD_REQUEST, e);
        }
    }

    @Get("{mealSortKey}")
    public Optional<Meal> getMeal(@NotNull @NotBlank String mealSortKey, Authentication authentication) {
        log.info("Getting Meal. mealSortKey: {}, uid:{}", mealSortKey, authentication.getName());
        return mealService.getMeal(authentication.getName(), mealSortKey);
    }

    @Get
    public List<Meal> listMeals(Authentication authentication) {
        log.info("Getting all meals for uid: {}", authentication.getName());
        return mealService.getListOfMeals(authentication.getName());
    }

    @Delete
    public void deleteMeal(@Valid @Body DeleteMealRequest deleteMealRequest) {
        mealService.deleteMeal(deleteMealRequest.uid(), deleteMealRequest.mealDate(), deleteMealRequest.id());
        orderService.deleteAllOrdersForMeal(deleteMealRequest.id());
    }
}
