package com.example.dto.request;

import com.example.models.meal.MealConfig;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.micronaut.core.annotation.Introspected;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;
import java.util.Objects;

@Introspected
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
        String venueName;
        @NotNull
        MealConfig mealConfig;

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

        public String getVenueName() {
                return venueName;
        }

        public void setVenueName(String venueName) {
                this.venueName = venueName;
        }
        public MealConfig getMealConfig() {
                return mealConfig;
        }

        public void setMealConfig(MealConfig mealConfig) {
                this.mealConfig = mealConfig;
        }


        @Override
        public boolean equals(Object object) {
                if (this == object) return true;
                if (!(object instanceof CreateMealRequest that)) return false;

                if (!Objects.equals(name, that.name)) return false;
                if (!Objects.equals(dateOfMeal, that.dateOfMeal)) return false;
                if (!Objects.equals(location, that.location)) return false;
                if (!Objects.equals(venueName, that.venueName)) return false;
            return Objects.equals(mealConfig, that.mealConfig);
        }

        @Override
        public int hashCode() {
                int result = name != null ? name.hashCode() : 0;
                result = 31 * result + (dateOfMeal != null ? dateOfMeal.hashCode() : 0);
                result = 31 * result + (location != null ? location.hashCode() : 0);
                result = 31 * result + (venueName != null ? venueName.hashCode() : 0);
                result = 31 * result + (mealConfig != null ? mealConfig.hashCode() : 0);
                return result;
        }
}


