package com.foodorder.server.services;

import com.foodorder.server.models.Notification;
import jakarta.inject.Named;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;

import java.util.List;


@Singleton
public class NotificationService {

    private final Logger log = LoggerFactory.getLogger(MealService.class);
    private final IDynamoDBFacadeService dynamoDBFacadeService;
    public NotificationService(@Named("primary-table") IDynamoDBFacadeService dynamoDBFacadeService) {
        this.dynamoDBFacadeService = dynamoDBFacadeService;
    }

    public void addNotification(Notification notification) {
        log.debug("Adding notification: {}", notification);
        dynamoDBFacadeService.save(notification);
    }

    public void removeNotification(Notification notification) {
        log.debug("Removing notification: {}", notification);
        dynamoDBFacadeService.delete(notification);
    }

    public List<Notification> getAllNotificationsForUserId(String userId) {
        log.debug("Getting all notifications for user {}", userId);
        final String pk = "Notification_" + userId;
        final Key key = Key.builder().partitionValue(pk).build();
        QueryConditional queryConditional = QueryConditional.keyEqualTo(key);
        return this.dynamoDBFacadeService.query(Notification.class, queryConditional);
    }
}
