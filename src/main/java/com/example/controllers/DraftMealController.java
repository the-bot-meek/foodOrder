package com.example.controllers;

import com.example.models.DraftMeal;
import com.example.services.MealService;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.rules.SecurityRule;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

@Controller("meal/draft")
@Secured(SecurityRule.IS_AUTHENTICATED)
public class DraftMealController {
    private final Logger log = LoggerFactory.getLogger(DraftMealController.class);
    private final MealService mealService;

    DraftMealController(MealService mealService) {
        this.mealService = mealService;
    }

    @Get("{mealSortKey}")
    public Optional<DraftMeal> getDraftMeal(@NotNull @NotBlank String mealSortKey, Authentication authentication) {
        log.info("Getting Meal. mealSortKey: {}, uid:{}", mealSortKey, authentication.getName());
        return mealService.getDraftMeal(authentication.getName(), mealSortKey);
    }
}
