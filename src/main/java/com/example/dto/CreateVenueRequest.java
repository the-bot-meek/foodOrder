package com.example.dto;

import com.example.models.MenuItem;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

@Introspected
@JsonSerialize
@Serdeable.Deserializable
public record CreateVenueRequest(
        @NotEmpty
        @NotNull
        Set<MenuItem> menuItems,
        @NotNull
        @NotBlank
        String location,
        @NotNull
        @NotBlank
        String name,
        @NotNull
        @NotBlank
        String description
) {
}
