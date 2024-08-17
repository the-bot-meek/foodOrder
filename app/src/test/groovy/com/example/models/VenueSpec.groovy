package com.example.models

import com.example.services.VenueService
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
