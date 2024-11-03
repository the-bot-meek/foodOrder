package com.example.client;

import com.example.models.meal.Meal;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.client.annotation.Client;

import java.util.Optional;

@Client("/venue")
public interface VenueMealClient {
    @Get("{venueId}/meal/{mealId}")
    Optional<Meal> getMeal(String venueId, String mealId);
}
