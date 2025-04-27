package com.thebotmeek.menuparser;

import io.micronaut.context.annotation.ConfigurationProperties;
import io.micronaut.context.annotation.Requires;

/**
 * THis class is used to configure the behavior of the MockTextToMenuItemParser.
 * The main purpose of it is to allow for errors to be thrown during testing.
 */
@Requires(beans = {MockTextToMenuItemParser.class})
@ConfigurationProperties("mockTextToMenuItemConfig")
public class MockTextToMenuItemParserConfig {
    private boolean error = false;

    public MockTextToMenuItemParserConfig() {
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }
}
