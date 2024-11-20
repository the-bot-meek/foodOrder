package com.foodorder.server.controllers;

import com.foodorder.server.converters.CreateVenueRequestConverter;
import com.foodorder.server.exceptions.VenueRequestConverterException;
import com.foodorder.server.request.CreateVenueRequest;
import com.foodorder.server.models.Venue;
import com.foodorder.server.services.VenueService;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

@Controller("venue")
@Secured(SecurityRule.IS_AUTHENTICATED)
public class VenueController {
    final private Logger log = LoggerFactory.getLogger(VenueController.class);
    final private VenueService venueService;
    final private CreateVenueRequestConverter createVenueRequestConverter;
    VenueController(VenueService venueService, CreateVenueRequestConverter createVenueRequestConverter) {
        this.venueService = venueService;
        this.createVenueRequestConverter = createVenueRequestConverter;
    }

    @Get("/{location}/{name}")
    public Optional<Venue> getVenue(String location, String name) {
        log.info("Getting venue. location: {}, name: {}", location, name);
        return venueService.getVenue(location, name);
    }

    @Get("/{location}")
    public List<Venue> listVenuesForLocation(String location) {
        log.info("Getting all Venues for location: {}", location);
        return venueService.listVenues(location);
    }

    @Post
    public HttpResponse<Venue> addVenue(@Body CreateVenueRequest createVenueRequest) {
        log.info("Adding Venue. createVenueRequest: {}", createVenueRequest);
        try {
            Venue venue = createVenueRequestConverter.convertCreateVenuelRequestIntoVenue(createVenueRequest);
            venueService.addVenue(venue);
            return HttpResponse.ok(venue);
        } catch (VenueRequestConverterException e) {
            log.error("Error converting CreateVenueRequest to Venue", e);
            return HttpResponse.badRequest();
        }
    }
}
