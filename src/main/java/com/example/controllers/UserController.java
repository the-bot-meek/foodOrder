package com.example.controllers;


import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import java.security.Principal;
import java.util.Map;

@Controller("user")
@Secured(SecurityRule.IS_AUTHENTICATED)
public class UserController {
    @Get
    public Map<String,?> getUserInfo(Principal principal) {
        return Map.of("name", principal.getName());
    }
}
