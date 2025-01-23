package com.thebotmeek.foodorder.server.controllers

import com.foodorder.server.controllers.MealController
import com.foodorder.server.converters.CreateMealRequestConverter
import com.foodorder.server.request.CreateMealRequest
import com.foodorder.server.models.meal.Meal
import com.foodorder.server.models.meal.MealConfig
import com.foodorder.server.repository.LocationRepository
import com.foodorder.server.repository.MealRepository
import io.micronaut.http.exceptions.HttpStatusException
import io.micronaut.security.authentication.Authentication
import spock.lang.Specification
import java.time.Instant

class MealControllerSpec extends Specification {
    LocationRepository locationService
    MealRepository mealService
    MealController mealController
    CreateMealRequestConverter createMealRequestConverter
    Authentication authentication

    def "setup"() {
        locationService = new LocationRepository()
        mealService = Mock(MealRepository)
        createMealRequestConverter = new CreateMealRequestConverter(locationService)
        mealController = new MealController(mealService, null, createMealRequestConverter)
        authentication = Mock(Authentication)
        authentication.getName() >> "principal_name"
    }
    
    def "AddMeal"() {
        given:
        CreateMealRequest createMealRequest = new CreateMealRequest(name:  "name", dateOfMeal:  Instant.ofEpochSecond(1711405066), location:  "London", venueName:  "MacD", mealConfig: new MealConfig())

        when:
        mealController.addMeal(createMealRequest, authentication)

        then:
        1 * mealService.saveMeal(_) >> { Meal meal ->
            meal.with {
                // ToDo: update this test
                assert it.getName() == "name"
                assert it.getMealDate() == Instant.ofEpochSecond(1711405066)
                assert it.getUid() == "principal_name"
                assert it.getSortKey().startsWith("2024-03-25T22:17:46Z_")
                assert it.getPrimaryKey() == "Meal_principal_name"
            }
        }
    }

    def "addMeal with invalid location"() {
        CreateMealRequest createMealRequest = new CreateMealRequest(name:  "name", dateOfMeal:  Instant.ofEpochSecond(1711405066), location:  "idk", venueName:  "MacD")

        when:
        mealController.addMeal(createMealRequest, authentication)

        then:
        thrown(HttpStatusException)
    }

    def "GetMeal"() {
        given:
        String mealSortKey = "2024-03-25T22:17:46Z_797b001f-de8f-47ed-833a-d84e61c73fe7"
        1 * mealService.getMeal("principal_name", mealSortKey) >> {
            Optional.of(
                    new Meal(id: "797b001f-de8f-47ed-833a-d84e61c73fe7", name: "name", mealDate: Instant.ofEpochSecond(1711405066), uid: "principal_name", location: "London", venueName: "MacD")
            )
        }

        when:
        Optional<Meal> meal = mealController.getMeal(mealSortKey, authentication)

        then:
        assert meal.isPresent()
        assert meal.get() == new Meal(id: "797b001f-de8f-47ed-833a-d84e61c73fe7", name: "name", mealDate: Instant.ofEpochSecond(1711405066), uid: "principal_name", location: "London", venueName: "MacD")
    }

    def "ListMeals"() {
        given:
        mealService.getListOfMeals("principal_name") >> [
                new Meal(
                        id: "797b001f-de8f-47ed-833a-d84e61c73fe7",
                        name: "name", mealDate: Instant.ofEpochSecond(1711405066),
                        uid: "principal_name",
                        location: "London",
                        venueName: "MacD"
                )
        ]

        when:
        List<Meal> mealList = mealController.listMeals(authentication)

        then:
        assert mealList == [
                new Meal(
                        id: "797b001f-de8f-47ed-833a-d84e61c73fe7",
                        name: "name", mealDate:
                        Instant.ofEpochSecond(1711405066),
                        uid: "principal_name",
                        location: "London",
                        venueName: "MacD"
                )
        ]
    }
}
