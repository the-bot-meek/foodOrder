package com.example.controllers

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression
import com.example.Converters.CreateMealRequestConverter
import com.example.dto.request.CreateMealRequest
import com.example.models.Meal.Meal
import com.example.services.IDynamoDBFacadeService
import com.example.services.LocationService
import com.example.services.MealService
import io.micronaut.http.exceptions.HttpStatusException
import io.micronaut.security.authentication.Authentication
import spock.lang.Specification
import java.time.Instant

class MealControllerSpec extends Specification {
    private Authentication mockAuthentication(String name) {
        Authentication authentication = Mock(Authentication)
        authentication.getName() >> name
        return authentication
    }
    
    def "AddMeal"() {
        given:
        IDynamoDBFacadeService dynamoDBFacadeService = Mock(IDynamoDBFacadeService)
        LocationService locationService = new LocationService()
        CreateMealRequestConverter createMealRequestConverter = new CreateMealRequestConverter(locationService)
        MealService mealService = new MealService(dynamoDBFacadeService, createMealRequestConverter)
        MealController mealController = new MealController(mealService, null)
        CreateMealRequest createMealRequest = new CreateMealRequest("name", Instant.ofEpochSecond(1711405066), "London", "MacD", null)
        Authentication authentication = mockAuthentication( "principal_name")

        when:
        mealController.addMeal(createMealRequest, authentication)

        then:
        1 * dynamoDBFacadeService.save(_) >> { Meal meal ->
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
        IDynamoDBFacadeService dynamoDBFacadeService = Mock(IDynamoDBFacadeService)
        LocationService locationService = new LocationService()
        CreateMealRequestConverter createMealRequestConverter = new CreateMealRequestConverter(locationService)
        MealService mealService = new MealService(dynamoDBFacadeService, createMealRequestConverter)
        MealController mealController = new MealController(mealService, null)
        CreateMealRequest createMealRequest = new CreateMealRequest("name", Instant.ofEpochSecond(1711405066), "idk", "MacD", null)
        Authentication authentication = mockAuthentication( "principal_name")

        when:
        mealController.addMeal(createMealRequest, authentication)

        then:
        thrown(HttpStatusException)
    }

    def "GetMeal"() {
        given:
        String mealSortKey = "2024-03-25T22:17:46Z_797b001f-de8f-47ed-833a-d84e61c73fe7"
        IDynamoDBFacadeService dynamoDBFacadeService = Mock(IDynamoDBFacadeService)
        dynamoDBFacadeService.load(Meal.class,"principal_name", mealSortKey) >> {
            Optional.of(
                    new Meal(id: "797b001f-de8f-47ed-833a-d84e61c73fe7", name: "name", mealDate: Instant.ofEpochSecond(1711405066), uid: "principal_name", location: "London", venueName: "MacD")
            )
        }

        MealService mealService = new MealService(dynamoDBFacadeService, null)
        MealController mealController = new MealController(mealService, null)
        Authentication authentication = mockAuthentication( "principal_name")

        when:
        Optional<Meal> meal = mealController.getMeal(mealSortKey, authentication)

        then:
        assert meal.isPresent()
        meal.get() == new Meal(id: "797b001f-de8f-47ed-833a-d84e61c73fe7", name: "name", mealDate: Instant.ofEpochSecond(1711405066), uid: "principal_name", location: "London", venueName: "MacD")
    }

    def "ListMeals"() {
        given:
        IDynamoDBFacadeService dynamoDBFacadeService = Mock(IDynamoDBFacadeService)
        MealService mealService = new MealService(dynamoDBFacadeService, null)
        MealController mealController = new MealController(mealService, null)
        Authentication authentication = mockAuthentication("principal_name")
        dynamoDBFacadeService.query(Meal, _ as DynamoDBQueryExpression) >> {return [new Meal(id: "797b001f-de8f-47ed-833a-d84e61c73fe7", name: "name", mealDate: Instant.ofEpochSecond(1711405066), uid: "principal_name", location: "London", venueName: "MacD")]}

        when:
        List<Meal> mealList = mealController.listMeals(authentication)

        then:
        mealList == [new Meal(id: "797b001f-de8f-47ed-833a-d84e61c73fe7", name: "name", mealDate: Instant.ofEpochSecond(1711405066), uid: "principal_name", location: "London", venueName: "MacD")]
    }
}
