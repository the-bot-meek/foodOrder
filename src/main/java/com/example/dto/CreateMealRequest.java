package com.example.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.micronaut.core.annotation.Introspected;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.util.LinkedHashMap;

@Introspected
@JsonSerialize
public record CreateMealRequest(
        @NotNull
        @NotBlank
        String name,
        @NotNull
        Long mealDate,
        @NotNull
        @NotBlank
        String venueMenuId
) {

}


