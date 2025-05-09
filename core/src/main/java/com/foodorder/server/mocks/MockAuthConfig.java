package com.foodorder.server.mocks;

import io.micronaut.context.annotation.ConfigurationProperties;

@ConfigurationProperties("mockAuthConfig")
public class MockAuthConfig {
    /**
     * When sessionLessStateAuth is true, a new test session id will be generated for each session.
     * The generated session id is then used as the name in the Authentication object used by the application.
     * This allows us to isolate automated e2e tests to allow for more consist results
     */
    private boolean sessionLessStateAuth = true;


    /**
     * When isAdmin is true, the user is considered an admin user.
     * This allows us to test admin only features in the application by adding the admin role to the JWT
     */
    private boolean isAdmin = true;

    public boolean isSessionLessStateAuth() {
        return sessionLessStateAuth;
    }

    public void setSessionLessStateAuth(boolean sessionLessStateAuth) {
        this.sessionLessStateAuth = sessionLessStateAuth;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }
}
