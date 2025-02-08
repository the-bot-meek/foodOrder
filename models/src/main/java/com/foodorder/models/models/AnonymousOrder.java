package com.foodorder.models.models;

import com.foodorder.models.models.meal.Meal;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSecondaryPartitionKey;

import java.util.Set;

@DynamoDbBean
public class AnonymousOrder extends Order {
    public AnonymousOrder() {

    }

    public AnonymousOrder(String id, Meal meal, String uid, String participantsName, Set<MenuItem> menuItems) {
        super(id, meal, uid, participantsName, menuItems);
    }

    @Override
    @DynamoDbAttribute("uid")
    @DynamoDbSecondaryPartitionKey(indexNames = "uid_gsi")
    public String getGSIPrimaryKey() {
        return "AnonymousOrder_" + uid;
    }

    public void setGSIPrimaryKey(String primaryKeyGSI) {
        uid = primaryKeyGSI.replace("AnonymousOrder_", "");
    }
}
