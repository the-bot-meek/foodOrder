package com.thebotmeek.foodorder.server.integration

import com.foodorder.server.client.MealClient
import com.foodorder.server.request.CreateMealRequest
import com.foodorder.server.models.meal.DraftMeal
import com.foodorder.server.models.meal.Meal
import com.foodorder.server.models.meal.MealConfig
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
        CreateMealRequest createMealRequest = new CreateMealRequest(name: "name", dateOfMeal: Instant.ofEpochSecond(1711405066), location: "London", menuName: "MacD", mealConfig: new MealConfig(draft: true))

        when:
        Meal draftMealSaved = mealClient.addMeal(createMealRequest)
        DraftMeal draftMeal = mealClient.fetchDraftMeal(draftMealSaved.getSortKey())

        then:
        assert draftMeal == draftMealSaved
    }

    def "Ensure draft meal is deleted"() {
        given:
        CreateMealRequest createMealRequest = new CreateMealRequest(name: "name", dateOfMeal: Instant.ofEpochSecond(1711405066), location: "London", menuName: "MacD", mealConfig: new MealConfig(draft: true))

        when:
        Meal meal = mealClient.addMeal(createMealRequest)
        mealClient.deleteDraftMeal(
                meal.getMealDate(),
                meal.getId()
        )
        Meal mealAfterDelete = mealClient.fetchDraftMeal(meal.getSortKey())

        then:
        assert mealAfterDelete == null
    }

    def "List all draft meals for current user"() {
        given:
        CreateMealRequest createMealRequest = new CreateMealRequest(name:  "name", dateOfMeal:  Instant.ofEpochSecond(1711405066), location:  "London", menuName:  "MacD", mealConfig: new MealConfig(draft: true))

        when:
        Meal draftMeal = mealClient.addMeal(createMealRequest)
        List<DraftMeal> mealList = mealClient.listAllDraftMealsForUser()
        then:
        assert !mealList.isEmpty()
        assert mealList.every {Meal meal -> meal.uid == "steven"}
        assert mealList.find {it.getId() == draftMeal.getId()} == draftMeal
    }
}
