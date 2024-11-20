package com.foodorder.server.models;


import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIndexHashKey;
import com.foodorder.server.models.meal.Meal;

import java.util.Set;

public class AnonymousOrder extends Order {
    public AnonymousOrder() {

    }

    public AnonymousOrder(String id, Meal meal, String uid, String participantsName, Set<MenuItem> menuItems) {
        super(id, meal, uid, participantsName, menuItems);
    }

    @DynamoDBIndexHashKey(globalSecondaryIndexName = "uid_gsi", attributeName = "uid")
    public String getGSIPrimaryKey() {
        return "AnonymousOrder_" + uid;
    }

    public void setGSIPrimaryKey(String primaryKeyGSI) {
        uid = primaryKeyGSI.replace("AnonymousOrder_", "");
    }
}
