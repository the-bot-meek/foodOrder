package com.example.services

import com.example.dto.CreateVenueRequest
import com.example.models.MenuItem
import com.example.models.Venue
import spock.lang.Specification

class VenueServiceTest extends Specification {
    def "ConvertMealRequestIntoMeal"() {
        given:
        String location = "London"
        String name = "name"
        String description = "description"
        Set<MenuItem> menuItems = Set.of(new MenuItem(name: "name", description: "description", price: 5.55))
        CreateVenueRequest createVenueRequest = new CreateVenueRequest(menuItems, location, name, description)

        when:
        Venue venue = VenueService.convertMealRequestIntoMeal(createVenueRequest)

        then:
        venue.with {
            assert it.menuItems == menuItems
            assert it.name == name
            assert it.description == description
            assert it.id != null
        }
    }
}
