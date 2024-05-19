package com.example.models.Venue;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import com.example.models.MenuItem;
import com.example.models.Model;
import io.micronaut.serde.annotation.Serdeable;

import java.util.Objects;
import java.util.Set;

@DynamoDBTable(tableName = "primary_table")
@Serdeable
public class Venue implements Model {
    private String id;
    private String name;
    private String location;
    private String description;
    private Set<MenuItem> menuItems;

    public Venue(String id, String name, String location, String description, Set<MenuItem> menuItems) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.description = description;
        this.menuItems = menuItems;
    }

    public Venue() {

    }

    @Override
    @DynamoDBIgnore
    public String getPrimaryKeySuffix() {
        return "Venue_";
    }

    @DynamoDBHashKey(attributeName = "pk")
    public String getPrimaryKey() {
        return getPrimaryKeySuffix() + this.location;
    }

    public void setPrimaryKey(String pk) {
        this.location = pk.replace(getPrimaryKeySuffix(), "");
    }

    @Override
    public String getSortKey() {
        return name;
    }

    @Override
    public void setSortKey(String sortKey) {
        name = sortKey;
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
    public Set<MenuItem> getMenuItems() {
        return menuItems;
    }

    public void setMenuItems(Set<MenuItem> menuItems) {
        this.menuItems = menuItems;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof Venue venue)) return false;

        if (!Objects.equals(id, venue.id)) return false;
        if (!Objects.equals(name, venue.name)) return false;
        if (!Objects.equals(location, venue.location)) return false;
        if (!Objects.equals(description, venue.description)) return false;
        return Objects.equals(menuItems, venue.menuItems);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (location != null ? location.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (menuItems != null ? menuItems.hashCode() : 0);
        return result;
    }
}
