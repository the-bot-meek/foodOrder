package com.foodorder.server.models.meal;

import io.micronaut.serde.annotation.Serdeable;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;

@Serdeable
@DynamoDbBean
public class MealConfig {
    private PrivateMealConfig privateMealConfig;
    private Boolean draft = false;

    public MealConfig() {

    }

    public MealConfig(PrivateMealConfig privateMealConfig, Boolean draft) {
        this.privateMealConfig = privateMealConfig;
        this.draft = draft;
    }

    public Boolean getDraft() {
        return draft;
    }

    public void setDraft(Boolean draft) {
        this.draft = draft;
    }

    public PrivateMealConfig getPrivateMealConfig() {
        return privateMealConfig;
    }

    public void setPrivateMealConfig(PrivateMealConfig privateMealConfig) {
        this.privateMealConfig = privateMealConfig;
    }
}
