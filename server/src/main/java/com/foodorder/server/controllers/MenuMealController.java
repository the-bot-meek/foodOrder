package com.foodorder.server.controllers;

import com.foodorder.server.models.meal.Meal;
import com.foodorder.server.repository.MealRepository;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import jakarta.validation.constraints.NotNull;

import java.util.Optional;

@Controller("/menu")
@Secured(SecurityRule.IS_ANONYMOUS)
public class MenuMealController {
    private final MealRepository mealRepository;
    MenuMealController(MealRepository mealRepository) {
        this.mealRepository = mealRepository;
    }

    @Get("{menuId}/meal/{mealId}")
    public Optional<Meal> getMeal(@NotNull String menuId, @NotNull String mealId) {
        return this.mealRepository.getMealByMenuNameAndMealId(menuId, mealId);
    }
}
