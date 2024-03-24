package com.example.models;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import io.micronaut.serde.annotation.Serdeable;

@DynamoDBTable(tableName = "primary_table")
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
}
