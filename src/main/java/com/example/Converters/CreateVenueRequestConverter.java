package com.example.Converters;

import com.example.Exceptions.VenueRequestConverterException;
import com.example.dto.request.CreateVenueRequest;
import com.example.models.Venue;
import com.example.services.LocationService;
import jakarta.inject.Singleton;

import java.util.UUID;

@Singleton
public class CreateVenueRequestConverter {
    private final LocationService locationService;
    public CreateVenueRequestConverter(LocationService locationService) {
        this.locationService = locationService;
    }
    public Venue convertCreateVenuelRequestIntoVenue(CreateVenueRequest createVenueRequest) throws VenueRequestConverterException {
        if (!locationService.listLocation().contains(createVenueRequest.location())) {
            String msg = String.format("Invalid Location %s", createVenueRequest.location());
            throw new VenueRequestConverterException(msg);
        }
        final String id = UUID.randomUUID().toString();
        return new Venue(
                id,
                createVenueRequest.name(),
                createVenueRequest.location(),
                createVenueRequest.description(),
                createVenueRequest.menuItems()
        );
    }
}
