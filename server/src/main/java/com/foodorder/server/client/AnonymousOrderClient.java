package com.foodorder.server.client;

import com.foodorder.server.request.CreateOrderRequest;
import com.foodorder.server.models.AnonymousOrder;
import io.micronaut.http.annotation.*;
import io.micronaut.http.client.annotation.Client;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import java.util.Optional;

@Client("/anonymousOrder")
public interface AnonymousOrderClient {
    @Get("{uid}/{mealId}")
    Optional<AnonymousOrder> getAnonymousOrder(@PathVariable String uid, @PathVariable String mealId);

    @Post("{uid}")
    AnonymousOrder addAnonymousOrder(@Body CreateOrderRequest createOrderRequest, @PathVariable String uid);

    @Post("addBlankOrdersForMeal/{mealDate}/{mealId}")
    void addOrdersForMeal(@NotNull Instant mealDate, @NotNull String mealId);
}
