package com.example.models.Meal;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME)
@JsonSubTypes({
        @JsonSubTypes.Type(value = MealConfig.class, name = "MealConfig"),
        @JsonSubTypes.Type(value = PrivateMealConfig.class, name = "PrivateMealConfig")
})
@DynamoDBDocument
public class MealConfig {

}
