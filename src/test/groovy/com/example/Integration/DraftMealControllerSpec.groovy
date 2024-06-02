package com.example.Integration

import com.example.client.MealClient
import com.example.dto.request.CreateMealRequest
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
        CreateMealRequest createMealRequest = new CreateMealRequest(name: "name", dateOfMeal: Instant.ofEpochSecond(1711405066), location: "London", venueName: "MacD", draft: true, mealConfig: new MealConfig())

        when:
        Meal draftMealSaved = mealClient.addMeal(createMealRequest)
        DraftMeal draftMeal = mealClient.fetchDraftMeal(draftMealSaved.getSortKey())

        then:
        assert draftMeal == draftMealSaved
    }
}
