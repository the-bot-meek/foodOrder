package com.example.mocks;

import io.micronaut.context.annotation.ConfigurationProperties;

@ConfigurationProperties("mockAuthConfig")
public class MockAuthConfig {
    /**
     * When sessionLessStateAuth is true, a new test session id will be generated for each session.
     * The generated session id is then used as the name in the Authentication object used by the application.
     * This allows us to isolate automated e2e tests to allow for more consist results
     */
    private boolean sessionLessStateAuth = true;

    public boolean isSessionLessStateAuth() {
        return sessionLessStateAuth;
    }

    public void setSessionLessStateAuth(boolean sessionLessStateAuth) {
        this.sessionLessStateAuth = sessionLessStateAuth;
    }
}
