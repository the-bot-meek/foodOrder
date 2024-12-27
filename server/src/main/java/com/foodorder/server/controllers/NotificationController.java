package com.foodorder.server.controllers;

import com.foodorder.server.models.Notification;
import com.foodorder.server.request.CreateNotificationRequest;
import com.foodorder.server.services.NotificationService;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.rules.SecurityRule;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Secured(SecurityRule.IS_AUTHENTICATED)
@Controller("/Notification")
public class NotificationController {
    NotificationService notificationService;

    NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @Get
    public List<Notification> getNotificationsForCurrentUser(Authentication authentication) {
        return notificationService.getAllNotificationsForUserId(authentication.getName());
    }

    @Post
    public Notification addNotification(Authentication authentication, @Body CreateNotificationRequest createNotificationRequest) {
        Notification notification = new Notification();
        notification.setUserId(authentication.getName());
        notification.setTitle(createNotificationRequest.title());
        notification.setBody(createNotificationRequest.body());
        notification.setActionPath(createNotificationRequest.actionPath());
        notification.setId(UUID.randomUUID().toString());
        notification.setDateAdded(Instant.now());

        this.notificationService.addNotification(notification);
        return notification;
    }
}
