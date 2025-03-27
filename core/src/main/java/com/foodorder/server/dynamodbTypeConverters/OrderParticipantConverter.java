package com.foodorder.server.dynamodbTypeConverters;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.foodorder.server.models.MenuItem;
import com.foodorder.server.models.orderParticipant.OrderParticipant;
import software.amazon.awssdk.enhanced.dynamodb.AttributeConverter;
import software.amazon.awssdk.enhanced.dynamodb.AttributeValueType;
import software.amazon.awssdk.enhanced.dynamodb.EnhancedType;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.util.Set;

public class OrderParticipantConverter implements AttributeConverter<OrderParticipant> {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Override
    public AttributeValue transformFrom(OrderParticipant orderParticipant) {
        try {
            String json = OBJECT_MAPPER.writeValueAsString(orderParticipant);
            return AttributeValue.builder().s(json).build();
        } catch (Exception e) {
            throw new RuntimeException("Error converting OrderParticipant to JSON", e);
        }
    }

    @Override
    public OrderParticipant transformTo(AttributeValue attributeValue) {
        try {
            String json = attributeValue.s();
            return OBJECT_MAPPER.readValue(json, OrderParticipant.class);
        } catch (Exception e) {
            throw new RuntimeException("Error converting JSON to OrderParticipant", e);
        }
    }

    @Override
    public EnhancedType<OrderParticipant> type() {
        return new EnhancedType<OrderParticipant>() {};
    }

    @Override
    public AttributeValueType attributeValueType() {
        return AttributeValueType.S;
    }
}
