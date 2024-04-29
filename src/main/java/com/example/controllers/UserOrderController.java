package com.example.controllers;

import com.example.models.Order;
import com.example.services.OrderService;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.Principal;
import java.util.List;

@Controller("user")
@Secured(SecurityRule.IS_AUTHENTICATED)
public class UserOrderController {
    private final OrderService orderService;
    private final Logger log = LoggerFactory.getLogger(UserOrderController.class);
    UserOrderController(OrderService orderService) {
        this.orderService = orderService;
    }
    @Get("order")
    public List<Order> listOrders(Principal principal) {
        log.info("Getting all Orders for uid: {}", principal.getName());
        return orderService.listOrdersFromUserID(principal.getName());
    }
}
