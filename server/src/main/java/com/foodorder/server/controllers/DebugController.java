package com.foodorder.server.controllers;

import com.foodorder.server.services.DynamoDBEnhancedFacadeService;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;

import java.util.Map;

@Controller
@Secured({SecurityRule.IS_ANONYMOUS, SecurityRule.IS_AUTHENTICATED})
public class DebugController {
    final Map<String, DynamoDBEnhancedFacadeService> dynamoDBEnhancedFacadeService;
    DebugController(Map<String, DynamoDBEnhancedFacadeService> enhancedFacadeServiceMap) {
        this.dynamoDBEnhancedFacadeService = enhancedFacadeServiceMap;
    }

    @Get
    public Map<String, DynamoDBEnhancedFacadeService> debug() {
        return dynamoDBEnhancedFacadeService;
    }
}
