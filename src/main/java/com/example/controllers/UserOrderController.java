package com.example.controllers;

import com.example.models.Order;
import com.example.services.OrderService;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;

import java.security.Principal;
import java.util.List;

@Controller("user")
@Secured(SecurityRule.IS_AUTHENTICATED)
public class UserOrderController {
    private final OrderService orderService;
    UserOrderController(OrderService orderService) {
        this.orderService = orderService;
    }
    @Get("order")
    public List<Order> listOrders(Principal principal) {
        return orderService.listOrdersFromUserID(principal.getName());
    }
}
