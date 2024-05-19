package com.example.client;

import com.example.dto.request.CreateVenueRequest;
import com.example.models.Venue.Venue;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Put;
import io.micronaut.http.client.annotation.Client;

import java.util.List;

@Client("/venue")
public interface VenueClient {
    @Get("/{location}/{name}")
    Venue fetchVenue(String location, String name);
    @Get("/{location}")
    List<Venue> listVenuesForLocation(String location);
    @Put
    Venue addVenue(@Body CreateVenueRequest createVenueRequest);
}
