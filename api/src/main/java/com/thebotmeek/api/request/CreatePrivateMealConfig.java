package com.thebotmeek.api.request;

import io.micronaut.serde.annotation.Serdeable;

import java.util.Objects;

@Serdeable
public class CreatePrivateMealConfig {
    private int numberOfRecipients;

    public CreatePrivateMealConfig() {
    }

    public CreatePrivateMealConfig(int numberOfRecipients) {
        this.numberOfRecipients = numberOfRecipients;
    }

    public int getNumberOfRecipients() {
        return numberOfRecipients;
    }

    public void setNumberOfRecipients(int numberOfRecipients) {
        this.numberOfRecipients = numberOfRecipients;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CreatePrivateMealConfig that)) return false;

        return Objects.equals(numberOfRecipients, that.numberOfRecipients);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(numberOfRecipients);
    }
}
