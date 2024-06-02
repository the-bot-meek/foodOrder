package com.example.models.meal;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
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
    private PrivateMealConfig privateMealConfig;
    private Boolean draft = false;

    public MealConfig() {

    }

    public Boolean getDraft() {
        return draft;
    }

    public void setDraft(Boolean draft) {
        this.draft = draft;
    }

    @DynamoDBAttribute
    public PrivateMealConfig getPrivateMealConfig() {
        return privateMealConfig;
    }

    public void setPrivateMealConfig(PrivateMealConfig privateMealConfig) {
        this.privateMealConfig = privateMealConfig;
    }
}
