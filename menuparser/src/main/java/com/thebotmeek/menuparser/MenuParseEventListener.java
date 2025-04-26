package com.thebotmeek.menuparser;

import com.thebotmeek.menuparser.exception.MenuParserException;
import io.micronaut.context.event.ApplicationEventListener;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Map;

@Singleton
public class MenuParseEventListener implements ApplicationEventListener<MenuParseEvent> {
    private static final Logger log = LoggerFactory.getLogger(MenuParseEventListener.class);
    private final Map<String, MenuParser> menuParsers;
    private final MenuParseTaskRepository menuParseTaskRepository;

    MenuParseEventListener(
            Map<String, MenuParser> menuParsers,
            MenuParseTaskRepository menuParseTaskRepository
    ) {
        this.menuParsers = menuParsers;
        this.menuParseTaskRepository =  menuParseTaskRepository;
    }

    @Override
    public void onApplicationEvent(MenuParseEvent event) {
        log.trace("Parsing menu for event: {}", event);
        MenuParser menuParser = menuParsers.get(event.getSupportedFileTypes().toString());
        parseMenu(event, menuParser);
    }

    private void parseMenu(MenuParseEvent event, MenuParser menuParser) {
        MenuParseTask menuParseTask = event.getMenuParseTask();
        try {
            if (menuParser == null) {
                throw new MenuParserException(
                        menuParseTask.getUserId(),
                        menuParseTask.getTaskId(),
                        new Exception("No parser found for file type: " + event.getSupportedFileTypes())
                );
            }
            log.trace("Parsing menu with parser: {}, menuParser: {}", menuParser, menuParser.getClass());
            menuParseTask.setResults(menuParser.parse(event));
            menuParseTask.setStatus(Status.SUCCESS);
        } catch (MenuParserException e) {
            log.error("Failed to parse menu items", e);
            menuParseTask.setStatus(Status.ERROR);
        }
        menuParseTaskRepository.save(menuParseTask);
    }
}
