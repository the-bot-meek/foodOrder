package com.foodorder.server.controllers;

import com.foodorder.server.models.Order;
import com.foodorder.server.services.OrderService;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.rules.SecurityRule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    public List<Order> listOrders(Authentication authentication) {
        log.info("Getting all Orders for uid: {}", authentication.getName());
        return orderService.listOrdersFromUserID(authentication.getName());
    }
}
