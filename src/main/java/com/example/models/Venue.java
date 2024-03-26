package com.example.models;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micronaut.serde.annotation.Serdeable;

import java.util.HashMap;
import java.util.Objects;
import java.util.Set;

@DynamoDBTable(tableName = "primary_table")
@Serdeable
public class Venue {
    private String id;
    private String name;
    private String location;
    private String description;
    private Set<MenuItem> menuItems;

    private final ObjectMapper mapper = new ObjectMapper();

    public Venue(String id, String name, String location, String description, Set<MenuItem> menuItems) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.description = description;
        this.menuItems = menuItems;
    }

    public Venue() {

    }

    @DynamoDBHashKey(attributeName = "pk")
    public String getPrimaryKey() {
        return "Venue_" + this.location;
    }

    public void setPrimaryKey(String pk) {
        this.location = pk.replace("Venue_", "");
    }

    @DynamoDBAttribute
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @DynamoDBRangeKey(attributeName = "sk")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @DynamoDBIgnore
    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @DynamoDBAttribute
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @DynamoDBAttribute
    @DynamoDBTypeConvertedJson
    public Set<MenuItem> getMenuItems() {
        return menuItems;
    }

    public void setMenuItems(Set<HashMap<String, ?>> menuItems) {
        this.menuItems = mapper.convertValue(menuItems, new TypeReference<>(){});
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof Venue venue)) return false;

        if (!Objects.equals(id, venue.id)) return false;
        if (!Objects.equals(name, venue.name)) return false;
        if (!Objects.equals(location, venue.location)) return false;
        if (!Objects.equals(description, venue.description)) return false;
        if (!Objects.equals(menuItems, venue.menuItems)) return false;
        return mapper.equals(venue.mapper);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (location != null ? location.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (menuItems != null ? menuItems.hashCode() : 0);
        result = 31 * result + mapper.hashCode();
        return result;
    }
}
