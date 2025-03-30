package com.foodorder.server.client;

import com.foodorder.server.models.Order;
import com.foodorder.server.request.CreateOrderRequest;
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
}
