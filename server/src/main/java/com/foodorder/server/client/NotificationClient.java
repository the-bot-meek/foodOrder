package com.foodorder.server.client;

import com.foodorder.server.models.Notification;
import com.foodorder.server.request.CreateNotificationRequest;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.client.annotation.Client;
import jakarta.validation.Valid;

import java.util.List;

@Client("/Notification")
public interface NotificationClient {
    @Get
    List<Notification> getAllNotificationsForCurrentUser();

    @Post
    Notification addNotificationForUser(@Valid @Body CreateNotificationRequest notification);
}
