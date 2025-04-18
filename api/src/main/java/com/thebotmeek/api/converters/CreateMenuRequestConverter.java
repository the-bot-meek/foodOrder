package com.thebotmeek.api.converters;

import com.foodorder.server.exceptions.MenuRequestConverterException;
import com.foodorder.server.models.Menu;
import com.foodorder.server.repository.LocationRepository;
import com.thebotmeek.api.request.CreateMenuRequest;
import jakarta.inject.Singleton;

import java.util.UUID;

@Singleton
public class CreateMenuRequestConverter {
    private final LocationRepository locationRepository;
    public CreateMenuRequestConverter(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }
    public Menu convertCreateMenulRequestIntoMenu(CreateMenuRequest createMenuRequest) throws MenuRequestConverterException {
        if (!locationRepository.listLocation().contains(createMenuRequest.location())) {
            String msg = String.format("Invalid Location %s", createMenuRequest.location());
            throw new MenuRequestConverterException(msg);
        }
        final String id = UUID.randomUUID().toString();
        return new Menu(
                id,
                createMenuRequest.name(),
                createMenuRequest.location(),
                createMenuRequest.description(),
                createMenuRequest.menuItems(),
                createMenuRequest.phoneNumber()
        );
    }
}
