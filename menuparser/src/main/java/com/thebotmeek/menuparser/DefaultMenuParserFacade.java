package com.thebotmeek.menuparser;

import jakarta.inject.Singleton;

import java.io.InputStream;
import java.util.Optional;

@Singleton
public class DefaultMenuParserFacade implements MenuParserFacade {
    private final MenuParseTaskRepository menuParseTaskRepository;

    DefaultMenuParserFacade(MenuParseTaskRepository menuParseTaskRepository) {
        this.menuParseTaskRepository = menuParseTaskRepository;
    }

    @Override
    public MenuParseTask parseMenu(InputStream menu, String userId) {
        MenuParseTask menuParseTask = menuParseTaskRepository.save(new MenuParseTask(userId));
        return menuParseTask;
    }

    @Override
    public Optional<MenuParseTask> getTask(String taskId, String userId) {
        return menuParseTaskRepository.getTask(taskId, userId);
    }
}
