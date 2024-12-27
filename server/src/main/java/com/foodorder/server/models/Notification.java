package com.foodorder.server.models;

import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;
import java.time.Instant;
import java.util.Objects;

@DynamoDbBean
@Serdeable
public class Notification implements Model {
    @NotNull
    @NotBlank
    private String userId;

    @NotNull
    @NotBlank
    private Instant dateAdded;

    @NotNull
    @NotBlank
    private String title;
    private String body;
    private String actionPath;
    private boolean read = false;

    @NotNull
    @NotBlank
    private String id;

    public Notification() {

    }

    @Override
    @DynamoDbPartitionKey
    @DynamoDbAttribute("pk")
    public String getPrimaryKey() {
        return "Notification_" + userId;
    }

    @Override
    public void setPrimaryKey(String value) {
        this.userId = value.replace("Notification_", "");
    }

    @Override
    @DynamoDbAttribute("sk")
    @DynamoDbSortKey
    public String getSortKey() {
        return this.dateAdded.toString() + "_" + this.userId;
    }

    @Override
    public void setSortKey(String sortKey) {

    }

    public @NotNull @NotBlank String getUserId() {
        return userId;
    }

    public void setUserId(@NotNull @NotBlank String userId) {
        this.userId = userId;
    }

    public @NotNull @NotBlank Instant getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(@NotNull @NotBlank Instant dateAdded) {
        this.dateAdded = dateAdded;
    }

    public @NotNull @NotBlank String getTitle() {
        return title;
    }

    public void setTitle(@NotNull @NotBlank String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getActionPath() {
        return actionPath;
    }

    public void setActionPath(String actionPath) {
        this.actionPath = actionPath;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public @NotNull @NotBlank String getId() {
        return id;
    }

    public void setId(@NotNull @NotBlank String id) {
        this.id = id;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Notification that)) return false;

        return read == that.read && Objects.equals(userId, that.userId) && Objects.equals(dateAdded, that.dateAdded) && Objects.equals(title, that.title) && Objects.equals(body, that.body) && Objects.equals(actionPath, that.actionPath) && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(userId);
        result = 31 * result + Objects.hashCode(dateAdded);
        result = 31 * result + Objects.hashCode(title);
        result = 31 * result + Objects.hashCode(body);
        result = 31 * result + Objects.hashCode(actionPath);
        result = 31 * result + Boolean.hashCode(read);
        result = 31 * result + Objects.hashCode(id);
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Notification{");
        sb.append("userId='").append(userId).append('\'');
        sb.append(", dateAdded=").append(dateAdded);
        sb.append(", title='").append(title).append('\'');
        sb.append(", body='").append(body).append('\'');
        sb.append(", actionPath='").append(actionPath).append('\'');
        sb.append(", read=").append(read);
        sb.append(", id='").append(id).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
