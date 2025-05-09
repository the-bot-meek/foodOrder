package com.thebotmeek.menuparser;

import io.micronaut.context.event.ApplicationEventPublisher;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.Optional;

@Singleton
public class DefaultMenuParserFacade implements MenuParserFacade {
    private static final Logger log = LoggerFactory.getLogger(DefaultMenuParserFacade.class);
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
        log.info("Parsing menu for user: {}, supportedFileTypes: {}", userId, supportedFileTypes.toString());
        MenuParseTask menuParseTask = menuParseTaskRepository.save(new MenuParseTask(userId));
        menuParseEventPublisher.publishEventAsync(new MenuParseEvent(menu, menuParseTask, supportedFileTypes));
        return menuParseTask;
    }

    @Override
    public Optional<MenuParseTask> getTask(String taskId, String userId) {
        return menuParseTaskRepository.getTask(taskId, userId);
    }
}
