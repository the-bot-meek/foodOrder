package com.example.services;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import io.micronaut.context.annotation.Requires;
import io.micronaut.context.annotation.Value;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

@Singleton
@Requires(property = "micronaut.dynamodb.primary_table.region")
@Requires(property = "micronaut.dynamodb.primary_table.endpoint")
public class DynamoDBFacadeService implements IDynamoDBFacadeService{
    public final AmazonDynamoDB dynamoDbClient;
    private final DynamoDBMapper dynamoDBMapper;
    private final Logger log = LoggerFactory.getLogger(DynamoDBFacadeService.class);

    public DynamoDBFacadeService(
            @Value("${micronaut.dynamodb.primary_table.region}") String endpoint,
            @Value("${micronaut.dynamodb.primary_table.endpoint}") String region
    ) {
        log.trace("DynamoDBFacadeService. endpoint: {}, region: {}", endpoint, region);
        this.dynamoDbClient = AmazonDynamoDBClientBuilder.standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(endpoint, region))
                .build();
        this.dynamoDBMapper = new DynamoDBMapper(dynamoDbClient);
    }

    public <T> Optional<T> load(Class<T> clazz, String primaryKey, String sortKey) {
        log.trace("Loading clazz: {}, primaryKey: {}, sortKey: {}", clazz, primaryKey, sortKey);
        return Optional.ofNullable(dynamoDBMapper.load(clazz, primaryKey, sortKey));
    }

    public <T> List<T> list(Class<T> clazz, DynamoDBQueryExpression<T> dynamoDBQueryExpression) {
        log.trace("Getting clazz: {}, dynamoDBQueryExpression: {}", clazz, dynamoDBQueryExpression);
        return dynamoDBMapper.query(clazz, dynamoDBQueryExpression);
    }

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
