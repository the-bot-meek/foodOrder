package com.thebotmeek.foodorder.server.models

import com.foodorder.server.models.Order
import spock.lang.Specification

import java.time.Instant

class OrderSpec extends Specification {
    def "test primary key sterilisation"() {
        given:
        Order order = new Order()

        when:
        order.setPrimaryKey("Order_101")

        then:
        order.getMeal().getId() == "101"
    }

    def "test primary key desterilisation"() {
        given:
        Order order = new Order()

        when:
        order.meal.setId("101")

        then:
        order.getPrimaryKey() == "Order_101"
    }

    def "test sort key desterilisation"() {
        given:
        Order order = new Order()

        when:
        order.meal.setMealDate(Instant.ofEpochSecond(1711394564))

        then:
        order.getSortKey() == "2024-03-25T19:22:44Z"
    }

    def "test sort key sterilisation"() {
        given:
        Order order = new Order()
        String sortKey = "2024-03-25T19:22:44Z"

        when:
        order.setSortKey(sortKey)

        then:
        order.getMeal().getMealDate() == Instant.ofEpochSecond(1711394564)
    }

    def "test GSIPrimaryKey sterilisation"() {
        given:
        Order order = new Order()
        String orderGSIPrimaryKey = "Order_c023346e-4e16-4fb0-a3df-b04dedf58307"

        when:
        order.setGSIPrimaryKey(orderGSIPrimaryKey)

        then:
        assert order.getUid() == "c023346e-4e16-4fb0-a3df-b04dedf58307"
    }

    def "test GSIPrimaryKey desterilisation"() {
        given:
        Order order = new Order()

        when:
        order.setUid("c023346e-4e16-4fb0-a3df-b04dedf58307")

        then:
        order.getGSIPrimaryKey() == "Order_c023346e-4e16-4fb0-a3df-b04dedf58307"
    }
}
