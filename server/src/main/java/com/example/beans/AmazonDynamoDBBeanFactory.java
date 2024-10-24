package com.example.beans;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import io.micronaut.context.annotation.Factory;
import io.micronaut.context.annotation.Value;
import jakarta.inject.Singleton;

@Factory
public class AmazonDynamoDBBeanFactory {
    private final String endPoint;
    private final String region;
    AmazonDynamoDBBeanFactory(@Value("${micronaut.dynamodb.primary_table.region}") String region,
                              @Value("${micronaut.dynamodb.primary_table.endpoint}") String endPoint) {
        this.endPoint = endPoint;
        this.region = region;
    }

    @Singleton
    AmazonDynamoDB AmazonDynamoDB() {
        return AmazonDynamoDBClientBuilder.standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(endPoint, region))
                .withCredentials(new AWSCredentialsProvider() {

                    @Override
                    public AWSCredentials getCredentials() {
                        return new AWSCredentials() {

                            @Override
                            public String getAWSAccessKeyId() {
                                return "123";
                            }

                            @Override
                            public String getAWSSecretKey() {
                                return "123";
                            }
                        };
                    }

                    @Override
                    public void refresh() {

                    }
                })
                .build();
    }
}
