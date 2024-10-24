package com.example.Integration

import com.example.client.MealClient
import com.example.client.VenueClient
import com.example.client.VenueMealClient
import com.example.controllers.MealController
import com.example.dto.request.CreateMealRequest
import com.example.models.MenuItem
import com.example.models.meal.Meal
import com.example.models.meal.MealConfig
import io.micronaut.security.authentication.Authentication
import io.micronaut.security.authentication.ServerAuthentication
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.IgnoreIf
import spock.lang.Specification

import java.time.Instant

@MicronautTest(environments = ["mock_auth"])
@IgnoreIf({System.getenv("requireIntegrationTests") != 'true'})
class VenueMealControllerSpec extends Specification {
    @Inject
    VenueMealClient venueMealClient

    @Inject
    MealClient mealClient

    @Inject
    VenueClient venueClient

    @Inject
    MealController mealController

    def "test getting Meal from venueName and MealId"() {
        given:
        Authentication authentication = new ServerAuthentication("steven", null, null)
        CreateMealRequest createMealRequest = new CreateMealRequest(name: "name", dateOfMeal: Instant.ofEpochSecond(1711405066), location: "London", venueName: "MacD", mealConfig: new MealConfig())

        when:
        Meal meal = mealController.addMeal(createMealRequest, authentication).body()
        Optional<Meal> mealFromVenueId = venueMealClient.getMeal(meal.getVenueName(), meal.getId())

        then:
        assert mealFromVenueId.isPresent()
        assert mealFromVenueId.get() == meal
        assert mealController != null
    }
}
