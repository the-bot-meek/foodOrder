package com.foodorder.server.request;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Introspected
@Serdeable
public record CreateNotificationRequest(
    @NotNull
    @NotBlank
    String title,
    String body,
    String actionPath
){
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("CreateNotificationRequest{");
        sb.append("title='").append(title).append('\'');
        sb.append(", body='").append(body).append('\'');
        sb.append(", actionPath='").append(actionPath).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
