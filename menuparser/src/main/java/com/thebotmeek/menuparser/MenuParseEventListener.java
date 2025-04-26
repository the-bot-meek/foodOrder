package com.thebotmeek.menuparser;

import com.thebotmeek.menuparser.exception.MenuParserException;
import io.micronaut.context.event.ApplicationEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class MenuParseEventListener implements ApplicationEventListener<MenuParseEvent> {
    private static final Logger log = LoggerFactory.getLogger(MenuParseEventListener.class);
    private final Map<String, MenuParser> menuParsers;

    MenuParseEventListener(Map<String, MenuParser> menuParsers) {
        this.menuParsers = menuParsers;
    }

    @Override
    public void onApplicationEvent(MenuParseEvent event) {
        log.trace("Parsing menu for event: {}", event);
        MenuParser menuParser = menuParsers.get(event.getSupportedFileTypes().toString());
        parseMenu(event, menuParser);
    }

    private void parseMenu(MenuParseEvent event, MenuParser menuParser) {
        try {
            if (menuParser == null) {
                throw new MenuParserException(
                        event.getMenuParseTask().getUserId(),
                        event.getMenuParseTask().getTaskId(),
                        new Exception("No parser found for file type: " + event.getSupportedFileTypes())
                );
            }
            log.trace("Parsing menu with parser: {}, menuParser: {}", menuParser, menuParser.getClass());
            menuParser.parse(event);
        } catch (MenuParserException e) {
            throw new RuntimeException(e);
        }
    }
}
