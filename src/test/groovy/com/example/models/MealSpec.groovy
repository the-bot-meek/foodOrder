package com.example.models

import com.example.models.Meal.Meal
import spock.lang.Specification

import java.time.Instant

class MealSpec extends Specification {
    def "test primary key sterilization" () {
        given:
        String organiserId = "12345"
        Meal meal = new Meal()

        when:
        meal.setUid(organiserId)

        then:
        meal.getPrimaryKey() == "Meal_" + organiserId
    }

    def "test primary key deserialization"() {
        given:
        Meal meal = new Meal()

        when:
        meal.setPrimaryKey("Meal_12345")

        then:
        meal.getUid() == "12345"
    }

    def "test sort key sterilization"() {
        given:
        Instant instant = Instant.ofEpochSecond(1711049071)
        String id = "12345"
        Meal meal = new Meal()

        when:
        meal.setMealDate(instant)
        meal.setId(id)

        then:
        meal.getSortKey() == "2024-03-21T19:24:31Z_12345"
    }

    def "test sort key deserialization" () {
        given:
        String sortKey = "2024-03-21T19:24:31Z_12345"
        Meal meal = new Meal()

        when:
        meal.setSortKey(sortKey)

        then:
        meal.getMealDate() == Instant.ofEpochSecond(1711049071)
        meal.getId() == "12345"
    }
}
