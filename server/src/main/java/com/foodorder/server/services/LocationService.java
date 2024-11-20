package com.foodorder.server.services;

import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Singleton
public class LocationService {
    private final Logger log = LoggerFactory.getLogger(LocationService.class);
    public List<String> listLocation() {
        log.trace("Getting all available locations");
        return List.of("Kirkwall", "London");
    }
}
