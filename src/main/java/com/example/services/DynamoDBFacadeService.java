package com.example.services;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.ConversionSchemas;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
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
    public <T> Optional<T> load(Class<T> clazz, String primaryKey, String sortKey) {
        log.trace("Loading clazz: {}, primaryKey: {}, sortKey: {}", clazz, primaryKey, sortKey);
        return Optional.ofNullable(dynamoDBMapper.load(clazz, primaryKey, sortKey));
    }

    @Override
    public <T> void save(T object) {
        log.trace("Saving object: {}", object);
        dynamoDBMapper.save(object);
    }


    @Override
    public <T> List<T> query(Class<T> clazz, DynamoDBQueryExpression<T> dynamoDBQueryExpression) {
        log.trace("Getting clazz: {}, dynamoDBQueryExpression: {}", clazz, dynamoDBQueryExpression);
        return dynamoDBMapper.query(clazz, dynamoDBQueryExpression);
    }
}
