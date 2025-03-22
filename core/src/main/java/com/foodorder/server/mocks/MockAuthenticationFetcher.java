package com.foodorder.server.mocks;

import io.micronaut.context.annotation.Replaces;
import io.micronaut.context.annotation.Requires;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.cookie.Cookie;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.authentication.ServerAuthentication;
import jakarta.inject.Singleton;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;
import io.micronaut.security.filters.AuthenticationFetcher;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Singleton
@Requires(env = "mock_auth")
@Replaces(AuthenticationFetcher.class)
public class MockAuthenticationFetcher implements AuthenticationFetcher<HttpRequest<?>> {
    public static final Logger log = LoggerFactory.getLogger(MockAuthenticationFetcher.class);
    private final MockAuthConfig mockAuthConfig;
    MockAuthenticationFetcher(MockAuthConfig mockAuthConfig) {
        this.mockAuthConfig = mockAuthConfig;
    }

    @Override
    public Publisher<Authentication> fetchAuthentication(HttpRequest<?> request) {
        log.debug("Fetching mock authentication for {}", request.getUri());
        Cookie testUserIdCookie = request.getCookies().get("test_user_session");
        if (testUserIdCookie == null || !mockAuthConfig.isSessionLessStateAuth()) {
            testUserIdCookie = Cookie.of("test_user_session", "steven");
        }
        log.debug("Test user session cookie is {}", testUserIdCookie.getValue());
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("name", "The bot meek");

        if (mockAuthConfig.isAdmin()) {
            log.debug("Adding admin role to user testUserId: {}", testUserIdCookie.getValue());
            attributes.put("foodorder_roles", "FoodOrderAdminUser");
        }
        Authentication authentication = new ServerAuthentication(testUserIdCookie.getValue(), null, attributes);
        return Mono.just(authentication);
    }
}
