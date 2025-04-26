package com.thebotmeek.menuparser;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.io.InputStream;
import java.util.Objects;

public class MenuParseEvent {
    @NotNull
    private InputStream inputStream;

    @NotNull
    @Valid
    private MenuParseTask menuParseTask;

    @NotNull
    private SupportedFileTypes supportedFileTypes;

    public MenuParseEvent(InputStream inputStream, @NotNull @Valid MenuParseTask menuParseTask, @NotNull SupportedFileTypes supportedFileTypes) {
        this.inputStream = inputStream;
        this.menuParseTask = menuParseTask;
        this.supportedFileTypes = supportedFileTypes;
    }

    public MenuParseEvent() {

    }

    public @NotNull InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(@NotNull InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public @NotNull @Valid MenuParseTask getMenuParseTask() {
        return menuParseTask;
    }

    public void setMenuParseTask(@NotNull @Valid MenuParseTask menuParseTask) {
        this.menuParseTask = menuParseTask;
    }

    public @NotNull SupportedFileTypes getSupportedFileTypes() {
        return supportedFileTypes;
    }

    public void setSupportedFileTypes(@NotNull SupportedFileTypes supportedFileTypes) {
        this.supportedFileTypes = supportedFileTypes;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MenuParseEvent that)) return false;

        return Objects.equals(inputStream, that.inputStream) && Objects.equals(menuParseTask, that.menuParseTask) && supportedFileTypes == that.supportedFileTypes;
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(inputStream);
        result = 31 * result + Objects.hashCode(menuParseTask);
        result = 31 * result + Objects.hashCode(supportedFileTypes);
        return result;
    }
}
