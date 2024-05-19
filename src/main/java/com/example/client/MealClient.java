package com.example.client;

import com.example.dto.request.CreateMealRequest;
import com.example.dto.request.DeleteMealRequest;
import com.example.models.Meal.AbstractMeal;
import com.example.models.Meal.DraftMeal;
import com.example.models.Meal.Meal;
import com.example.models.Order.Order;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Delete;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Put;
import io.micronaut.http.client.annotation.Client;
import jakarta.validation.Valid;

import java.util.List;

@Client("/meal")
public interface MealClient {
    @Get()
    List<Meal> listAllMealsForUser();

    @Put()
    AbstractMeal addMeal(@Body CreateMealRequest createMealRequest);

    @Get("{mealSortKey}")
    Meal fetchMeal(String mealSortKey);

    @Get("draft/{mealSortKey}")
    DraftMeal fetchDraftMeal(String mealSortKey);

    @Get("/{mealId}/orders")
    List<Order> listAllOrdersForMeal(String mealId);

    @Delete()
    void deleteMeal(@Valid @Body DeleteMealRequest deleteMealRequest);
}
