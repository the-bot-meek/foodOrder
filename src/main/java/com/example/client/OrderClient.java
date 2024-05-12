package com.example.client;

import com.example.dto.request.CreateOrderRequest;
import com.example.models.Order;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Put;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.security.authentication.Authentication;
import jakarta.validation.Valid;

@Client("/order")
public interface OrderClient {
    @Put
    public Order addOrder(@Valid @Body CreateOrderRequest createOrderRequest);
}
