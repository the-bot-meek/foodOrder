package com.foodorder.server.request;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.foodorder.server.converters.CreatePrivateMealRequest;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;

@Introspected
@Serdeable
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, defaultImpl = CreateMealRequest.class, property = "mealType")
@JsonSubTypes({@JsonSubTypes.Type(value = CreateMealRequest.class, name = "Meal"), @JsonSubTypes.Type(value = CreatePrivateMealRequest.class, name = "PrivateMeal")})
public class CreateMealRequest {
        @NotNull
        @NotBlank
        protected String name;
        @NotNull
        protected Instant dateOfMeal;
        @NotNull
        @NotBlank
        protected String location;
        @NotNull
        @NotBlank
        protected String venueName;

        @NotNull
        protected Boolean isDraft = false;

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

        public @NotNull Boolean getDraft() {
                return isDraft;
        }

        public void setDraft(@NotNull Boolean draft) {
                isDraft = draft;
        }
}


