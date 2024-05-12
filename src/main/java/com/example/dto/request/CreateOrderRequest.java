package com.example.dto.request;

import com.example.models.MenuItem;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;
import java.util.Set;

@Introspected
@JsonSerialize
@Serdeable.Deserializable
public record CreateOrderRequest(
        @NotNull
        Instant dateOfMeal,
        @NotNull
        @NotBlank
        String mealId,
        @NotNull
        @NotEmpty
        Set<MenuItem> menuItems,
        @NotNull
        @NotBlank
        String organizerUid
) {
        @Override
        public String toString() {
                final StringBuilder sb = new StringBuilder("CreateOrderRequest{");
                sb.append("dateOfMeal=").append(dateOfMeal);
                sb.append(", mealId='").append(mealId).append('\'');
                sb.append(", menuItems=").append(menuItems);
                sb.append(", organizerUid='").append(organizerUid).append('\'');
                sb.append('}');
                return sb.toString();
        }
}
