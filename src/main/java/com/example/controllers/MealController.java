package com.example.controllers;

import com.example.dto.CreateMealRequest;
import com.example.models.Meal;
import com.example.services.MealService;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.Optional;


@Controller("meal")
public class MealController {
    private final MealService mealService;

    MealController(MealService mealService) {
        this.mealService = mealService;
    }
    @Put(consumes = MediaType.ALL)
    public HttpResponse<Meal> addMeal(@Body CreateMealRequest createMealRequest) {
        return HttpResponse.ok(mealService.newMeal(createMealRequest, "101"));
    }

    @Get("{mealSortKey}")
    public Optional<Meal> updateMeal(@NotNull @NotBlank String mealSortKey) {
        return mealService.getMeal("101", mealSortKey);
    }

    @Get
    public List<Meal> listMeals() {
        return mealService.getListOfMeals("101");
    }
}
