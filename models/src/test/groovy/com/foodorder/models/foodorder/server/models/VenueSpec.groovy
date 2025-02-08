package com.foodorder.models.foodorder.server.models

import com.foodorder.models.models.Venue
import spock.lang.Specification

class VenueSpec extends Specification {
    def "test pk serialization"() {
        given:
        Venue venue = new Venue()

        when:
        venue.setLocation("location")

        then:
        venue.getPrimaryKey() == "Venue_location"
    }

    def "test pk deserialization"() {
        given:
        String pk = "Venue_location"
        Venue venue = new Venue()

        when:
        venue.setPrimaryKey(pk)

        then:
        venue.getLocation() == "location"
    }
}
