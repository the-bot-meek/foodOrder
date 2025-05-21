package com.thebotmeek.menuparser;

import dev.langchain4j.service.SystemMessage;
import io.micronaut.langchain4j.annotation.AiService;

public interface TextToMenuItemParser {
    String parseMenu(String menuText);
}
