package com.example.dto.request;

import com.example.models.meal.MealConfig;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.core.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;
import java.util.Objects;

@Introspected
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME)
@JsonSubTypes({@JsonSubTypes.Type(value = CreateMealRequest.class, name = "Meal")})
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
        @Nullable
        Boolean draft;
        @NotNull
        MealConfig mealConfig;

        public CreateMealRequest(String name, Instant dateOfMeal, String location, String venueName, Boolean draft, MealConfig mealConfig) {
                this.name = name;
                this.dateOfMeal = dateOfMeal;
                this.location = location;
                this.venueName = venueName;
                this.draft = draft;
                this.mealConfig = mealConfig;
        }

        public CreateMealRequest(String name, Instant dateOfMeal, String location, String venueName, Boolean draft) {
                this.name = name;
                this.dateOfMeal = dateOfMeal;
                this.location = location;
                this.venueName = venueName;
                this.draft = draft;
                this.mealConfig = new MealConfig();
        }

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

        public Boolean getDraft() {
                if (draft == null) return false;
                return draft;
        }

        public MealConfig getMealConfig() {
                return mealConfig;
        }

        public void setMealConfig(MealConfig mealConfig) {
                this.mealConfig = mealConfig;
        }

        public void setDraft(Boolean draft) {
                this.draft = draft;
        }

        @Override
        public boolean equals(Object object) {
                if (this == object) return true;
                if (!(object instanceof CreateMealRequest that)) return false;

                if (!Objects.equals(name, that.name)) return false;
                if (!Objects.equals(dateOfMeal, that.dateOfMeal)) return false;
                if (!Objects.equals(location, that.location)) return false;
                if (!Objects.equals(venueName, that.venueName)) return false;
            return Objects.equals(draft, that.draft);
        }

        @Override
        public int hashCode() {
                int result = name != null ? name.hashCode() : 0;
                result = 31 * result + (dateOfMeal != null ? dateOfMeal.hashCode() : 0);
                result = 31 * result + (location != null ? location.hashCode() : 0);
                result = 31 * result + (venueName != null ? venueName.hashCode() : 0);
                result = 31 * result + (draft != null ? draft.hashCode() : 0);
                return result;
        }
}


