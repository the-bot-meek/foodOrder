package com.foodorder.server.models.orderParticipant;

import com.fasterxml.jackson.annotation.JsonIgnore;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbIgnore;

public class AnonomusOrderParticipant extends OrderParticipant{
    public AnonomusOrderParticipant(String name, String userId) {
        super(name, userId);
    }

    public AnonomusOrderParticipant() {
        super();
    }

    @Override
    @JsonIgnore
    public String getKey() {
        return "ANONYMOUS";
    }
}
