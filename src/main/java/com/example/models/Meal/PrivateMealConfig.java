package com.example.models.Meal;

import java.util.HashSet;
import java.util.Set;

public class PrivateMealConfig extends MealConfig {
    private Set<String> recipientIds = new HashSet<>();

    public PrivateMealConfig(Set<String> recipientIds) {
        this.recipientIds = recipientIds;
    }

    public PrivateMealConfig() {

    }

    public Set<String> getRecipientIds() {
        return recipientIds;
    }

    public void addRecipientId(String recipientId) {
        this.recipientIds.add(recipientId);
    }

    public void removeRecipientId(String recipientId) {
        this.recipientIds.remove(recipientId);
    }
}
