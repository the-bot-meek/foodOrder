package com.example.services

import com.example.dto.request.CreateMealRequest
import com.example.models.Meal.DraftMeal
import com.example.models.Meal.Meal
import spock.lang.Specification

import java.time.Instant

class MealServiceTest extends Specification {
    def "ConvertCreateMealRequestToMeal"() {
        given:
        LocationService locationService = Mock(LocationService)
        locationService.listLocation() >> ["London"]
        MealService mealService = new MealService(null, locationService)
        String uid = "684d5aa7-2275-469e-8478-c8e35d50a5f9"
        String mealId = "b9a36f0f-1a01-4d9a-88ee-097abe1b29cc"

        CreateMealRequest createMealRequest = new CreateMealRequest("name", Instant.ofEpochSecond(1711405066), "London", "MacD", false)

        when:
        Meal meal = mealService.convertCreateMealRequestToNewMeal(createMealRequest, uid, mealId)

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
        MealService mealService = new MealService(null, locationService)
        String uid = "684d5aa7-2275-469e-8478-c8e35d50a5f9"
        String mealId = "b9a36f0f-1a01-4d9a-88ee-097abe1b29cc"

        CreateMealRequest createMealRequest = new CreateMealRequest("name", Instant.ofEpochSecond(1711405066), "London", "MacD", true)

        when:
        DraftMeal meal = mealService.convertCreateMealRequestToNewMeal(createMealRequest, uid, mealId)

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
