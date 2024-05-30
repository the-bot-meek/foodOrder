package com.example.converters;

import com.example.exceptions.MealRequestConverterException;
import com.example.dto.request.CreateMealRequest;
import com.example.dto.request.CreatePrivateMealRequest;
import com.example.models.meal.DraftMeal;
import com.example.models.meal.Meal;
import com.example.models.meal.MealConfig;
import com.example.models.meal.PrivateMealConfig;
import com.example.services.LocationService;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

@Singleton
public class CreateMealRequestConverter {
    private Logger log = LoggerFactory.getLogger(CreateMealRequestConverter.class);
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
            meal = new DraftMeal(id, uid, createMealRequest.getName(), createMealRequest.getDateOfMeal(), createMealRequest.getLocation(), createMealRequest.getVenueName());
        } else {
            meal = new Meal(id, uid, createMealRequest.getName(), createMealRequest.getDateOfMeal(), createMealRequest.getLocation(), createMealRequest.getVenueName());
        }
        meal.setMealConfig(buildMealConfig(createMealRequest));
        return meal;
    }

    private MealConfig buildMealConfig(CreateMealRequest createMealRequest) {
        if (createMealRequest.getClass() == CreatePrivateMealRequest.class) {
            return new PrivateMealConfig(((CreatePrivateMealRequest) createMealRequest).getParticipant());
        } else {
            return new MealConfig();
        }
    }
}
