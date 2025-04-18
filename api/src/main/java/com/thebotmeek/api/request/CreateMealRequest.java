package com.thebotmeek.api.request;

import com.foodorder.server.models.meal.MealConfig;
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

        public String getMenuName() {
                return menuName;
        }

        public void setMenuName(String menuName) {
                this.menuName = menuName;
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
                if (!Objects.equals(menuName, that.menuName)) return false;
            return Objects.equals(mealConfig, that.mealConfig);
        }

        @Override
        public int hashCode() {
                int result = name != null ? name.hashCode() : 0;
                result = 31 * result + (dateOfMeal != null ? dateOfMeal.hashCode() : 0);
                result = 31 * result + (location != null ? location.hashCode() : 0);
                result = 31 * result + (menuName != null ? menuName.hashCode() : 0);
                result = 31 * result + (mealConfig != null ? mealConfig.hashCode() : 0);
                return result;
        }
}


