package com.example.client;

import com.example.dto.request.CreateOrderRequest;
import com.example.models.Order;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.client.annotation.Client;
import jakarta.validation.Valid;

@Client("/order")
public interface OrderClient {
    @Post
    Order addOrder(@Valid @Body CreateOrderRequest createOrderRequest);
}
