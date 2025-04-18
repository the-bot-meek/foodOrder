package com.thebotmeek.api.controllers

import com.thebotmeek.api.controllers.MenuMealController
import com.foodorder.server.models.meal.Meal
import com.foodorder.server.repository.IDynamoDBFacadeRepository
import com.foodorder.server.repository.MealRepository
import spock.lang.Specification

class MenuMealControllerSpec extends Specification {
    MenuMealController menuMealController
    MealRepository mealRepository

    def "setup"() {
        mealRepository = Mock(MealRepository)
        menuMealController = new MenuMealController(mealRepository)
    }

    def "get meal from menuName and mealId"() {
        given:
        Meal meal = new Meal(
                id: "9e2-0eccd796-924a-45f2e-23089298f845",
                name: "SomeMenuName"
        )


        when:
        Optional<Meal> fetchedMeal = menuMealController.getMeal(meal.getName(), meal.getId())

        then:
        1 * mealRepository.getMealByMenuNameAndMealId(meal.getName(), meal.getId()) >> Optional.of(meal)
        assert fetchedMeal.isPresent()
        assert meal == fetchedMeal.get()
    }

    def "Fail to get meal from menuName and mealId"() {
        given:
        Meal meal = new Meal(
                id: "0eccd796-924a-45f2-9e2e-23089298f845",
                name: "SomeMenuName"
        )

        when:
        Optional<Meal> fetchedMeal = menuMealController.getMeal(meal.getName(), meal.getId())

        then:
        1 * mealRepository.getMealByMenuNameAndMealId(meal.getName(), meal.getId()) >> Optional.empty()
        assert fetchedMeal.isEmpty()
    }
}
