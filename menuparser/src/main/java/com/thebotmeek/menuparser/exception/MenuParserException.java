package com.thebotmeek.menuparser.exception;

public class MenuParserException extends Exception {
    public MenuParserException(String userId, String taskId, Exception cause) {
        super(String.format("User %s, Task %s: %s", userId, taskId, cause));
    }
}
