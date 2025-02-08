package com.foodorder.server.client;

import com.foodorder.models.models.meal.Meal;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.client.annotation.Client;

import java.util.Optional;

@Client("/venue")
public interface VenueMealClient {
    @Get("{venueId}/meal/{mealId}")
    Optional<Meal> getMeal(String venueId, String mealId);
}
