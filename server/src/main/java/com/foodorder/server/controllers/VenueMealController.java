package com.foodorder.server.controllers;

import com.foodorder.server.models.meal.Meal;
import com.foodorder.server.services.MealService;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import jakarta.validation.constraints.NotNull;

import java.util.Optional;

@Controller("/venue")
@Secured(SecurityRule.IS_ANONYMOUS)
public class VenueMealController {
    private final MealService mealService;
    VenueMealController(MealService mealService) {
        this.mealService = mealService;
    }

    @Get("{venueId}/meal/{mealId}")
    public Optional<Meal> getMeal(@NotNull String venueId, @NotNull String mealId) {
        return this.mealService.getMealByVenueNameAndMealId(venueId, mealId);
    }
}
