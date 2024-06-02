package com.example.Integration

import com.example.client.MealClient
import com.example.dto.request.CreateMealRequest
import com.example.dto.request.DeleteMealRequest
import com.example.models.Order
import com.example.models.meal.DraftMeal
import com.example.models.meal.Meal
import com.example.models.meal.MealConfig
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.IgnoreIf
import spock.lang.Specification

import java.time.Instant

@MicronautTest(environments = ["mock_auth"])
@IgnoreIf({System.getenv("requireIntegrationTests") != 'true'})
class DraftMealControllerSpec extends Specification {
    @Inject
    MealClient mealClient
    def "Get Draft Meal"() {
        given:
        CreateMealRequest createMealRequest = new CreateMealRequest(name: "name", dateOfMeal: Instant.ofEpochSecond(1711405066), location: "London", venueName: "MacD", mealConfig: new MealConfig(draft: true))

        when:
        Meal draftMealSaved = mealClient.addMeal(createMealRequest)
        DraftMeal draftMeal = mealClient.fetchDraftMeal(draftMealSaved.getSortKey())

        then:
        assert draftMeal == draftMealSaved
    }

    def "Ensure draft meal is deleted"() {
        given:
        CreateMealRequest createMealRequest = new CreateMealRequest(name:  "name", dateOfMeal:  Instant.ofEpochSecond(1711405066), location:  "London", venueName:  "MacD", mealConfig: new MealConfig(draft: true))

        when:
        Meal meal = mealClient.addMeal(createMealRequest)
        DeleteMealRequest deleteMealRequest = new DeleteMealRequest(
                meal.getUid(),
                meal.getMealDate(),
                meal.getId()
        )
        mealClient.deleteDraftMeal(deleteMealRequest)
        Meal mealAfterDelete = mealClient.fetchDraftMeal(meal.getSortKey())

        then:
        assert mealAfterDelete == null
    }

    def "List all draft meals for current user"() {
        given:
        CreateMealRequest createMealRequest = new CreateMealRequest(name:  "name", dateOfMeal:  Instant.ofEpochSecond(1711405066), location:  "London", venueName:  "MacD", mealConfig: new MealConfig(draft: true))

        when:
        mealClient.addMeal(createMealRequest)
        List<DraftMeal> mealList = mealClient.listAllDraftMealsForUser()
        then:
        assert !mealList.isEmpty()
        assert mealList.every {Meal meal -> meal.uid == "steven"}
    }
}
