package com.example.services;

import jakarta.inject.Singleton;

import java.util.List;

@Singleton
public class LocationService {
    public List<String> listLocation() {
        return List.of("Kirkwall", "London");
    }
}
