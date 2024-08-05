package com.example.client;

import com.example.dto.request.CreateOrderRequest;
import com.example.models.AnonymousOrder;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.Put;
import io.micronaut.http.client.annotation.Client;

import java.time.Instant;
import java.util.Optional;

@Client("/AnonymousOrder")
public interface AnonymousOrderClient {
    @Get("{uid}/{mealId}")
    Optional<AnonymousOrder> getAnonymousOrder(@PathVariable String uid, @PathVariable String mealId);

    @Put("{uid}")
    AnonymousOrder addAnonymousOrder(@Body CreateOrderRequest createOrderRequest, @PathVariable String uid);
}
