package com.foodorder.server.repository;


import com.foodorder.server.configuration.TableConfigration;
import com.foodorder.models.models.Model;
import io.micronaut.context.annotation.EachBean;
import jakarta.inject.Singleton;
import software.amazon.awssdk.enhanced.dynamodb.*;
import software.amazon.awssdk.enhanced.dynamodb.model.Page;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

import java.util.List;
import java.util.Optional;

@Singleton
@EachBean(TableConfigration.class)
public class DynamoDBEnhancedFacadeRepository implements IDynamoDBFacadeRepository {
    private final DynamoDbEnhancedClient enhancedClient;
    private final String tableName;

    DynamoDBEnhancedFacadeRepository(DynamoDbClient dynamoDbClient, TableConfigration tableConfigration) {
        this.enhancedClient = DynamoDbEnhancedClient.builder()
                .dynamoDbClient(dynamoDbClient)
                .build();
        this.tableName = tableConfigration.getTableName();
    }
    @Override
    public <T extends Model> Optional<T> load(Class<T> clazz, String primaryKey, String sortKey) {
        DynamoDbTable<T> table = enhancedClient.table(tableName, TableSchema.fromBean(clazz));
        Key key = Key.builder().partitionValue(primaryKey).sortValue(sortKey).build();
        return Optional.ofNullable(table.getItem(key));
    }

    @Override
    public <T extends Model> void save(T object) {
        Class<T> clazz = (Class<T>) object.getClass();
        DynamoDbTable<T> table = enhancedClient.table(tableName, TableSchema.fromBean(clazz));
        table.putItem(object);
    }

    @Override
    public <T extends Model> List<T> query(Class<T> clazz, QueryConditional dynamoDBQueryExpression) {
        DynamoDbTable<T> table = enhancedClient.table(tableName, TableSchema.fromBean(clazz));
        return table.query(r -> r.queryConditional(dynamoDBQueryExpression))
                .items()
                .stream()
                .toList();
    }

    @Override
    public <T extends Model> List<T> queryWithIndex(Class<T> clazz, QueryConditional dynamoDBQueryExpression, String indexName) {
        DynamoDbTable<T> table = enhancedClient.table(tableName, TableSchema.fromBean(clazz));
        return table.index(indexName).query(r -> r.queryConditional(dynamoDBQueryExpression))
                .stream()
                .map(Page::items)
                .flatMap(List::stream)
                .toList();
    }

    @Override
    public <T extends Model> void delete(T entity) {
        Class<T> clazz = (Class<T>) entity.getClass();
        DynamoDbTable<T> table = enhancedClient.table(tableName, TableSchema.fromBean(clazz));
        table.deleteItem(entity);
    }

    @Override
    public <T extends Model> void batchDelete(List<T> list) {
        list.forEach(this::delete);
    }

    @Override
    public <T extends Model> List<?> batchSave(List<T> ts) {
        ts.forEach(this::save);
        return List.of();
    }
}
