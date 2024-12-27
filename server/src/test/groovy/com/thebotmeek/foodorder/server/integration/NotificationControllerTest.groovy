package com.thebotmeek.foodorder.server.integration

import com.foodorder.server.client.NotificationClient
import com.foodorder.server.models.Notification
import com.foodorder.server.request.CreateNotificationRequest
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.IgnoreIf
import spock.lang.Specification

import java.time.Instant

@MicronautTest(environments = ["mock_auth"])
@IgnoreIf({System.getenv("requireIntegrationTests") != 'true'})
class NotificationControllerTest extends Specification{
    @Inject
    NotificationClient notificationClient
    CreateNotificationRequest genericCreateNotificationRequest

    def "setup"() {
        genericCreateNotificationRequest = new CreateNotificationRequest("title", "body", "/actionPath")
    }

    def "Test adding notification"() {
        when:
        Instant beforeSend = Instant.now()
        Notification notification = notificationClient.addNotificationForUser(genericCreateNotificationRequest)

        then:
        assert notification.getActionPath() == genericCreateNotificationRequest.actionPath()
        assert notification.getBody() == genericCreateNotificationRequest.body()
        assert notification.getId() != null
        assert notification.getTitle() == genericCreateNotificationRequest.title()
        assert notification.getDateAdded().isAfter(beforeSend)
    }

    def "test getting all notification for user"() {
        when:
        List<Notification> notificationListFromSave = []
        for (int i : 1..10) {
            notificationListFromSave.add(notificationClient.addNotificationForUser(genericCreateNotificationRequest))
        }

        List<Notification> notificationListFromFetch = notificationClient.getAllNotificationsForCurrentUser()

        then:
        List<Notification> filteredNotificationListFromFetch = notificationListFromFetch.findAll {
            notification -> notificationListFromSave.contains(notification)
        }

        assert filteredNotificationListFromFetch.size() == 10
        assert filteredNotificationListFromFetch == notificationListFromSave
    }
}
