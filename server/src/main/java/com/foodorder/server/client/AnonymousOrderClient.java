package com.foodorder.server.client;

import com.foodorder.server.request.CreateOrderRequest;
import com.foodorder.models.models.AnonymousOrder;
import io.micronaut.http.annotation.*;
import io.micronaut.http.client.annotation.Client;

import java.util.Optional;

@Client("/anonymousOrder")
public interface AnonymousOrderClient {
    @Get("{uid}/{mealId}")
    Optional<AnonymousOrder> getAnonymousOrder(@PathVariable String uid, @PathVariable String mealId);

    @Post("{uid}")
    AnonymousOrder addAnonymousOrder(@Body CreateOrderRequest createOrderRequest, @PathVariable String uid);
}
