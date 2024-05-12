package com.example.dto.request;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;

@Introspected
@JsonSerialize
@Serdeable.Deserializable
public record CreateMealRequest(
        @NotNull
        @NotBlank
        String name,
        @NotNull
        Instant dateOfMeal,
        @NotNull
        @NotBlank
        String location,
        @NotNull
        @NotBlank
        String venueName
) {

        @Override
        public String toString() {
                final StringBuilder sb = new StringBuilder("CreateMealRequest{");
                sb.append("name='").append(name).append('\'');
                sb.append(", dateOfMeal=").append(dateOfMeal);
                sb.append(", location='").append(location).append('\'');
                sb.append(", venueName='").append(venueName).append('\'');
                sb.append('}');
                return sb.toString();
        }
}


