package com.thebotmeek.menuparser;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.io.InputStream;

public class MenuParseEvent {
    @NotNull
    InputStream inputStream;

    @NotNull
    @Valid
    MenuParseTask menuParseTask;

    @NotNull
    SupportedFileTypes supportedFileTypes;

    public MenuParseEvent(InputStream inputStream, @NotNull @Valid MenuParseTask menuParseTask, @NotNull SupportedFileTypes supportedFileTypes) {
        this.inputStream = inputStream;
        this.menuParseTask = menuParseTask;
        this.supportedFileTypes = supportedFileTypes;
    }
}
