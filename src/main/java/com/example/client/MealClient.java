package com.example.client;

import com.example.dto.request.CreateMealRequest;
import com.example.models.Meal;
import com.example.models.Order;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Put;
import io.micronaut.http.client.annotation.Client;

import java.util.List;

@Client("/meal")
public interface MealClient {
    @Get()
    List<Meal> listAllMealsForUser();

    @Put()
    Meal addMeal(@Body CreateMealRequest createMealRequest);

    @Get("{mealSortKey}")
    Meal fetchMeal(String mealSortKey);

    @Get("/{mealId}/orders")
    List<Order> listAllOrdersForMeal(String mealId);
}
