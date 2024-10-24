package com.example.mocks;

import io.micronaut.context.annotation.Requires;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.MutableHttpResponse;
import io.micronaut.http.annotation.Filter;
import io.micronaut.http.cookie.Cookie;
import io.micronaut.http.filter.HttpServerFilter;
import io.micronaut.http.filter.ServerFilterChain;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;
import java.util.UUID;


@Filter("/**")
@Requires(env = "mock_auth")
@Requires(property = "mockAuthConfig.sessionLessStateAuth", value = "true")
public class TestIdInterceptor implements HttpServerFilter {
    @Override
    public Publisher<MutableHttpResponse<?>> doFilter(HttpRequest<?> request, ServerFilterChain chain) {
        // If a user-Agent is not provided, then we will assume that it is a unit test and proceed without the test_user_session cookie
        if (!request.getHeaders().contains("user-Agent")) {
            return chain.proceed(request);
        }
        Cookie testUserIdCookie = request.getCookies().get("test_user_session");
        boolean hasCookieBeenAdded = testUserIdCookie != null;
        if (!hasCookieBeenAdded) {
            testUserIdCookie = Cookie.of("test_user_session", UUID.randomUUID().toString());
            request = request.mutate().cookie(testUserIdCookie);
        }

        final Cookie finalTestUserIdCookie = testUserIdCookie;
        return Mono.from(chain.proceed(request)).map((MutableHttpResponse<?> resp) -> {
            if(!hasCookieBeenAdded) {
                resp.cookie(finalTestUserIdCookie);
            }
            return resp;
        });
    }
}
