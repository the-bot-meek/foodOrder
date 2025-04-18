package com.thebotmeek.api.client;

import com.foodorder.server.models.Order;
import com.thebotmeek.api.request.CreateOrderRequest;
import io.micronaut.http.annotation.*;
import io.micronaut.http.client.annotation.Client;

import java.util.Optional;

@Client("/anonymousOrder")
public interface AnonymousOrderClient {
    @Get("{uid}/{mealId}")
    Optional<Order> getAnonymousOrder(@PathVariable String uid, @PathVariable String mealId);

    @Post("{uid}")
    Order addAnonymousOrder(@Body CreateOrderRequest createOrderRequest, @PathVariable String uid);

    @Put("{uid}")
    Order updateAnonymousOrder(String uid, @Body Order order);

    @Post("addBlankOrdersForMeal/{mealSortKey}")
    void addAnonymousOrdersForMeal(@PathVariable String mealSortKey);
}
