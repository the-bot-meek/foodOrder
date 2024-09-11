package com.example.cacheTests

import com.example.models.meal.Meal
import com.example.services.IDynamoDBFacadeService
import com.example.services.MealService
import io.micronaut.test.annotation.MockBean
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.Specification
import java.time.Instant


@MicronautTest
class MealServiceCacheTest extends Specification {
    @Inject
    MealService mealService

    @Inject
    IDynamoDBFacadeService dynamoDBFacadeService

    Meal meal

    def "setup"() {
        meal = new Meal(uid: "uid-1234", mealDate: Instant.ofEpochSecond(100000), id: "12345")
        dynamoDBFacadeService.save(_ as Meal) >> meal
    }

    def "test adding a meal and then trying to get it again uses the cached value"() {
        when:
        Meal savedMeal = mealService.saveMeal(meal)
        Optional<Meal> fetchedMeal = mealService.getMeal(meal.getPrimaryKey(), meal.getSortKey())

        then:
        assert savedMeal == fetchedMeal.get()
    }

    def "test that deleting a meal Invalidates the cache"() {
        when:
        Meal savedMeal = mealService.saveMeal(meal)
        Optional<Meal> mealBeforeInvalidation = mealService.getMeal(meal.getPrimaryKey(), meal.getSortKey())
        mealService.deleteMeal(meal)
        Optional<Meal> deletedMeal = mealService.getMeal(meal.getPrimaryKey(), meal.getSortKey())

        then:
        assert savedMeal == mealBeforeInvalidation.get()
        assert deletedMeal.isEmpty()
        1 * dynamoDBFacadeService.load(Meal, meal.getPrimaryKey(), meal.getSortKey()) >> Optional.empty()
    }

    def "test that a value returned from Dynamodb is cached"() {
        when:
        Optional<Meal> fetchedMealInitCall = mealService.getMeal(meal.getPrimaryKey(), meal.getSortKey())
        Optional<Meal> fetchedMealCachedCall = mealService.getMeal(meal.getPrimaryKey(), meal.getSortKey())

        then:
        1 * dynamoDBFacadeService.load(Meal, meal.getPrimaryKey(), meal.getSortKey()) >> Optional.of(meal)
        fetchedMealInitCall == fetchedMealCachedCall
    }

    @MockBean(IDynamoDBFacadeService)
    IDynamoDBFacadeService dynamoDBFacadeService() {
        return Mock(IDynamoDBFacadeService)
    }
}

