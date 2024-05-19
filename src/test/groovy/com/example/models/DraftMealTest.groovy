package com.example.models

import com.example.models.Meal.DraftMeal
import spock.lang.Specification

class DraftMealTest extends Specification {
    def "test primary key deserialization"() {
        given:
        DraftMeal draftMeal = new DraftMeal()

        when:
        draftMeal.setPrimaryKey("DraftMeal_12345")

        then:
        draftMeal.getUid() == "12345"
    }

    def "test primary key serialization"() {
        given:
        DraftMeal draftMeal = new DraftMeal()

        when:
        draftMeal.setUid("12345")

        then:
        draftMeal.getPrimaryKey() == "DraftMeal_12345"
    }
}
