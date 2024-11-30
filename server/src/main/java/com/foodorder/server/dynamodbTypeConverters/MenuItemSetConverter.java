package com.foodorder.server.dynamodbTypeConverters;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.foodorder.server.models.MenuItem;
import software.amazon.awssdk.enhanced.dynamodb.AttributeConverter;
import software.amazon.awssdk.enhanced.dynamodb.AttributeValueType;
import software.amazon.awssdk.enhanced.dynamodb.EnhancedType;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;


import java.util.Set;

public class MenuItemSetConverter implements AttributeConverter<Set<MenuItem>> {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Override
    public AttributeValue transformFrom(Set<MenuItem> menuItems) {
        try {
            String json = OBJECT_MAPPER.writeValueAsString(menuItems);
            return AttributeValue.builder().s(json).build();
        } catch (Exception e) {
            throw new RuntimeException("Error converting Set<MenuItem> to JSON", e);
        }
    }

    @Override
    public Set<MenuItem> transformTo(AttributeValue attributeValue) {
        try {
            String json = attributeValue.s();
            return OBJECT_MAPPER.readValue(json, new TypeReference<Set<MenuItem>>() {});
        } catch (Exception e) {
            throw new RuntimeException("Error converting JSON to Set<MenuItem>", e);
        }
    }

    @Override
    public EnhancedType<Set<MenuItem>> type() {

        return new EnhancedType<Set<MenuItem>>() {};
    }

    @Override
    public AttributeValueType attributeValueType() {
        return  AttributeValueType.S;
    }
}