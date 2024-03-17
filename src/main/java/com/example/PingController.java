package com.example;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.MutableHttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;

import java.net.URI;

@Controller
public class PingController {
    @Get
    public HttpResponse<?> loggedIn() {
        return HttpResponse.ok("logged in");
    }

    @Secured(SecurityRule.IS_ANONYMOUS)
    @Get("login")
    public HttpResponse<?> redirectToOauthController() {
        return HttpResponse.redirect(URI.create("/oauth/login/auth0"));
    }
}
