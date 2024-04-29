package com.example.controllers;

import com.example.Exceptions.MealRequestConverterException;
import com.example.dto.request.CreateMealRequest;
import com.example.models.Meal;
import com.example.services.MealService;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.*;
import io.micronaut.http.exceptions.HttpStatusException;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.Principal;
import java.util.List;
import java.util.Optional;


@Controller("meal")
@Secured(SecurityRule.IS_AUTHENTICATED)
public class MealController {
    private final MealService mealService;
    private final Logger log = LoggerFactory.getLogger(MealController.class);

    MealController(MealService mealService) {
        this.mealService = mealService;
    }
    @Put
    public HttpResponse<Meal> addMeal(@Valid @Body CreateMealRequest createMealRequest, Principal principal) {
        try {
            log.info("Adding new Meal. CreateMealRequest: {}, uid: {}", createMealRequest, principal.getName());
            return HttpResponse.ok(mealService.newMeal(createMealRequest, principal.getName()));
        } catch (MealRequestConverterException e) {
            throw new HttpStatusException(HttpStatus.BAD_REQUEST, e);
        }
    }

    @Get("{mealSortKey}")
    public Optional<Meal> getMeal(@NotNull @NotBlank String mealSortKey, Principal principal) {
        log.info("Getting Meal. mealSortKey: {}, uid:{}", mealSortKey, principal.getName());
        return mealService.getMeal(principal.getName(), mealSortKey);
    }

    @Get
    public List<Meal> listMeals(Principal principal) {
        log.info("Getting all meals for uid: {}", principal.getName());
        return mealService.getListOfMeals(principal.getName());
    }
}
