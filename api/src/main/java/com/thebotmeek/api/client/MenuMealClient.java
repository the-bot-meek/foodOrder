package com.thebotmeek.api.client;

import com.foodorder.server.models.meal.Meal;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.client.annotation.Client;

import java.util.Optional;

@Client("/menu")
public interface MenuMealClient {
    @Get("{menuId}/meal/{mealId}")
    Optional<Meal> getMeal(String menuId, String mealId);
}
