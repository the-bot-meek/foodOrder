package com.foodorder.server.client;

import com.foodorder.server.models.Order;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.client.annotation.Client;

import java.util.List;

@Client("/User")
public interface UserClient {
    @Get("/Order")
    List<Order> listOrders();
}
