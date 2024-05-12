package com.example.Integration

import com.example.client.MealClient
import com.example.dto.request.CreateMealRequest
import com.example.models.Meal
import io.micronaut.context.ApplicationContext
import io.micronaut.runtime.server.EmbeddedServer
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.IgnoreIf
import spock.lang.Specification

import java.time.Instant


@MicronautTest(environments = ["integration"])
@IgnoreIf({System.getenv("requireIntegrationTests") != 'true'})
class MealIntegrationSpec extends Specification {
    @Inject
    MealClient mealClient
    def "Add meal"() {
        given:
        CreateMealRequest createMealRequest = new CreateMealRequest("name", Instant.ofEpochSecond(1711405066), "London", "MacD")

        when:
        Meal meal = mealClient.addMeal(createMealRequest)

        then:
        assert meal.getMealDate() == Instant.ofEpochSecond(1711405066)
        assert meal.getUid() == "steven"
        assert meal.getName() == "name"
        assert meal.getVenueName() == "MacD"
        assert meal.getLocation() == "London"
        assert meal.getId() != null
    }

    def "Get Meal"() {
        given:
        CreateMealRequest createMealRequest = new CreateMealRequest("name", Instant.ofEpochSecond(1711405066), "London", "MacD")

        when:
        Meal createMealResp = mealClient.addMeal(createMealRequest)
        Meal meal = mealClient.fetchMeal(createMealResp.sortKey)

        then:
        meal == createMealResp
    }

    def "List all meals for current user"() {
        given:
        CreateMealRequest createMealRequest = new CreateMealRequest("name", Instant.ofEpochSecond(1711405066), "London", "MacD")

        when:
        mealClient.addMeal(createMealRequest)
        List<Meal> mealList = mealClient.listAllMealsForUser()
        then:
        assert !mealList.isEmpty()
        assert mealList.every {Meal meal -> meal.uid == "steven"}
    }
}
