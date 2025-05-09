package com.thebotmeek.menuparser;

import jakarta.validation.Valid;

import java.io.InputStream;
import java.util.Optional;

public interface MenuParserFacade {
    /**
     * Parses the menu from the given InputStream.
     *
     * @param menu the InputStream containing the menu data
     * @return taskId
     */
    MenuParseTask parseMenu(InputStream menu, String userId, SupportedFileTypes supportedFileTypes);
    Optional<@Valid MenuParseTask> getTask(String taskId, String userId);
}
