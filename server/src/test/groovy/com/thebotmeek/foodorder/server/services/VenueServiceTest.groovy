package com.thebotmeek.foodorder.server.services

import com.foodorder.server.converters.CreateVenueRequestConverter
import com.foodorder.server.request.CreateVenueRequest
import com.foodorder.server.models.MenuItem
import com.foodorder.server.models.Venue
import com.foodorder.server.services.LocationService
import spock.lang.Specification

class VenueServiceTest extends Specification {
    def "ConvertMealRequestIntoMeal"() {
        given:
        String location = "London"
        String name = "name"
        String description = "description"
        Set<MenuItem> menuItems = Set.of(new MenuItem(name: "name", description: "description", price: 5.55))
        CreateVenueRequest createVenueRequest = new CreateVenueRequest(menuItems, location, name, description)
        LocationService locationService = Mock(LocationService)
        locationService.listLocation() >> ["London"]
        CreateVenueRequestConverter createVenueRequestConverter = new CreateVenueRequestConverter(locationService)

        when:
        Venue venue = createVenueRequestConverter.convertCreateVenuelRequestIntoVenue(createVenueRequest)

        then:
        venue.with {
            assert it.menuItems == menuItems
            assert it.name == name
            assert it.description == description
            assert it.id != null
        }
    }
}
