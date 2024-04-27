package com.example.services

import com.example.dto.request.CreateOrderRequest
import com.example.models.Order
import io.micronaut.security.authentication.Authentication
import spock.lang.Specification

import java.time.Instant

class OrderServiceTest extends Specification {
    def "ConvertCreateOrderRequestToOrder"() {
        given:
        String mealId = "797b001f-de8f-47ed-833a-d84e61c73fe7"
        Instant dateOfMeal = Instant.ofEpochSecond(1711487392)
        String uid = "d84e61c73fe7-de8f-47ed-833a-797b001f"
        CreateOrderRequest createOrderRequest = new CreateOrderRequest(dateOfMeal, mealId)
        Authentication authentication = Mock(Authentication)
        authentication.getAttributes() >> Map.of("preferred_username", "usename")
        authentication.getName() >> uid

        when:
        Order order = OrderService.convertCreateOrderRequestToOrder(createOrderRequest, authentication)

        then:
        order == new Order(mealId: mealId, dateOfMeal: dateOfMeal, uid: uid)
    }
}
