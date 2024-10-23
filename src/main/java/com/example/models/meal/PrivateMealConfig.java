package com.example.models.meal;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import io.micronaut.serde.annotation.Serdeable;

import java.util.HashSet;
import java.util.Set;

@DynamoDBDocument
@Serdeable
public class PrivateMealConfig {
    private Set<String> recipientIds = new HashSet<>();

    public PrivateMealConfig(Set<String> recipientIds) {
        this.recipientIds = recipientIds;
    }

    public PrivateMealConfig() {

    }

    public Set<String> getRecipientIds() {
        return recipientIds;
    }

    public void setRecipientIds(Set<String> recipientIds) {
        this.recipientIds = recipientIds;
    }

    public void addRecipientId(String recipientId) {
        this.recipientIds.add(recipientId);
    }

    public void removeRecipientId(String recipientId) {
        this.recipientIds.remove(recipientId);
    }
}
