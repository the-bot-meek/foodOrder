package com.example.controllers;


import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.Principal;
import java.util.Map;

@Controller("user")
@Secured(SecurityRule.IS_AUTHENTICATED)
public class UserController {
    private final Logger log = LoggerFactory.getLogger(UserController.class);
    @Get
    public Map<String,?> getUserInfo(Principal principal) {
        log.info("Getting UserInfo. uid: {}", principal.getName());
        return Map.of("name", principal.getName());
    }
}
