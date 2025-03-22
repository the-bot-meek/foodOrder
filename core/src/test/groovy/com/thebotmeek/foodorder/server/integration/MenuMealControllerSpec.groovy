package com.thebotmeek.foodorder.server.integration

import com.foodorder.server.client.MealClient
import com.foodorder.server.client.MenuClient
import com.foodorder.server.client.MenuMealClient
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
class MenuMealControllerSpec extends Specification {
    @Inject
    MenuMealClient menuMealClient

    @Inject
    MealClient mealClient

    @Inject
    MenuClient menuClient

    @Inject
    MealController mealController

    def "test getting Meal from menuName and MealId"() {
        given:
        Authentication authentication = new ServerAuthentication("steven", null, null)
        CreateMealRequest createMealRequest = new CreateMealRequest(name: "name", dateOfMeal: Instant.ofEpochSecond(1711405066), location: "London", menuName: "MacD", mealConfig: new MealConfig())

        when:
        Meal meal = mealController.addMeal(createMealRequest, authentication).body()
        Optional<Meal> mealFromMenuId = menuMealClient.getMeal(meal.getMenuName(), meal.getId())

        then:
        assert mealFromMenuId.isPresent()
        assert mealFromMenuId.get() == meal
        assert mealController != null
    }
}
