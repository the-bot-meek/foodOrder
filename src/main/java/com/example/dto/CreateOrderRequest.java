package com.example.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;

@Introspected
@JsonSerialize
@Serdeable.Deserializable
public record CreateOrderRequest(
        @NotNull
        Instant dateOfMeal,
        @NotNull
        @NotBlank
        String mealId
) {
}
