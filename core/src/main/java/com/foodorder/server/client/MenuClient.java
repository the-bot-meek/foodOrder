package com.foodorder.server.client;

import com.foodorder.server.models.Menu;
import com.foodorder.server.request.CreateMenuRequest;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.client.annotation.Client;

import java.util.List;

@Client("/menu")
public interface MenuClient {
    @Get("/{location}/{name}")
    Menu fetchMenu(String location, String name);
    @Get("/{location}")
    List<Menu> listMenusForLocation(String location);
    @Post
    Menu addMenu(@Body CreateMenuRequest createMenuRequest);
}
