package com.foodorder.server.models.orderParticipant;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.foodorder.server.models.meal.Meal;
import io.micronaut.serde.annotation.Serdeable;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbIgnore;

import java.util.Objects;

@Serdeable
@DynamoDbBean
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, defaultImpl = Meal.class)
@JsonSubTypes({@JsonSubTypes.Type(value = AnonomusOrderParticipant.class, name = "AnonomusOrderParticipant"), @JsonSubTypes.Type(value = AuthenticatedOrderParticipant.class, name = "AuthenticatedOrderParticipant")})
public abstract class OrderParticipant {
    private String name;
    private String userId;

    public OrderParticipant(String name, String userId) {
        this.name = name;
        this.userId = userId;
    }

    public OrderParticipant() {

    }

    @JsonIgnore
    public abstract String getKey();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OrderParticipant that)) return false;

        return Objects.equals(name, that.name) && Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(name);
        result = 31 * result + Objects.hashCode(userId);
        return result;
    }
}
