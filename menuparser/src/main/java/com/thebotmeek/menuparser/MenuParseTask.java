package com.thebotmeek.menuparser;

import com.foodorder.server.dynamodbTypeConverters.MenuItemSetConverter;
import com.foodorder.server.models.MenuItem;
import com.foodorder.server.models.Model;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.*;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@DynamoDbBean
@Serdeable
public class MenuParseTask implements Model {
    @NotNull
    String taskId;
    @NotNull
    String userId;
    @NotNull
    Status status;
    @NotNull
    Set<MenuItem> results;
    @NotNull
    Instant dateCreated;

    public MenuParseTask() {
    }

    public MenuParseTask(String userId) {
        this.userId = userId;
        this.status = Status.SUBMITTED;
        this.dateCreated = Instant.now();
    }


    @Override
    @DynamoDbPartitionKey
    @DynamoDbAttribute("pk")
    public String getPrimaryKey() {
        return "MenuParseTask_" + userId;
    }

    @Override
    public void setPrimaryKey(String value) {
    }

    @Override
    @DynamoDbSortKey
    @DynamoDbAttribute("sk")
    public String getSortKey() {
        return taskId;
    }

    @Override
    public void setSortKey(String sortKey) {

    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @DynamoDbConvertedBy(MenuItemSetConverter.class)
    public Set<MenuItem> getResults() {
        return results;
    }

    public void setResults(Set<MenuItem> results) {
        this.results = results;
    }

    public Instant getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Instant dateCreated) {
        this.dateCreated = dateCreated;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MenuParseTask that)) return false;

        return Objects.equals(taskId, that.taskId) && Objects.equals(userId, that.userId) && status == that.status && Objects.equals(results, that.results) && Objects.equals(dateCreated, that.dateCreated);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(taskId);
        result = 31 * result + Objects.hashCode(userId);
        result = 31 * result + Objects.hashCode(status);
        result = 31 * result + Objects.hashCode(results);
        result = 31 * result + Objects.hashCode(dateCreated);
        return result;
    }
}
