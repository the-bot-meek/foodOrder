package com.foodorder.server.configuration;

import io.micronaut.context.annotation.EachProperty;
import io.micronaut.context.annotation.Parameter;

import java.util.Objects;

@EachProperty("dynamodb-tables")
public class TableConfigration {
    private final String tableId;



    private String tableName;
    TableConfigration(@Parameter String tableId) {
        this.tableId = tableId;
    }

    public String getTableId() {
        return tableId;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TableConfigration that)) return false;

        return Objects.equals(tableName, that.tableName);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(tableName);
    }
}
