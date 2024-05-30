package com.example.controllers


import com.example.models.meal.DraftMeal
import com.example.services.IDynamoDBFacadeService
import com.example.services.LocationService
import com.example.services.MealService
import io.micronaut.security.authentication.Authentication
import spock.lang.Specification

import java.time.Instant

class DraftMealControllerTest extends Specification {
    def "Get draft meal"() {
        given:
        String uid =  "user id"
        Authentication authentication = Mock(Authentication)
        authentication.getName() >> uid
        String mealId = "684d5aa7-2275-469e-8478-c8e35d50a5f9"
        String mealSortKey = "DraftMeal_${mealId}"

        IDynamoDBFacadeService dynamoDBFacadeService = Mock(IDynamoDBFacadeService)
        DraftMeal savedDraftMeal = new DraftMeal(
                id: mealId,
                name: "name",
                mealDate: Instant.ofEpochSecond(1711405066),
                uid: "principal_name",
                location: "London",
                venueName: "MacD"
        )
        dynamoDBFacadeService.load(DraftMeal.class, uid, mealSortKey) >> Optional.of(savedDraftMeal)
        LocationService locationService = Mock(LocationService)
        locationService.listLocation() >> ["London"]
        MealService mealService = new MealService(dynamoDBFacadeService)
        DraftMealController draftMealController = new DraftMealController(mealService)

        when:
        DraftMeal draftMeal = draftMealController.getDraftMeal(mealSortKey, authentication).get()

        then:
        assert draftMeal == savedDraftMeal
    }
}
