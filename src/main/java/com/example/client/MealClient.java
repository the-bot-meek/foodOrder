package com.example.client;

import com.example.dto.request.CreateMealRequest;
import com.example.models.meal.DraftMeal;
import com.example.models.meal.Meal;
import com.example.models.Order;
import io.micronaut.http.annotation.*;
import io.micronaut.http.client.annotation.Client;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Client("/meal")
public interface MealClient {
    @Get()
    List<Meal> listAllMealsForUser();

    @Get("/draft")
    List<DraftMeal> listAllDraftMealsForUser();

    @Put()
    Meal addMeal(@Valid @Body CreateMealRequest createMealRequest);

    @Get("{mealSortKey}")
    Meal fetchMeal(String mealSortKey);

    @Get("draft/{mealSortKey}")
    DraftMeal fetchDraftMeal(String mealSortKey);

    @Get("/{mealId}/orders")
    List<Order> listAllOrdersForMeal(String mealId);

    @Get("/{mealDateTimeStamp}/{id}")
    Optional<Meal> getMeal(@NotNull long mealDateTimeStamp, @NotNull String id);

    @Delete
    void deleteMeal(
            @NotNull @QueryValue Instant mealDate,
            @NotNull @NotEmpty @QueryValue String id
    );

    @Delete("draft")
    void deleteDraftMeal(
        @NotNull @QueryValue Instant mealDate,
        @NotNull @NotEmpty @QueryValue String mealId
    );
}
