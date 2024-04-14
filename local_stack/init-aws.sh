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

 awslocal dynamodb create-table --region eu-west-1 --cli-input-json file:///etc/localstack/init/ready.d/order_table.json