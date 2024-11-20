package com.foodorder.server.request;

import com.foodorder.server.models.MenuItem;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

@Introspected
@JsonSerialize
@Serdeable
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
        @Override
        public String toString() {
                final StringBuilder sb = new StringBuilder("CreateVenueRequest{");
                sb.append("menuItems=").append(menuItems);
                sb.append(", location='").append(location).append('\'');
                sb.append(", name='").append(name).append('\'');
                sb.append(", description='").append(description).append('\'');
                sb.append('}');
                return sb.toString();
        }
}
