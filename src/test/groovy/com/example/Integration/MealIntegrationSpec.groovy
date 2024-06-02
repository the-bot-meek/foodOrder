package com.example.Integration

import com.example.client.MealClient
import com.example.client.OrderClient
import com.example.dto.request.CreateMealRequest
import com.example.dto.request.CreateOrderRequest

import com.example.dto.request.DeleteMealRequest
import com.example.models.meal.DraftMeal
import com.example.models.meal.Meal
import com.example.models.meal.MealConfig
import com.example.models.meal.PrivateMealConfig
import com.example.models.MenuItem
import com.example.models.Order
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.IgnoreIf
import spock.lang.Specification

import java.time.Instant


@MicronautTest(environments = ["mock_auth"])
@IgnoreIf({System.getenv("requireIntegrationTests") != 'true'})
class MealIntegrationSpec extends Specification {
    @Inject
    MealClient mealClient
    def "Add meal"() {
        given:
        CreateMealRequest createMealRequest = new CreateMealRequest(name: "name", dateOfMeal: Instant.ofEpochSecond(1711405066), location: "London", venueName: "MacD", mealConfig: new MealConfig())

        when:
        Meal meal = mealClient.addMeal(createMealRequest)

        then:
        assert meal.getMealDate() == Instant.ofEpochSecond(1711405066)
        assert meal.getUid() == "steven"
        assert meal.getName() == "name"
        assert meal.getVenueName() == "MacD"
        assert meal.getLocation() == "London"
        assert meal.getId() != null
        assert meal.getClass() == Meal
        assert meal.getMealConfig().getClass() == MealConfig
    }

    def "Add draftMeal"() {
        given:
        CreateMealRequest createMealRequest = new CreateMealRequest(name: "name", dateOfMeal:  Instant.ofEpochSecond(1711405066), location:  "London", venueName:  "MacD", mealConfig: new MealConfig(draft: true))

        when:
        Meal meal = mealClient.addMeal(createMealRequest)

        then:
        assert meal.getMealDate() == Instant.ofEpochSecond(1711405066)
        assert meal.getUid() == "steven"
        assert meal.getName() == "name"
        assert meal.getVenueName() == "MacD"
        assert meal.getLocation() == "London"
        assert meal.getId() != null
        assert meal instanceof DraftMeal
    }


    def "Add private meal"() {
        Set<String> participant = ["9def4c16-91fa-4de3-9673-bfb0b09cce81"]
        CreateMealRequest createMealRequest = new CreateMealRequest(name:  "name", dateOfMeal:  Instant.ofEpochSecond(1711405066), location:  "London", venueName:  "MacD", mealConfig: new MealConfig(privateMealConfig: new PrivateMealConfig(participant)))

        when:
        Meal meal = mealClient.addMeal(createMealRequest)

        then:
        assert meal.getMealDate() == Instant.ofEpochSecond(1711405066)
        assert meal.getUid() == "steven"
        assert meal.getName() == "name"
        assert meal.getVenueName() == "MacD"
        assert meal.getLocation() == "London"
        assert meal.getId() != null
        assert meal.getClass() == Meal
        assert meal.getMealConfig().getPrivateMealConfig().getRecipientIds() == ["9def4c16-91fa-4de3-9673-bfb0b09cce81"].toSet()
    }

    def "Get Meal"() {
        given:
        CreateMealRequest createMealRequest = new CreateMealRequest(name:  "name", dateOfMeal:  Instant.ofEpochSecond(1711405066), location:  "London", venueName:  "MacD", mealConfig: new MealConfig())

        when:
        Meal createMealResp = mealClient.addMeal(createMealRequest)
        Meal meal = mealClient.fetchMeal(createMealResp.sortKey)

        then:
        meal == createMealResp
    }

    def "get meals by mealDate and id"() {
        given:
        CreateMealRequest createMealRequest = new CreateMealRequest(name:  "name", dateOfMeal:  Instant.ofEpochSecond(1711405066), location:  "London", venueName:  "MacD", mealConfig: new MealConfig())

        when:
        Meal createMealResp = mealClient.addMeal(createMealRequest)
        Meal meal = mealClient.getMeal(createMealResp.getMealDate().toEpochMilli(), createMealResp.getId()).get()

        then:
        meal == createMealResp
    }

    def "List all meals for current user"() {
        given:
        CreateMealRequest createMealRequest = new CreateMealRequest(name:  "name", dateOfMeal:  Instant.ofEpochSecond(1711405066), location:  "London", venueName:  "MacD", mealConfig: new MealConfig())

        when:
        mealClient.addMeal(createMealRequest)
        List<Meal> mealList = mealClient.listAllMealsForUser()
        then:
        assert !mealList.isEmpty()
        assert mealList.every {Meal meal -> meal.uid == "steven"}
    }

    def "Ensure meal is deleted"() {
        given:
        CreateMealRequest createMealRequest = new CreateMealRequest(name:  "name", dateOfMeal:  Instant.ofEpochSecond(1711405066), location:  "London", venueName:  "MacD", mealConfig: new MealConfig())

        when:
        Meal meal = mealClient.addMeal(createMealRequest)
        DeleteMealRequest deleteMealRequest = new DeleteMealRequest(
                meal.getUid(),
                meal.getMealDate(),
                meal.getId()
        )
        mealClient.deleteMeal(deleteMealRequest)
        Meal mealAfterDelete = mealClient.fetchMeal(meal.getSortKey())
        List<Order> orderAfterDelete = mealClient.listAllOrdersForMeal(meal.getId())

        then:
        assert mealAfterDelete == null
        assert orderAfterDelete.size() == 0

    }
}
