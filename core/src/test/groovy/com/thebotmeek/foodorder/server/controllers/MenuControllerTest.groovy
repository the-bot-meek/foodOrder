package com.thebotmeek.foodorder.server.controllers

import com.foodorder.server.controllers.MenuController
import com.foodorder.server.converters.CreateMenuRequestConverter
import com.foodorder.server.request.CreateMenuRequest
import com.foodorder.server.models.MenuItem
import com.foodorder.server.models.Menu
import com.foodorder.server.repository.IDynamoDBFacadeRepository
import com.foodorder.server.repository.LocationRepository
import com.foodorder.server.repository.MenuRepository
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import spock.lang.Specification

class MenuControllerTest extends Specification {
    String menuId
    String menuName
    String location
    String description
    Set<MenuItem> menuItems
    String phoneNumber
    IDynamoDBFacadeRepository dynamoDBFacadeService
    LocationRepository locationService
    MenuRepository menuService
    MenuController menuController


    private boolean isEql(Menu menu) {
         menu.with {
            assert it.name == menuName
            assert it.location == location
            assert it.description == description
            assert it.menuItems == menuItems
            assert it.id != null
        }
        return true
    }

    def setup() {
        menuId = "d84e61c73fe7-de8f-47ed-833a-797b001f"
        menuName = "Prep"
        location = "London"
        description = "description"
        phoneNumber = "+44 20 7123 4567"
        menuItems = Set.of(new MenuItem(name: "name", description: "description", price: 5.55))
        dynamoDBFacadeService = Mock(IDynamoDBFacadeRepository)
        locationService = new LocationRepository()
        menuService = new MenuRepository(dynamoDBFacadeService)
        CreateMenuRequestConverter createMenuRequestConverter = new CreateMenuRequestConverter(locationService)
        menuController = new MenuController(menuService, createMenuRequestConverter)
    }

    def "GetMenu"() {
        when:
        Optional<Menu> menu = menuController.getMenu("London", "Prep")

        then:
        1 * dynamoDBFacadeService.load(Menu, "Menu_London", "Prep") >> { Optional.of(
                new Menu(id: menuId, name: menuName, location: location, description: description, menuItems: menuItems)
        )
        }
        assert menu.isPresent()
        isEql(menu.get())
    }

    def "ListMenusForLocation"() {
        when:
        List<Menu> menuList = menuController.listMenusForLocation(location)

        then:
        1 * dynamoDBFacadeService.query(Menu.class, _) >> {
            return List.of(new Menu(id: menuId, name: menuName, location: location, description: "description", menuItems: menuItems))
        }
        isEql(menuList.get(0))
    }

    def "AddMenu - valid"() {
        given:
        CreateMenuRequest createMenuRequest = new CreateMenuRequest(menuItems, location, menuName, description, phoneNumber)

        IDynamoDBFacadeRepository dynamoDBFacadeService = Mock(IDynamoDBFacadeRepository)
        LocationRepository locationService = new LocationRepository()
        MenuRepository menuService = new MenuRepository(dynamoDBFacadeService)
        CreateMenuRequestConverter createMenuRequestConverter = new CreateMenuRequestConverter(locationService)
        MenuController menuController = new MenuController(menuService, createMenuRequestConverter)

        when:
        Menu menu =  menuController.addMenu(createMenuRequest).body()

        then:
        1 * dynamoDBFacadeService.save(_) >> { Menu v -> isEql(v)}
        isEql(menu)
    }

    def "AddMenu - invalid location"() {
        given:
        location = "Not London"
        CreateMenuRequest createMenuRequest = new CreateMenuRequest(menuItems, location, menuName, description, phoneNumber)

        IDynamoDBFacadeRepository dynamoDBFacadeService = Mock(IDynamoDBFacadeRepository)
        LocationRepository locationService = new LocationRepository()
        MenuRepository menuService = new MenuRepository(dynamoDBFacadeService)
        CreateMenuRequestConverter createMenuRequestConverter = new CreateMenuRequestConverter(locationService)
        MenuController menuController = new MenuController(menuService, createMenuRequestConverter)

        when:
        HttpResponse<Menu> menuHttpResponse = menuController.addMenu(createMenuRequest)

        then:
        menuHttpResponse.status() == HttpStatus.BAD_REQUEST
    }
}
