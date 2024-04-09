#!/bin/bash
awslocal dynamodb create-table \
  --table-name primary_table \
  --attribute-definitions \
    AttributeName=pk,AttributeType=S \
    AttributeName=sk,AttributeType=S \
  --key-schema \
    AttributeName=pk,KeyType=HASH \
    AttributeName=sk,KeyType=RANGE \
  --provisioned-throughput \
          ReadCapacityUnits=5,WriteCapacityUnits=5 \
  --region eu-west-1

awslocal dynamodb create-table \
  --table-name order_table \
  --attribute-definitions \
    AttributeName=meal_id,AttributeType=S \
    AttributeName=date_of_meal,AttributeType=S \
  --key-schema \
    AttributeName=meal_id,KeyType=HASH \
    AttributeName=date_of_meal,KeyType=RANGE \
  --provisioned-throughput \
    ReadCapacityUnits=5,WriteCapacityUnits=5 \
    --region eu-west-1
