package com.example.controllers

import com.example.converters.CreateOrderRequestConverter
import com.example.dto.request.CreateOrderRequest
import com.example.models.AnonymousOrder
import com.example.models.MenuItem
import com.example.models.Order
import com.example.services.OrderService
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import spock.lang.Specification

import java.time.Instant

class AnonymousOrderControllerSpec extends Specification {
    def "test add Order"() {
        given:
        OrderService orderService = Mock(OrderService)
        CreateOrderRequestConverter createOrderRequestConverter = Mock(CreateOrderRequestConverter)

        AnonymousOrderController anonymousOrderController = new AnonymousOrderController(orderService, null, createOrderRequestConverter)

        Set<MenuItem> menuItems = [new MenuItem(name: ",", description: "", price: 0)]
        String uid = "79fe4688-604d-4301-9025-e5ff0c8c50c3"
        CreateOrderRequest createOrderRequest = new CreateOrderRequest(Instant.ofEpochSecond(5), "535823f0-1707-4734-8e18-21ac20350b96", menuItems, "1707-535823f0-4734-21ac20350b96-8e18")

        when:
        HttpResponse<AnonymousOrder> resp =  anonymousOrderController.addAnonymousOrder(createOrderRequest, uid)

        then:
        assert resp.status() == HttpStatus.OK
        1 * orderService.addOrder(_ as Order)
        1 * createOrderRequestConverter.convertCreateOrderRequestToOrder(createOrderRequest, uid, AnonymousOrderController.DEFAULT_AnonymousUser_NAME, true) >> new AnonymousOrder()
    }
}
