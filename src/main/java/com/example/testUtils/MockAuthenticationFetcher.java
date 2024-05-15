package com.example.testUtils;

import io.micronaut.context.annotation.Replaces;
import io.micronaut.context.annotation.Requires;
import io.micronaut.context.env.Environment;
import io.micronaut.http.HttpRequest;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.authentication.ServerAuthentication;
import jakarta.inject.Singleton;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;
import io.micronaut.security.filters.AuthenticationFetcher;

import java.util.Map;

@Singleton
@Requires(env = Environment.TEST)
@Replaces(AuthenticationFetcher.class)
public class MockAuthenticationFetcher implements AuthenticationFetcher<HttpRequest<?>> {
    @Override
    public Publisher<Authentication> fetchAuthentication(HttpRequest<?> request) {
        Map<String, Object> attributes = Map.of("preferred_username", "The bot meek");
        Authentication authentication = new ServerAuthentication("steven", null, attributes);
        return Mono.just(authentication);
    }
}
