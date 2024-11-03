package com.example.models;

public interface Model {
    String getPrimaryKey();
    void setPrimaryKey(String value);
    String getSortKey();
    void setSortKey(String sortKey);
}
