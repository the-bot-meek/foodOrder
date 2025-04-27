package com.thebotmeek.menuparser;

import io.micronaut.context.annotation.Replaces;
import io.micronaut.context.annotation.Requires;
import jakarta.inject.Singleton;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;


@Singleton
@Requires(env = {"mock_auth"})
@Replaces(DefaultTextToMenuItemParser.class)
public class MockTextToMenuItemParser implements TextToMenuItemParser {
    private final String DEFAULT_MENU_ITEMS_JSON;
    MockTextToMenuItemParser() throws IOException {
        InputStream in = getClass()
                .getClassLoader()
                .getResourceAsStream("default_menu-items.json");
        assert in != null;
        DEFAULT_MENU_ITEMS_JSON = new String(in.readAllBytes(), StandardCharsets.UTF_8);
    }

    @Override
    public String parseMenu(String menuText) {
        return DEFAULT_MENU_ITEMS_JSON;
    }
}
