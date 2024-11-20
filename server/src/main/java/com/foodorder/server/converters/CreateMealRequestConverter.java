package com.foodorder.server.converters;

import com.foodorder.server.exceptions.MealRequestConverterException;
import com.foodorder.server.request.CreateMealRequest;
import com.foodorder.server.models.meal.DraftMeal;
import com.foodorder.server.models.meal.Meal;
import com.foodorder.server.services.LocationService;
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
        if (createMealRequest.getMealConfig().getDraft()) {
            meal = new DraftMeal(id, uid, createMealRequest.getName(), createMealRequest.getDateOfMeal(), createMealRequest.getLocation(), createMealRequest.getVenueName(), createMealRequest.getMealConfig());
        } else {
            meal = new Meal(id, uid, createMealRequest.getName(), createMealRequest.getDateOfMeal(), createMealRequest.getLocation(), createMealRequest.getVenueName(), createMealRequest.getMealConfig());
        }
        return meal;
    }
}
