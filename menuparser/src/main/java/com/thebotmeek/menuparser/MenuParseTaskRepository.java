package com.thebotmeek.menuparser;

import com.foodorder.server.models.meal.Meal;
import com.foodorder.server.repository.IDynamoDBFacadeRepository;
import jakarta.inject.Named;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.UUID;

@Singleton
public class MenuParseTaskRepository {
    private final Logger log = LoggerFactory.getLogger(MenuParseTaskRepository.class);
    private final IDynamoDBFacadeRepository dynamoDBFacadeRepository;

    MenuParseTaskRepository(@Named("primary-table") IDynamoDBFacadeRepository dynamoDBFacadeRepository) {
        this.dynamoDBFacadeRepository = dynamoDBFacadeRepository;
    }

    public MenuParseTask save(MenuParseTask menuParseTask) {
        log.trace("Saving MenuParseTask: {}", menuParseTask);
        if (menuParseTask.getTaskId() == null) {
            log.trace("Task ID is null, generating a new one");
            menuParseTask.setTaskId(UUID.randomUUID().toString());
        }
        dynamoDBFacadeRepository.save(menuParseTask);
        return menuParseTask;
    }

    public Optional<MenuParseTask> getTask(String taskId, String userId) {
        log.trace("Getting MenuParseTask with taskId: {} and userId: {}", taskId, userId);
        String pk = "MenuParseTask_" + userId;
        return dynamoDBFacadeRepository.load(MenuParseTask.class, pk, taskId);
    }
}
