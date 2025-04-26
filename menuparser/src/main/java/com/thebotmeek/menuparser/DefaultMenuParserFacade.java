package com.thebotmeek.menuparser;

import io.micronaut.context.event.ApplicationEventPublisher;
import jakarta.inject.Singleton;

import java.io.InputStream;
import java.util.Optional;

@Singleton
public class DefaultMenuParserFacade implements MenuParserFacade {
    private final MenuParseTaskRepository menuParseTaskRepository;
    private final ApplicationEventPublisher<MenuParseEvent> menuParseEventPublisher;

    DefaultMenuParserFacade(
            MenuParseTaskRepository menuParseTaskRepository,
            ApplicationEventPublisher<MenuParseEvent> menuParseEventPublisher
    ) {
        this.menuParseTaskRepository = menuParseTaskRepository;
        this.menuParseEventPublisher = menuParseEventPublisher;
    }

    @Override
    public MenuParseTask parseMenu(InputStream menu, String userId, SupportedFileTypes supportedFileTypes) {
        MenuParseTask menuParseTask = menuParseTaskRepository.save(new MenuParseTask(userId));
        menuParseEventPublisher.publishEvent(new MenuParseEvent(menu, menuParseTask, supportedFileTypes));
        return menuParseTask;
    }

    @Override
    public Optional<MenuParseTask> getTask(String taskId, String userId) {
        return menuParseTaskRepository.getTask(taskId, userId);
    }
}
