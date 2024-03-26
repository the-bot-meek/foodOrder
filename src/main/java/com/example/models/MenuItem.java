package com.example.models;

import io.micronaut.serde.annotation.Serdeable;

import java.util.Objects;


@Serdeable
public class MenuItem {
    private String name;
    private String description;
    private Double price;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof MenuItem menuItem)) return false;

        if (!Objects.equals(name, menuItem.name)) return false;
        if (!Objects.equals(description, menuItem.description))
            return false;
        return Objects.equals(price, menuItem.price);
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (price != null ? price.hashCode() : 0);
        return result;
    }
}
