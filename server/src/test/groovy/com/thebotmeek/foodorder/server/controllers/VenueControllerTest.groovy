package com.thebotmeek.foodorder.server.controllers

import com.foodorder.server.controllers.VenueController
import com.foodorder.server.converters.CreateVenueRequestConverter
import com.foodorder.server.request.CreateVenueRequest
import com.foodorder.server.models.MenuItem
import com.foodorder.server.models.Venue
import com.foodorder.server.services.IDynamoDBFacadeService
import com.foodorder.server.services.LocationService
import com.foodorder.server.services.VenueService
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import spock.lang.Specification

class VenueControllerTest extends Specification {
    String venueId
    String venueName
    String location
    String description
    Set<MenuItem> menuItems
    String phoneNumber;
    IDynamoDBFacadeService dynamoDBFacadeService
    LocationService locationService
    VenueService venueService
    VenueController venueController


    private boolean isEql(Venue venue) {
         venue.with {
            assert it.name == venueName
            assert it.location == location
            assert it.description == description
            assert it.menuItems == menuItems
            assert it.id != null
        }
        return true
    }

    def setup() {
        venueId = "d84e61c73fe7-de8f-47ed-833a-797b001f"
        venueName = "Prep"
        location = "London"
        description = "description"
        phoneNumber = "+44 20 7123 4567"
        menuItems = Set.of(new MenuItem(name: "name", description: "description", price: 5.55))
        dynamoDBFacadeService = Mock(IDynamoDBFacadeService)
        locationService = new LocationService()
        venueService = new VenueService(dynamoDBFacadeService)
        CreateVenueRequestConverter createVenueRequestConverter = new CreateVenueRequestConverter(locationService)
        venueController = new VenueController(venueService, createVenueRequestConverter)
    }

    def "GetVenue"() {
        when:
        Optional<Venue> venue = venueController.getVenue("London", "Prep")

        then:
        1 * dynamoDBFacadeService.load(Venue, "Venue_London", "Prep") >> { Optional.of(
                new Venue(id: venueId, name: venueName, location: location, description: description, menuItems: menuItems)
        )
        }
        assert venue.isPresent()
        isEql(venue.get())
    }

    def "ListVenuesForLocation"() {
        when:
        List<Venue> venueList = venueController.listVenuesForLocation(location)

        then:
        1 * dynamoDBFacadeService.query(Venue.class, _) >> {
            return List.of(new Venue(id: venueId, name: venueName, location: location, description: "description", menuItems: menuItems))
        }
        isEql(venueList.get(0))
    }

    def "AddVenue - valid"() {
        given:
        CreateVenueRequest createVenueRequest = new CreateVenueRequest(menuItems, location, venueName, description, phoneNumber)

        IDynamoDBFacadeService dynamoDBFacadeService = Mock(IDynamoDBFacadeService)
        LocationService locationService = new LocationService()
        VenueService venueService = new VenueService(dynamoDBFacadeService)
        CreateVenueRequestConverter createVenueRequestConverter = new CreateVenueRequestConverter(locationService)
        VenueController venueController = new VenueController(venueService, createVenueRequestConverter)

        when:
        Venue venue =  venueController.addVenue(createVenueRequest).body()

        then:
        1 * dynamoDBFacadeService.save(_) >> { Venue v -> isEql(v)}
        isEql(venue)
    }

    def "AddVenue - invalid location"() {
        given:
        location = "Not London"
        CreateVenueRequest createVenueRequest = new CreateVenueRequest(menuItems, location, venueName, description, phoneNumber)

        IDynamoDBFacadeService dynamoDBFacadeService = Mock(IDynamoDBFacadeService)
        LocationService locationService = new LocationService()
        VenueService venueService = new VenueService(dynamoDBFacadeService)
        CreateVenueRequestConverter createVenueRequestConverter = new CreateVenueRequestConverter(locationService)
        VenueController venueController = new VenueController(venueService, createVenueRequestConverter)

        when:
        HttpResponse<Venue> venueHttpResponse = venueController.addVenue(createVenueRequest)

        then:
        venueHttpResponse.status() == HttpStatus.BAD_REQUEST;
    }
}
