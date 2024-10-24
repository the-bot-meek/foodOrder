package com.example.Integration

import com.example.client.VenueClient
import com.example.dto.request.CreateVenueRequest
import com.example.models.MenuItem
import com.example.models.Venue
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.IgnoreIf
import spock.lang.Specification

@MicronautTest(environments = ["mock_auth"])
@IgnoreIf({System.getenv("requireIntegrationTests") != 'true'})
class VenueIntegrationSpec extends Specification {
    @Inject
    VenueClient venueClient

    def "Add Venue"() {
        given:
        Set<MenuItem> menuItems = [new MenuItem(name: "name", description: "description", price: 1.01)]
        CreateVenueRequest createVenueRequest = new CreateVenueRequest(menuItems, "London", "name", "description")

        when:
        Venue venue = venueClient.addVenue(createVenueRequest)

        then:
        assert venue.name == "name"
        assert venue.location == "London"
        assert venue.description == "description"
        assert venue.menuItems == menuItems
        assert venue.id != null
    }

    def "Get venue"() {
        given:
        Set<MenuItem> menuItems = [new MenuItem(name: "name", description: "description", price: 1.01)]
        CreateVenueRequest createVenueRequest = new CreateVenueRequest(menuItems, "London", "name", "description")

        when:
        Venue createVenueResp = venueClient.addVenue(createVenueRequest)
        Venue venue = venueClient.fetchVenue(createVenueRequest.location(), createVenueRequest.name())

        then:
        assert createVenueResp == venue
    }

    def "list all venues for location"() {
        given:
        Set<MenuItem> menuItems = [new MenuItem(name: "name", description: "description", price: 1.01)]
        CreateVenueRequest createVenueRequest = new CreateVenueRequest(menuItems, "London", "name", "description")

        when:
        venueClient.addVenue(createVenueRequest)
        List<Venue> venueList = venueClient.listVenuesForLocation("London")

        then:
        assert !venueList.isEmpty()
        assert venueList.every {Venue venue -> venue.location == "London"}
    }
}
