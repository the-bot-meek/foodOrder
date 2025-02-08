package com.foodorder.server.client;

import com.foodorder.server.request.CreateOrderRequest;
import com.foodorder.models.models.Order;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.client.annotation.Client;
import jakarta.validation.Valid;

@Client("/order")
public interface OrderClient {
    @Post
    Order addOrder(@Valid @Body CreateOrderRequest createOrderRequest);
}
