package com.thebotmeek.foodorder.server.controllers

import com.foodorder.server.controllers.VenueMealController
import com.foodorder.server.models.meal.Meal
import com.foodorder.server.services.IDynamoDBFacadeService
import com.foodorder.server.services.MealService
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional
import spock.lang.Specification

class VenueMealControllerSpec extends Specification {
    VenueMealController venueMealController
    IDynamoDBFacadeService dynamoDBFacadeService

    def "setup"() {
        dynamoDBFacadeService = Mock(IDynamoDBFacadeService)
        MealService mealService = new MealService(dynamoDBFacadeService)
        venueMealController = new VenueMealController(mealService)
    }

    def "get meal from venueName and mealId"() {
        given:
        Meal meal = new Meal(id: "9e2-0eccd796-924a-45f2e-23089298f845")
        String venueName = "SomeVenueName"
        String mealId = "0eccd796-924a-45f2-9e2e-23089298f845"

        when:
        Optional<Meal> fetchedMeal = venueMealController.getMeal(venueName, mealId)

        then:
        1 * dynamoDBFacadeService.queryWithIndex(Meal.class, _ as QueryConditional, "gsi") >> [meal]
        assert fetchedMeal.isPresent()
        assert meal == fetchedMeal.get()
    }

    def "get meal from venueName and mealId"() {
        given:
        String venueName = "SomeVenueName"
        String mealId = "0eccd796-924a-45f2-9e2e-23089298f845"

        when:
        Optional<Meal> fetchedMeal = venueMealController.getMeal(venueName, mealId)

        then:
        1 * dynamoDBFacadeService.queryWithIndex(Meal.class, _ as QueryConditional, "gsi") >> []
        assert fetchedMeal.isEmpty()
    }
}
