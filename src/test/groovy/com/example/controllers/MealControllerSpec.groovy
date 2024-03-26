package com.example.controllers

import com.example.dto.CreateMealRequest
import com.example.models.Meal
import com.example.services.IDynamoDBFacadeService
import com.example.services.MealService
import spock.lang.Specification

import java.security.Principal
import java.time.Instant

class MealControllerSpec extends Specification {
    def "AddMeal"() {
        given:
        IDynamoDBFacadeService dynamoDBFacadeService = Mock(IDynamoDBFacadeService)
        MealService mealService = new MealService(dynamoDBFacadeService)
        MealController mealController = new MealController(mealService)
        CreateMealRequest createMealRequest = new CreateMealRequest("name", 1711405066, "123456789")
        Principal principal = Mock(Principal)
        principal.getName() >> "principal_name"

        when:
        mealController.addMeal(createMealRequest, principal)

        then:
        1 * dynamoDBFacadeService.save(_) >> { Meal meal ->
            meal.with {
                assert it.getName() == "name"
                assert it.getMealDate() == Instant.ofEpochSecond(1711405066)
                assert it.getMenuId() == "123456789"
                assert it.getUid() == "principal_name"
                assert it.getSortKey().startsWith("2024-03-25T22:17:46Z_")
                assert it.getPrimaryKeyValue() == "Meal_principal_name"
            }
        }
    }

    def "GetMeal"() {
        given:
        String mealSortKey = "2024-03-25T22:17:46Z_797b001f-de8f-47ed-833a-d84e61c73fe7"
        IDynamoDBFacadeService dynamoDBFacadeService = Mock(IDynamoDBFacadeService)
        dynamoDBFacadeService.load(Meal.class,"principal_name", mealSortKey) >> {
            Optional.of(
                    new Meal(id: "797b001f-de8f-47ed-833a-d84e61c73fe7", name: "name", mealDate: Instant.ofEpochSecond(1711405066), menuId: "123456789", uid: "principal_name")
            )
        }

        MealService mealService = new MealService(dynamoDBFacadeService)
        MealController mealController = new MealController(mealService)
        Principal principal = Mock(Principal)
        principal.getName() >> "principal_name"

        when:
        Optional<Meal> meal = mealController.getMeal(mealSortKey, principal)

        then:
        assert meal.isPresent()
        meal.get() == new Meal(id: "797b001f-de8f-47ed-833a-d84e61c73fe7", name: "name", mealDate: Instant.ofEpochSecond(1711405066), menuId: "123456789", uid: "principal_name")
    }

    def "ListMeals"() {
        given:
        IDynamoDBFacadeService dynamoDBFacadeService = Mock(IDynamoDBFacadeService)
        MealService mealService = new MealService(dynamoDBFacadeService)
        MealController mealController = new MealController(mealService)
        Principal principal = Mock(Principal)
        principal.getName() >> "principal_name"
        dynamoDBFacadeService.list(Meal, _) >> {return [new Meal(id: "797b001f-de8f-47ed-833a-d84e61c73fe7", name: "name", mealDate: Instant.ofEpochSecond(1711405066), menuId: "123456789", uid: "principal_name")]}

        when:
        List<Meal> mealList = mealController.listMeals(principal)

        then:
        mealList == [new Meal(id: "797b001f-de8f-47ed-833a-d84e61c73fe7", name: "name", mealDate: Instant.ofEpochSecond(1711405066), menuId: "123456789", uid: "principal_name")]
    }
}
