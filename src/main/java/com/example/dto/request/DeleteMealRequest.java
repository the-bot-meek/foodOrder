package com.example.dto.request;

import io.micronaut.core.annotation.Introspected;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;

@Introspected
public record DeleteMealRequest(
        @NotNull
        @NotEmpty
        String uid,
        @NotNull
        Instant mealDate,
        @NotNull
        @NotEmpty
        String id
) {
}
