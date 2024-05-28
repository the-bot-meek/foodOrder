package com.example.Converters;

import com.example.Exceptions.OrderRequestConverterException;
import com.example.dto.request.CreateOrderRequest;
import com.example.models.Meal.Meal;
import com.example.models.Meal.PrivateMealConfig;
import com.example.models.MenuItem;
import com.example.models.Order;
import com.example.models.Venue;
import com.example.services.MealService;
import com.example.services.VenueService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public class CreateOrderRequestConverter {
    private final Logger log = LoggerFactory.getLogger(CreateOrderRequestConverter.class);
    private final MealService mealService;
    private final VenueService venueService;
    public CreateOrderRequestConverter(MealService mealService, VenueService venueService) {
        this.mealService = mealService;
        this.venueService = venueService;
    }
    private List<MenuItem> getInvalidMenuItemsForVenue(Set<MenuItem> menuItems, Venue venue) {
        return menuItems.stream().filter(menuItem -> !venue.getMenuItems().contains(menuItem)).toList();
    }
    public Order convertCreateOrderRequestToOrder(CreateOrderRequest createOrderRequest, String uid, String preferredUsername) throws OrderRequestConverterException {
        final Optional<Meal> mealOptional = mealService.getMeal(
                createOrderRequest.organizerUid(),
                createOrderRequest.dateOfMeal(),
                createOrderRequest.mealId()
        );

        if (mealOptional.isEmpty()) {
            log.trace("Could not find Meal organizerUid: {}, dateOfMeal: {}, mealId: {}.",
                    createOrderRequest.organizerUid(),
                    createOrderRequest.dateOfMeal(),
                    createOrderRequest.mealId()
            );
            throw new OrderRequestConverterException(
                    String.format("Could not find Meal organizerUid: %s, dateOfMeal: %s, mealId: %s.", createOrderRequest.organizerUid(),
                            createOrderRequest.dateOfMeal(),
                            createOrderRequest.mealId()
                    )
            );
        }

        Meal meal = mealOptional.get();

        if (meal.getMealConfig() instanceof PrivateMealConfig && !((PrivateMealConfig) meal.getMealConfig()).getRecipientIds().contains(uid)) {
            String errMsg = String.format("Uid: %s is not in recipientIds list for Meal: %s", uid, meal.getId());
            throw new OrderRequestConverterException(errMsg);
        }

        final Optional<Venue> venueOptional = venueService.getVenue(
                meal.getLocation(),
                meal.getVenueName()
        );

        if (venueOptional.isEmpty()) {
            log.trace("Could not find Venue location: {}, name: {}", meal.getLocation(), meal.getVenueName());
            throw new OrderRequestConverterException(
                    String.format("Could not find Venue with location: %s, name: %s", mealOptional.get().getLocation(), mealOptional.get().getVenueName())
            );
        }
        Venue venue = venueOptional.get();

        List<MenuItem> invalidMenuItems = getInvalidMenuItemsForVenue(createOrderRequest.menuItems(), venue);

        if (!invalidMenuItems.isEmpty()) {
            log.trace("Found {} invalid invalidMenuItems for venue id: {}, location: {}, name: {}.",
                    invalidMenuItems.size(),
                    venue.getId(),
                    venue.getLocation(),
                    venue.getName()
            );
            throw new OrderRequestConverterException("Invalid MenuItems");
        }

        String orderId = UUID.randomUUID().toString();
        return new Order(
                orderId,
                meal,
                uid,
                preferredUsername,
                createOrderRequest.menuItems()
        );
    }
}
