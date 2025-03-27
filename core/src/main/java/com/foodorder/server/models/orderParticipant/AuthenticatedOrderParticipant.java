package com.foodorder.server.models.orderParticipant;

import com.fasterxml.jackson.annotation.JsonIgnore;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbIgnore;

public class AuthenticatedOrderParticipant extends OrderParticipant {
    public AuthenticatedOrderParticipant(String name, String userId) {
        super(name, userId);
    }

    public AuthenticatedOrderParticipant() {
        super();
    }

    @Override
    @JsonIgnore
    public String getKey() {
        return "AUTHENTICATED";
    }
}
