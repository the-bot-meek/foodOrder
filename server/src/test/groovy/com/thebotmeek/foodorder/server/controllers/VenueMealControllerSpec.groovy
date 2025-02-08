package com.thebotmeek.foodorder.server.controllers

import com.foodorder.models.models.meal.Meal
import com.foodorder.server.controllers.VenueMealController
import com.foodorder.server.repository.IDynamoDBFacadeRepository
import com.foodorder.server.repository.MealRepository
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional
import spock.lang.Specification

class VenueMealControllerSpec extends Specification {
    VenueMealController venueMealController
    IDynamoDBFacadeRepository dynamoDBFacadeService

    def "setup"() {
        dynamoDBFacadeService = Mock(IDynamoDBFacadeRepository)
        MealRepository mealService = new MealRepository(dynamoDBFacadeService)
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
