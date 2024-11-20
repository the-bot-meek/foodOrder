package com.thebotmeek.foodorder.server.converters

import com.foodorder.server.converters.CreateMealRequestConverter
import com.foodorder.server.request.CreateMealRequest
import com.foodorder.server.models.meal.DraftMeal
import com.foodorder.server.models.meal.Meal
import com.foodorder.server.models.meal.MealConfig
import com.foodorder.server.services.LocationService
import spock.lang.Specification

import java.time.Instant

class CreateMealRequestConverterTest extends Specification {
    def "ConvertCreateMealRequestToMeal"() {
        given:
        LocationService locationService = Mock(LocationService)
        locationService.listLocation() >> ["London"]
        String uid = "684d5aa7-2275-469e-8478-c8e35d50a5f9"
        String mealId = "b9a36f0f-1a01-4d9a-88ee-097abe1b29cc"
        CreateMealRequestConverter createMealRequestConverter = new CreateMealRequestConverter(locationService)

        CreateMealRequest createMealRequest = new CreateMealRequest(name: "name", dateOfMeal: Instant.ofEpochSecond(1711405066), location: "London", venueName: "MacD", mealConfig: new MealConfig())

        when:
        Meal meal = createMealRequestConverter.convertCreateMealRequestToNewMeal(createMealRequest, uid, mealId)

        then:
        assert meal.getId() == mealId
        assert meal.getSortKey() == "2024-03-25T22:17:46Z_b9a36f0f-1a01-4d9a-88ee-097abe1b29cc"
        assert meal.getUid() == uid
        assert meal.getMealDate() == createMealRequest.getDateOfMeal()
        assert meal.getLocation() == createMealRequest.getLocation()
        assert meal.getVenueName() == createMealRequest.getVenueName()
        assert meal.getName() == createMealRequest.getName()
        assert meal.getPrimaryKey() == "Meal_684d5aa7-2275-469e-8478-c8e35d50a5f9"
        assert meal instanceof Meal
    }

    def "ConvertCreateMealRequestToMeal DraftMeal"() {
        given:
        LocationService locationService = Mock(LocationService)
        locationService.listLocation() >> ["London"]
        CreateMealRequestConverter createMealRequestConverter = new CreateMealRequestConverter(locationService)
        String uid = "684d5aa7-2275-469e-8478-c8e35d50a5f9"
        String mealId = "b9a36f0f-1a01-4d9a-88ee-097abe1b29cc"

        CreateMealRequest createMealRequest = new CreateMealRequest(name:  "name", dateOfMeal:  Instant.ofEpochSecond(1711405066), location:  "London", venueName:  "MacD", mealConfig: new MealConfig(draft: true))

        when:
        Meal meal = createMealRequestConverter.convertCreateMealRequestToNewMeal(createMealRequest, uid, mealId)

        then:
        assert meal.getId() == mealId
        assert meal.getSortKey() == "2024-03-25T22:17:46Z_b9a36f0f-1a01-4d9a-88ee-097abe1b29cc"
        assert meal.getUid() == uid
        assert meal.getMealDate() == createMealRequest.getDateOfMeal()
        assert meal.getLocation() == createMealRequest.getLocation()
        assert meal.getVenueName() == createMealRequest.getVenueName()
        assert meal.getName() == createMealRequest.getName()
        assert meal.getPrimaryKey() == "DraftMeal_684d5aa7-2275-469e-8478-c8e35d50a5f9"
        assert meal instanceof DraftMeal
    }
}
