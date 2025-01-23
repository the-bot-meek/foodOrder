package com.thebotmeek.foodorder.server.controllers

import com.foodorder.server.controllers.DraftMealController
import com.foodorder.server.models.meal.DraftMeal
import com.foodorder.server.repository.MealRepository
import io.micronaut.security.authentication.Authentication
import spock.lang.Specification

import java.time.Instant

class DraftMealControllerTest extends Specification {
    Authentication authentication
    MealRepository mealService
    DraftMealController draftMealController
    String uid
    String mealId
    String mealSortKey

    def "setup"() {
        authentication = Mock(Authentication)
        mealService = Mock(MealRepository)
        uid =  "user id"
        authentication.getName() >> uid
        draftMealController = new DraftMealController(mealService)
        mealId = "684d5aa7-2275-469e-8478-c8e35d50a5f9"
        mealSortKey = "DraftMeal_${mealId}"
    }

    def "Get draft meal"() {
        given:
        DraftMeal savedDraftMeal = new DraftMeal(
                id: mealId,
                name: "name",
                mealDate: Instant.ofEpochSecond(1711405066),
                uid: "principal_name",
                location: "London",
                venueName: "MacD"
        )

        when:
        DraftMeal draftMeal = draftMealController.getDraftMeal(mealSortKey, authentication).get()

        then:
        1 * mealService.getDraftMeal(uid, mealSortKey) >> Optional.of(savedDraftMeal)

        assert draftMeal == savedDraftMeal
    }

    def "listAllDraftMealsForUser"() {
        when:
        draftMealController.listAllDraftMealsForUser(authentication)

        then:
        1 * mealService.getListOfDraftMeals(uid)
    }

    def "deleteDraftMeal"() {
        given:
        Instant mealDate = Instant.ofEpochSecond(1723572330)

        when:
        draftMealController.deleteDraftMeal(authentication, mealDate, mealId)

        then:
        mealService.deleteDraftMeal(uid, mealDate, mealId)
    }
}
