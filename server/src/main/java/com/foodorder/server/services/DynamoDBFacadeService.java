package com.foodorder.server.services;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.*;
import com.foodorder.server.models.Model;
import io.micronaut.context.annotation.Requires;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

@Singleton
@Requires(bean = AmazonDynamoDB.class)
public class DynamoDBFacadeService implements IDynamoDBFacadeService{
    public final AmazonDynamoDB dynamoDbClient;
    private final DynamoDBMapper dynamoDBMapper;
    private final Logger log = LoggerFactory.getLogger(DynamoDBFacadeService.class);

    public DynamoDBFacadeService(AmazonDynamoDB dynamoDbClient) {
        this.dynamoDbClient = dynamoDbClient;
        DynamoDBMapperConfig dynamoDBMapperConfig = DynamoDBMapperConfig
                .builder()
                .withConversionSchema(ConversionSchemas.V2)
                .build();
        this.dynamoDBMapper = new DynamoDBMapper(dynamoDbClient, dynamoDBMapperConfig);
    }

    @Override
    public <T extends Model> Optional<T> load(Class<T> clazz, String primaryKey, String sortKey) {
        log.debug("Loading clazz: {}, primaryKey: {}, sortKey: {}", clazz, primaryKey, sortKey);
        return Optional.ofNullable(dynamoDBMapper.load(clazz, primaryKey, sortKey));
    }

    @Override
    public <T extends Model> void save(T object) {
        log.debug("Saving object: {}", object);
        dynamoDBMapper.save(object);
    }


    @Override
    public <T extends Model> List<T> query(Class<T> clazz, DynamoDBQueryExpression<T> dynamoDBQueryExpression) {
        log.debug("Getting clazz: {}, dynamoDBQueryExpression: {}", clazz, dynamoDBQueryExpression);
        return dynamoDBMapper.query(clazz, dynamoDBQueryExpression);
    }

    @Override
    public <T extends Model> void delete(T entity) {
        log.debug("Deleting entity: {}", entity);
        dynamoDBMapper.delete(entity);
    }

    public <T extends Model> void batchDelete(List<T> tList) {
        log.debug("Deleting entitys: {}", tList);
        dynamoDBMapper.batchDelete(tList);
    }

    public <T extends Model> List<DynamoDBMapper.FailedBatch> batchSave(List<T> tList) {
        return dynamoDBMapper.batchSave(tList);
    }
}
