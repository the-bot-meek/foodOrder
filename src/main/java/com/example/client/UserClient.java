package com.example.client;

import com.example.models.Order.Order;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.client.annotation.Client;

import java.util.List;

@Client("/User")
public interface UserClient {
    @Get("/Order")
    List<Order> listOrders();
}
