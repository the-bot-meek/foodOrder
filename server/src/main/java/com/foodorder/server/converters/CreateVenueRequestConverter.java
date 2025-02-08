package com.foodorder.server.converters;

import com.foodorder.server.exceptions.VenueRequestConverterException;
import com.foodorder.server.request.CreateVenueRequest;
import com.foodorder.models.models.Venue;
import com.foodorder.server.repository.LocationRepository;
import jakarta.inject.Singleton;

import java.util.UUID;

@Singleton
public class CreateVenueRequestConverter {
    private final LocationRepository locationRepository;
    public CreateVenueRequestConverter(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }
    public Venue convertCreateVenuelRequestIntoVenue(CreateVenueRequest createVenueRequest) throws VenueRequestConverterException {
        if (!locationRepository.listLocation().contains(createVenueRequest.location())) {
            String msg = String.format("Invalid Location %s", createVenueRequest.location());
            throw new VenueRequestConverterException(msg);
        }
        final String id = UUID.randomUUID().toString();
        return new Venue(
                id,
                createVenueRequest.name(),
                createVenueRequest.location(),
                createVenueRequest.description(),
                createVenueRequest.menuItems(),
                createVenueRequest.phoneNumber()
        );
    }
}
