package com.example.client;

import com.example.dto.request.CreateVenueRequest;
import com.example.models.Venue;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.client.annotation.Client;

import java.util.List;

@Client("/venue")
public interface VenueClient {
    @Get("/{location}/{name}")
    Venue fetchVenue(String location, String name);
    @Get("/{location}")
    List<Venue> listVenuesForLocation(String location);
    @Post
    Venue addVenue(@Body CreateVenueRequest createVenueRequest);
}
