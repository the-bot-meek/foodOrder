package com.example.controllers;

import com.example.dto.request.CreateVenueRequest;
import com.example.models.Venue;
import com.example.services.VenueService;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Put;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import java.util.List;
import java.util.Optional;

@Controller("venue")
@Secured(SecurityRule.IS_AUTHENTICATED)
public class VenueController {
    final private VenueService venueService;
    VenueController(VenueService venueService) {
        this.venueService = venueService;
    }

    @Get("/{location}/{name}")
    public Optional<Venue> getVenue(String location, String name) {
        return venueService.getValue(location, name);
    }

    @Get("/{location}")
    public List<Venue> listVenuesForLocation(String location) {
        return venueService.listVenues(location);
    }

    @Put
    public Venue addVenue(@Body CreateVenueRequest createVenueRequest) {
        return venueService.addVenue(createVenueRequest);
    }
}
