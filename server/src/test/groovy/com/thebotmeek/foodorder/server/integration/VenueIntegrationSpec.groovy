package com.thebotmeek.foodorder.server.integration

import com.foodorder.server.client.VenueClient
import com.foodorder.server.request.CreateVenueRequest
import com.foodorder.server.models.MenuItem
import com.foodorder.server.models.Venue
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.IgnoreIf
import spock.lang.Specification

@MicronautTest(environments = ["mock_auth"])
@IgnoreIf({System.getenv("requireIntegrationTests") != 'true'})
class VenueIntegrationSpec extends Specification {
    @Inject
    VenueClient venueClient
    Set<MenuItem> menuItems
    CreateVenueRequest createVenueRequest

    def "setup"() {
        menuItems = [new MenuItem(name: "name", description: "description", price: 1.01)]
        createVenueRequest = new CreateVenueRequest(menuItems, "London", UUID.randomUUID().toString(), "description")
    }

    def "Add Venue"() {
        when:
        Venue venue = venueClient.addVenue(createVenueRequest)

        then:
        assert venue.name == createVenueRequest.name()
        assert venue.location == createVenueRequest.location()
        assert venue.description == createVenueRequest.description()
        assert venue.menuItems == menuItems
        assert venue.id != null
    }

    def "Get venue"() {
        when:
        Venue createVenueResp = venueClient.addVenue(createVenueRequest)
        Venue venue = venueClient.fetchVenue(createVenueRequest.location(), createVenueRequest.name())

        then:
        assert createVenueResp == venue
    }

    def "list all venues for location"() {
        when:
        venueClient.addVenue(createVenueRequest)
        List<Venue> venueList = venueClient.listVenuesForLocation("London")

        then:
        assert !venueList.isEmpty()
        assert venueList.every {Venue venue -> venue.location == "London"}
    }
}
