package com.thebotmeek.api.client;

import com.foodorder.server.models.Order;
import com.thebotmeek.api.request.CreateOrderRequest;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.client.annotation.Client;
import jakarta.validation.Valid;

@Client("/order")
public interface OrderClient {
    @Post
    Order addOrder(@Valid @Body CreateOrderRequest createOrderRequest);
}
