package com.example.services;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.example.models.Model;

import java.util.List;
import java.util.Optional;

public interface IDynamoDBFacadeService {
    <T extends Model> Optional<T> load(Class<T> clazz, String primaryKey, String sortKey);
    <T extends Model> void save(T object);
    <T extends Model> List<T> query(Class<T> clazz, DynamoDBQueryExpression<T> dynamoDBQueryExpression);
    <T extends Model> void delete(T entity);
    <T extends Model> void batchDelete(List<T> list);
}
