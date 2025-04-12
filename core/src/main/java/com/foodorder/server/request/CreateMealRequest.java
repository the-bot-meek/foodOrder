package com.foodorder.server.request;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;
import java.util.Objects;

@Introspected
@Serdeable
public class CreateMealRequest {
        @NotNull
        @NotBlank
        String name;
        @NotNull
        Instant dateOfMeal;
        @NotNull
        @NotBlank
        String location;
        @NotNull
        @NotBlank
        String menuName;
        @NotNull
        CreateMealConfig createMealConfig;

        public CreateMealRequest() {

        }

        public String getName() {
                return name;
        }

        public void setName(String name) {
                this.name = name;
        }

        public Instant getDateOfMeal() {
                return dateOfMeal;
        }

        public void setDateOfMeal(Instant dateOfMeal) {
                this.dateOfMeal = dateOfMeal;
        }

        public String getLocation() {
                return location;
        }

        public void setLocation(String location) {
                this.location = location;
        }

        public String getMenuName() {
                return menuName;
        }

        public void setMenuName(String menuName) {
                this.menuName = menuName;
        }

        public @NotNull CreateMealConfig getCreateMealConfig() {
                return createMealConfig;
        }

        public void setCreateMealConfig(@NotNull CreateMealConfig createMealConfig) {
                this.createMealConfig = createMealConfig;
        }

        @Override
        public final boolean equals(Object o) {
                if (this == o) return true;
                if (!(o instanceof CreateMealRequest that)) return false;

            return Objects.equals(name, that.name) && Objects.equals(dateOfMeal, that.dateOfMeal) && Objects.equals(location, that.location) && Objects.equals(menuName, that.menuName) && Objects.equals(createMealConfig, that.createMealConfig);
        }

        @Override
        public int hashCode() {
                int result = Objects.hashCode(name);
                result = 31 * result + Objects.hashCode(dateOfMeal);
                result = 31 * result + Objects.hashCode(location);
                result = 31 * result + Objects.hashCode(menuName);
                result = 31 * result + Objects.hashCode(createMealConfig);
                return result;
        }
}


