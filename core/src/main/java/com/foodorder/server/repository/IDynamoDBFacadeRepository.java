package com.foodorder.server.repository;

import com.foodorder.server.models.Model;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;

import java.util.List;
import java.util.Optional;

public interface IDynamoDBFacadeRepository {
    <T extends Model> Optional<T> load(Class<T> clazz, String primaryKey, String sortKey);
    <T extends Model> void save(T object);
    <T extends Model> List<T> query(Class<T> clazz, QueryConditional dynamoDBQueryExpression);
    <T extends Model> List<T> queryWithIndex(Class<T> clazz, QueryConditional dynamoDBQueryExpression, String indexName);
    <T extends Model> void delete(T entity);
    <T extends Model> void batchDelete(List<T> list);
    <T extends Model> List<?> batchSave(List<T> tList);
}
