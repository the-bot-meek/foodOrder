package com.foodorder.server.controllers;

import com.foodorder.models.models.meal.DraftMeal;
import com.foodorder.server.repository.MealRepository;
import io.micronaut.http.annotation.*;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.rules.SecurityRule;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Controller("meal/draft")
@Secured(SecurityRule.IS_AUTHENTICATED)
public class DraftMealController {
    private final Logger log = LoggerFactory.getLogger(DraftMealController.class);
    private final MealRepository mealRepository;

    DraftMealController(MealRepository mealRepository) {
        this.mealRepository = mealRepository;
    }

    @Get("{mealSortKey}")
    public Optional<DraftMeal> getDraftMeal(@NotNull @NotBlank String mealSortKey, Authentication authentication) {
        log.info("Getting Meal. mealSortKey: {}, uid:{}", mealSortKey, authentication.getName());
        return mealRepository.getDraftMeal(authentication.getName(), mealSortKey);
    }

    @Get
    public List<DraftMeal> listAllDraftMealsForUser(Authentication authentication) {
        log.info("Listing all meals for uid: {}", authentication.getName());
        return mealRepository.getListOfDraftMeals(authentication.getName());
    }

    @Delete
    public void deleteDraftMeal(
            @NotNull Authentication authentication,
            @NotNull @QueryValue Instant mealDate,
            @NotNull @NotEmpty @QueryValue String mealId
    ) {
        log.info("Deleting draft meal {}", mealId);
        mealRepository.deleteDraftMeal(authentication.getName(), mealDate, mealId);
    }
}
