package com.thebotmeek.api.request;

import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.Valid;

@Serdeable
public class CreateMealConfig {
    @Valid
    private CreatePrivateMealConfig createPrivateMealConfig;
    private boolean draft;

    public CreateMealConfig() {
    }

    public CreateMealConfig(CreatePrivateMealConfig createPrivateMealConfig, boolean draft) {
        this.createPrivateMealConfig = createPrivateMealConfig;
        this.draft = draft;
    }

    public @Valid CreatePrivateMealConfig getCreatePrivateMealConfig() {
        return createPrivateMealConfig;
    }

    public void setCreatePrivateMealConfig(@Valid CreatePrivateMealConfig createPrivateMealConfig) {
        this.createPrivateMealConfig = createPrivateMealConfig;
    }

    public boolean isDraft() {
        return draft;
    }

    public void setDraft(boolean draft) {
        this.draft = draft;
    }
}
