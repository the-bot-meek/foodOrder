package com.thebotmeek.foodorder.server.services

import com.foodorder.server.converters.CreateMenuRequestConverter
import com.foodorder.server.request.CreateMenuRequest
import com.foodorder.server.models.MenuItem
import com.foodorder.server.models.Menu
import com.foodorder.server.repository.LocationRepository
import spock.lang.Specification

class MenuRepositoryTest extends Specification {
    def "ConvertMealRequestIntoMeal"() {
        given:
        String location = "London"
        String name = "name"
        String description = "description"
        String phoneNumber = '+44 20 7123 4567'
        Set<MenuItem> menuItems = Set.of(new MenuItem(name: "name", description: "description", price: 5.55))
        CreateMenuRequest createMenuRequest = new CreateMenuRequest(menuItems, location, name, description, phoneNumber)
        LocationRepository locationService = Mock(LocationRepository)
        locationService.listLocation() >> ["London"]
        CreateMenuRequestConverter createMenuRequestConverter = new CreateMenuRequestConverter(locationService)

        when:
        Menu menu = createMenuRequestConverter.convertCreateMenulRequestIntoMenu(createMenuRequest)

        then:
        menu.with {
            assert it.menuItems == menuItems
            assert it.name == name
            assert it.description == description
            assert it.phoneNumber == phoneNumber
            assert it.id != null
        }
    }
}
