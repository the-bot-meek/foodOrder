package com.thebotmeek.foodorder.server.integration

import com.foodorder.server.client.MealClient
import com.foodorder.server.client.VenueClient
import com.foodorder.server.client.VenueMealClient
import com.foodorder.server.controllers.MealController
import com.foodorder.server.request.CreateMealRequest
import com.foodorder.server.models.meal.Meal
import com.foodorder.server.models.meal.MealConfig
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
        CreateMealRequest createMealRequest = new CreateMealRequest(name: "name", dateOfMeal: Instant.ofEpochSecond(1711405066), location: "London", venueName: "MacD")

        when:
        Meal meal = mealController.handleCreateMealRequest(createMealRequest, authentication)
        Optional<Meal> mealFromVenueId = venueMealClient.getMeal(meal.getVenueName(), meal.getId())

        then:
        assert mealFromVenueId.isPresent()
        assert mealFromVenueId.get() == meal
        assert mealController != null
    }
}
