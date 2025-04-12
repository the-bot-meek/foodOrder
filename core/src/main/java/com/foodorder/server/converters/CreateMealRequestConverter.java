package com.foodorder.server.converters;

import com.foodorder.server.exceptions.MealRequestConverterException;
import com.foodorder.server.models.meal.MealConfig;
import com.foodorder.server.request.CreateMealRequest;
import com.foodorder.server.models.meal.DraftMeal;
import com.foodorder.server.models.meal.Meal;
import com.foodorder.server.repository.LocationRepository;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

@Singleton
public class CreateMealRequestConverter {
    private final Logger log = LoggerFactory.getLogger(CreateMealRequestConverter.class);
    private final LocationRepository locationRepository;
    private final CreateMealConfigConverter createMealConfigConverter;

    public CreateMealRequestConverter(LocationRepository locationRepository, CreateMealConfigConverter createMealConfigConverter) {
        this.locationRepository = locationRepository;
        this.createMealConfigConverter = createMealConfigConverter;
    }

    public Meal convertCreateMealRequestToNewMeal(CreateMealRequest createMealRequest, String uid) throws MealRequestConverterException {
        final String id = UUID.randomUUID().toString();
        return convertCreateMealRequestToNewMeal(createMealRequest, uid, id);
    }

    public Meal convertCreateMealRequestToNewMeal(CreateMealRequest createMealRequest, String uid, String id) throws MealRequestConverterException {
        log.trace("Converting CreateMealRequest into Meal");
        if (!locationRepository.listLocation().contains(createMealRequest.getLocation())) {
            log.trace("Invalid Location Invalid location: {}", createMealRequest.getLocation());
            throw new MealRequestConverterException("Invalid Location");
        }
        Meal meal;
        MealConfig mealConfig = createMealConfigConverter.convertCreateMealConfigToNewMealConfig(
                createMealRequest.getCreateMealConfig()
        );
        if (createMealRequest.getCreateMealConfig().isDraft()) {
            meal = new DraftMeal(id, uid, createMealRequest.getName(), createMealRequest.getDateOfMeal(), createMealRequest.getLocation(), createMealRequest.getMenuName(), mealConfig);
        } else {
            meal = new Meal(id, uid, createMealRequest.getName(), createMealRequest.getDateOfMeal(), createMealRequest.getLocation(), createMealRequest.getMenuName(), mealConfig);
        }
        return meal;
    }
}
