package com.foodorder.server.client;

import com.foodorder.models.models.Order;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.client.annotation.Client;

import java.util.List;

@Client("/user")
public interface UserClient {
    @Get("/order")
    List<Order> listOrders();
}
