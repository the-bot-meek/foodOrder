package com.thebotmeek.api.converters;

import com.foodorder.server.exceptions.OrderRequestConverterException;
import com.foodorder.server.models.Menu;
import com.foodorder.server.models.MenuItem;
import com.foodorder.server.models.Order;
import com.foodorder.server.models.meal.Meal;
import com.foodorder.server.models.orderParticipant.AnonomusOrderParticipant;
import com.foodorder.server.models.orderParticipant.AuthenticatedOrderParticipant;
import com.foodorder.server.models.orderParticipant.OrderParticipant;
import com.foodorder.server.repository.MealRepository;
import com.foodorder.server.repository.MenuRepository;
import com.thebotmeek.api.request.CreateOrderRequest;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Singleton
public class CreateOrderRequestConverter {
    private final Logger log = LoggerFactory.getLogger(CreateOrderRequestConverter.class);
    private final MealRepository mealRepository;
    private final MenuRepository menuRepository;
    public CreateOrderRequestConverter(MealRepository mealRepository, MenuRepository menuRepository) {
        this.mealRepository = mealRepository;
        this.menuRepository = menuRepository;
    }
    private List<MenuItem> getInvalidMenuItemsForMenu(Set<MenuItem> menuItems, Menu menu) {
        return menuItems.stream().filter(menuItem -> !menu.getMenuItems().contains(menuItem)).toList();
    }

    public Order convertCreateOrderRequestToOrder(CreateOrderRequest createOrderRequest, String uid, String preferredUsername) throws OrderRequestConverterException {
        return convertCreateOrderRequestToOrder(createOrderRequest, uid, preferredUsername, false);
    }

    public Order convertCreateOrderRequestToOrder(CreateOrderRequest createOrderRequest, String uid, String preferredUsername, boolean anonymous) throws OrderRequestConverterException {
        final Optional<Meal> mealOptional = mealRepository.getMeal(
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

        if (meal.getMealConfig().getPrivateMealConfig() != null && !meal.getMealConfig().getPrivateMealConfig().getRecipientIds().contains(uid)) {
            String errMsg = String.format("Uid: %s is not in recipientIds list for Meal: %s", uid, meal.getId());
            throw new OrderRequestConverterException(errMsg);
        }

        final Optional<Menu> menuOptional = menuRepository.getMenu(
                meal.getLocation(),
                meal.getMenuName()
        );

        if (menuOptional.isEmpty()) {
            log.trace("Could not find Menu location: {}, name: {}", meal.getLocation(), meal.getMenuName());
            throw new OrderRequestConverterException(
                    String.format("Could not find Menu with location: %s, name: %s", mealOptional.get().getLocation(), mealOptional.get().getMenuName())
            );
        }
        Menu menu = menuOptional.get();

        List<MenuItem> invalidMenuItems = getInvalidMenuItemsForMenu(createOrderRequest.menuItems(), menu);

        if (!invalidMenuItems.isEmpty()) {
            log.trace("Found {} invalid invalidMenuItems for menu id: {}, location: {}, name: {}.",
                    invalidMenuItems.size(),
                    menu.getId(),
                    menu.getLocation(),
                    menu.getName()
            );
            throw new OrderRequestConverterException("Invalid MenuItems");
        }

        String orderId = UUID.randomUUID().toString();
        OrderParticipant orderParticipant = (anonymous)? new AnonomusOrderParticipant(preferredUsername, uid) : new AuthenticatedOrderParticipant(preferredUsername, uid);
        return new Order(orderId, meal, orderParticipant , createOrderRequest.menuItems(), false);
    }
}
