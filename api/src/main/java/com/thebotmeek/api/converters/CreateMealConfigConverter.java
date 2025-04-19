package com.thebotmeek.api.converters;

import com.foodorder.server.models.meal.MealConfig;
import com.foodorder.server.models.meal.PrivateMealConfig;
import com.thebotmeek.api.request.CreateMealConfig;
import jakarta.inject.Singleton;

@Singleton
public class CreateMealConfigConverter {
    private final CreatePrivateMealConfigConverter createPrivateMealConfigConverter;

    public CreateMealConfigConverter(CreatePrivateMealConfigConverter createPrivateMealConfigConverter) {
        this.createPrivateMealConfigConverter = createPrivateMealConfigConverter;
    }

    MealConfig convertCreateMealConfigToNewMealConfig(CreateMealConfig createMealConfig) {
        PrivateMealConfig privateMealConfig = createPrivateMealConfigConverter.convertCreatePrivateMealConfigToPrivateMealConfig(
                createMealConfig.getCreatePrivateMealConfig()
        );
        return new MealConfig(
                privateMealConfig,
                createMealConfig.isDraft()
        );
    }
}
