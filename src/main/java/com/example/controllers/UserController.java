package com.example.controllers;


import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import java.security.Principal;

@Controller("user")
@Secured(SecurityRule.IS_AUTHENTICATED)
public class UserController {
    @Get
    public Principal getUserInfo(Principal principal) {
        return principal;
    }
}
