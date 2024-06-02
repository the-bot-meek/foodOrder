package com.example.converters;

import com.example.exceptions.MealRequestConverterException;
import com.example.dto.request.CreateMealRequest;
import com.example.models.meal.DraftMeal;
import com.example.models.meal.Meal;
import com.example.services.LocationService;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

@Singleton
public class CreateMealRequestConverter {
    private final Logger log = LoggerFactory.getLogger(CreateMealRequestConverter.class);
    private final LocationService locationService;
    public CreateMealRequestConverter(LocationService locationService) {
        this.locationService = locationService;
    }
    public Meal convertCreateMealRequestToNewMeal(CreateMealRequest createMealRequest, String uid) throws MealRequestConverterException {
        final String id = UUID.randomUUID().toString();
        return convertCreateMealRequestToNewMeal(createMealRequest, uid, id);
    }

    public Meal convertCreateMealRequestToNewMeal(CreateMealRequest createMealRequest, String uid, String id) throws MealRequestConverterException {
        log.trace("Converting CreateMealRequest into Meal");
        if (!locationService.listLocation().contains(createMealRequest.getLocation())) {
            log.trace("Invalid Location Invalid location: {}", createMealRequest.getLocation());
            throw new MealRequestConverterException("Invalid Location");
        }
        Meal meal;
        if (createMealRequest.getDraft()) {
            meal = new DraftMeal(id, uid, createMealRequest.getName(), createMealRequest.getDateOfMeal(), createMealRequest.getLocation(), createMealRequest.getVenueName(), createMealRequest.getMealConfig());
        } else {
            meal = new Meal(id, uid, createMealRequest.getName(), createMealRequest.getDateOfMeal(), createMealRequest.getLocation(), createMealRequest.getVenueName(), createMealRequest.getMealConfig());
        }
        return meal;
    }
}
