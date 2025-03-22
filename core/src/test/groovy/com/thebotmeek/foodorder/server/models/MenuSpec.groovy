package com.thebotmeek.foodorder.server.models


import com.foodorder.server.models.Menu
import spock.lang.Specification

class MenuSpec extends Specification {
    def "test pk serialization"() {
        given:
        Menu menu = new Menu()

        when:
        menu.setLocation("location")

        then:
        menu.getPrimaryKey() == "Menu_location"
    }

    def "test pk deserialization"() {
        given:
        String pk = "Menu_location"
        Menu menu = new Menu()

        when:
        menu.setPrimaryKey(pk)

        then:
        menu.getLocation() == "location"
    }
}
