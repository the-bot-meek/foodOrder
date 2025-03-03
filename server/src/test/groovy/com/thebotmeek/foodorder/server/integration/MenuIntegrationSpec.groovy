package com.thebotmeek.foodorder.server.integration

import com.foodorder.server.client.MenuClient
import com.foodorder.server.request.CreateMenuRequest
import com.foodorder.server.models.MenuItem
import com.foodorder.server.models.Menu
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.IgnoreIf
import spock.lang.Specification

@MicronautTest(environments = ["mock_auth"])
@IgnoreIf({System.getenv("requireIntegrationTests") != 'true'})
class MenuIntegrationSpec extends Specification {
    @Inject
    MenuClient menuClient
    Set<MenuItem> menuItems
    CreateMenuRequest createMenuRequest

    def "setup"() {
        menuItems = [new MenuItem(name: "name", description: "description", price: 1.01)]
        createMenuRequest = new CreateMenuRequest(menuItems, "London", UUID.randomUUID().toString(), "description", "+44 20 7123 4567")
    }

    def "Add Menu"() {
        when:
        Menu menu = menuClient.addMenu(createMenuRequest)

        then:
        assert menu.name == createMenuRequest.name()
        assert menu.location == createMenuRequest.location()
        assert menu.description == createMenuRequest.description()
        assert menu.menuItems == menuItems
        assert menu.id != null
    }

    def "Get menu"() {
        when:
        Menu createMenuResp = menuClient.addMenu(createMenuRequest)
        Menu menu = menuClient.fetchMenu(createMenuRequest.location(), createMenuRequest.name())

        then:
        assert createMenuResp == menu
    }

    def "list all menus for location"() {
        when:
        menuClient.addMenu(createMenuRequest)
        List<Menu> menuList = menuClient.listMenusForLocation("London")

        then:
        assert !menuList.isEmpty()
        assert menuList.every { Menu menu -> menu.location == "London"}
    }
}
