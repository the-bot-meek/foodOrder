package com.foodorder.models.models;


import dynamodbTypeConverters.MenuItemSetConverter;
import io.micronaut.serde.annotation.Serdeable;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.*;

import java.util.Objects;
import java.util.Set;


@DynamoDbBean
@Serdeable
public class Venue implements Model {
    private String id;
    private String name;
    private String location;
    private String description;
    private Set<MenuItem> menuItems;
    private String phoneNumber;

    public Venue(String id, String name, String location, String description, Set<MenuItem> menuItems, String phoneNumber) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.description = description;
        this.menuItems = menuItems;
        this.phoneNumber = phoneNumber;
    }

    public Venue() {

    }

    @DynamoDbPartitionKey
    @DynamoDbAttribute("pk")
    public String getPrimaryKey() {
        return "Venue_" + this.location;
    }

    public void setPrimaryKey(String pk) {
        this.location = pk.replace("Venue_", "");
    }

    @Override
    @DynamoDbAttribute("sk")
    @DynamoDbSortKey
    public String getSortKey() {
        return name;
    }

    @Override
    public void setSortKey(String sortKey) {
        name = sortKey;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @DynamoDbConvertedBy(MenuItemSetConverter.class)
    public Set<MenuItem> getMenuItems() {
        return menuItems;
    }

    public void setMenuItems(Set<MenuItem> menuItems) {
        this.menuItems = menuItems;
    }


    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Venue venue)) return false;

        return Objects.equals(id, venue.id) && Objects.equals(name, venue.name) && Objects.equals(location, venue.location) && Objects.equals(description, venue.description) && Objects.equals(menuItems, venue.menuItems) && Objects.equals(phoneNumber, venue.phoneNumber);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(id);
        result = 31 * result + Objects.hashCode(name);
        result = 31 * result + Objects.hashCode(location);
        result = 31 * result + Objects.hashCode(description);
        result = 31 * result + Objects.hashCode(menuItems);
        result = 31 * result + Objects.hashCode(phoneNumber);
        return result;
    }
}
