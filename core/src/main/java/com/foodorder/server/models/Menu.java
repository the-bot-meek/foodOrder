package com.foodorder.server.models;

import com.foodorder.server.dynamodbTypeConverters.MenuItemSetConverter;
import io.micronaut.serde.annotation.Serdeable;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.*;

import java.util.Objects;
import java.util.Set;


@DynamoDbBean
@Serdeable
public class Menu implements Model {
    private String id;
    private String name;
    private String location;
    private String description;
    private Set<MenuItem> menuItems;
    private String phoneNumber;

    public Menu(String id, String name, String location, String description, Set<MenuItem> menuItems, String phoneNumber) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.description = description;
        this.menuItems = menuItems;
        this.phoneNumber = phoneNumber;
    }

    public Menu() {

    }

    @DynamoDbPartitionKey
    @DynamoDbAttribute("pk")
    public String getPrimaryKey() {
        return "Menu_" + this.location;
    }

    public void setPrimaryKey(String pk) {
        this.location = pk.replace("Menu_", "");
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
        if (!(o instanceof Menu menu)) return false;

        return Objects.equals(id, menu.id) && Objects.equals(name, menu.name) && Objects.equals(location, menu.location) && Objects.equals(description, menu.description) && Objects.equals(menuItems, menu.menuItems) && Objects.equals(phoneNumber, menu.phoneNumber);
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
