package com.thebotmeek.api.converters

import com.thebotmeek.api.converters.CreateOrderRequestConverter
import com.foodorder.server.exceptions.OrderRequestConverterException
import com.foodorder.server.models.Menu
import com.foodorder.server.models.MenuItem
import com.foodorder.server.models.Order
import com.foodorder.server.models.meal.Meal
import com.foodorder.server.models.meal.MealConfig
import com.foodorder.server.models.meal.PrivateMealConfig
import com.foodorder.server.models.orderParticipant.AuthenticatedOrderParticipant
import com.foodorder.server.repository.IDynamoDBFacadeRepository
import com.foodorder.server.repository.MealRepository
import com.foodorder.server.repository.MenuRepository
import com.thebotmeek.api.request.CreateOrderRequest
import spock.lang.Specification

import java.time.Instant

class CreateOrderRequestConverterTest extends Specification {
    def "ConvertCreateOrderRequestToOrder"() {
        given:
        String mealId = "797b001f-de8f-47ed-833a-d84e61c73fe7"
        Instant dateOfMeal = Instant.ofEpochSecond(1711487392)
        String uid = "d84e61c73fe7-de8f-47ed-833a-797b001f"
        String preferredUsername = "Steven"
        String organizerUid = "384n4uc73fe7-de8f-47ed-833a-797b001f"
        String location = "London"
        String name = "MacD"
        Set<MenuItem> menuItems = [new MenuItem(name: "name", description: "description", price: 1.0)]
        CreateOrderRequest createOrderRequest = new CreateOrderRequest(dateOfMeal, mealId, menuItems, organizerUid)


        IDynamoDBFacadeRepository orderServiceIDynamoDBFacadeService = Mock(IDynamoDBFacadeRepository)
        MealRepository mealService = new MealRepository(orderServiceIDynamoDBFacadeService)
        Meal meal = new Meal(id: mealId, mealDate: dateOfMeal, location: location, menuName: name, mealConfig: new MealConfig())
        orderServiceIDynamoDBFacadeService.load(Meal.class, "Meal_" + organizerUid, dateOfMeal.toString() + "_" + mealId) >> {
            return Optional.of(meal)
        }

        IDynamoDBFacadeRepository menuServiceIDynamoDBFacadeService = Mock(IDynamoDBFacadeRepository)
        MenuRepository menuService = new MenuRepository(menuServiceIDynamoDBFacadeService)
        menuServiceIDynamoDBFacadeService.load(Menu.class, "Menu_" + location, name) >> {
            return Optional.of(
                    new Menu(menuItems: List.of(
                            new MenuItem(name: "name", description: "description", price: 1.0)
                    )
                    )
            )
        }
        CreateOrderRequestConverter createOrderRequestConverter = new CreateOrderRequestConverter(mealService, menuService)

        when:
        Order order = createOrderRequestConverter.convertCreateOrderRequestToOrder(createOrderRequest, uid, preferredUsername)

        then:
        assert order.getId() != null
        order.setId(null)

        order == new Order(meal: meal, menuItems: menuItems, orderParticipant: new AuthenticatedOrderParticipant(preferredUsername,uid), submitted: false)
    }

    def "ConvertCreateOrderRequestToOrder with invalid mealId organizerUid/sort key"() {
        given:
        String mealId = "797b001f-de8f-47ed-833a-d84e61c73fe7"
        Instant dateOfMeal = Instant.ofEpochSecond(1711487392)
        String uid = "d84e61c73fe7-de8f-47ed-833a-797b001f"
        String preferredUsername = "Steven"
        String organizerUid = "384n4uc73fe7-de8f-47ed-833a-797b001f"
        MenuItem menuItem = new MenuItem(
                name: "name",
                description: "description",
                price: 1.0
        )
        CreateOrderRequest createOrderRequest = new CreateOrderRequest(dateOfMeal, mealId, Set.of(menuItem), organizerUid)

        IDynamoDBFacadeRepository orderServiceIDynamoDBFacadeService = Mock(IDynamoDBFacadeRepository)
        MealRepository mealService = new MealRepository(orderServiceIDynamoDBFacadeService)
        orderServiceIDynamoDBFacadeService.load(Meal.class, "Meal_" + organizerUid, dateOfMeal.toString() + "_" + mealId) >> {
            return Optional.empty()
        }
        CreateOrderRequestConverter createOrderRequestConverter = new CreateOrderRequestConverter(mealService, null)

        when:
        createOrderRequestConverter.convertCreateOrderRequestToOrder(createOrderRequest, uid, preferredUsername)

        then:
        thrown(OrderRequestConverterException)
    }

    def "ConvertCreateOrderRequestToOrder with invalid mealId menuItems"(Integer index, Optional<Menu> menu) {
        given:
        String mealId = "797b001f-de8f-47ed-833a-d84e61c73fe7"
        Instant dateOfMeal = Instant.ofEpochSecond(1711487392)
        String uid = "d84e61c73fe7-de8f-47ed-833a-797b001f"
        String preferredUsername = "Steven"
        String organizerUid = "384n4uc73fe7-de8f-47ed-833a-797b001f"
        String location = "London"
        String name = "MacD"
        MenuItem menuItem = new MenuItem(
                name: "name",
                description: "description",
                price: 1.0
        )
        CreateOrderRequest createOrderRequest = new CreateOrderRequest(dateOfMeal, mealId, Set.of(menuItem), organizerUid)

        IDynamoDBFacadeRepository orderServiceIDynamoDBFacadeService = Mock(IDynamoDBFacadeRepository)
        MealRepository mealService = new MealRepository(orderServiceIDynamoDBFacadeService)
        orderServiceIDynamoDBFacadeService.load(Meal.class, "Meal_" + organizerUid, dateOfMeal.toString() + "_" + mealId) >> {
            return Optional.of(new Meal(location: location, menuName: name, mealConfig: new MealConfig()))
        }

        IDynamoDBFacadeRepository menuServiceIDynamoDBFacadeService = Mock(IDynamoDBFacadeRepository)
        MenuRepository menuService = new MenuRepository(menuServiceIDynamoDBFacadeService)
        menuServiceIDynamoDBFacadeService.load(Menu.class, "Menu_" + location, name) >> {
            return menu
        }
        CreateOrderRequestConverter createOrderRequestConverter = new CreateOrderRequestConverter(mealService, menuService)


        when:
        createOrderRequestConverter.convertCreateOrderRequestToOrder(createOrderRequest, uid, preferredUsername)

        then:
        thrown(OrderRequestConverterException)

        where:
        index | menu
        1     | Optional.of(new Menu(menuItems: List.of(new MenuItem(name: "name", description: "description", price: 5.0))))
        2     | Optional.of(new Menu(menuItems: new ArrayList<MenuItem>()))
    }

    def "Make sure that validation passes when adding a order to a private meal with valid uid "() {
        given:
        String mealId = "797b001f-de8f-47ed-833a-d84e61c73fe7"
        Instant dateOfMeal = Instant.ofEpochSecond(1711487392)
        String uid = "d84e61c73fe7-de8f-47ed-833a-797b001f"
        String preferredUsername = "Steven"
        String organizerUid = "384n4uc73fe7-de8f-47ed-833a-797b001f"
        String location = "London"
        String name = "MacD"
        Set<MenuItem> menuItems = [new MenuItem(name: "name", description: "description", price: 1.0)]
        CreateOrderRequest createOrderRequest = new CreateOrderRequest(dateOfMeal, mealId, menuItems, organizerUid)


        IDynamoDBFacadeRepository orderServiceIDynamoDBFacadeService = Mock(IDynamoDBFacadeRepository)
        MealRepository mealService = new MealRepository(orderServiceIDynamoDBFacadeService)
        PrivateMealConfig privateMealConfig = new PrivateMealConfig()
        privateMealConfig.addRecipientId(uid)
        Meal meal = new Meal(id: mealId, mealDate: dateOfMeal, location: location, menuName: name, mealConfig: new MealConfig(privateMealConfig: privateMealConfig))
        orderServiceIDynamoDBFacadeService.load(Meal.class, "Meal_" + organizerUid, dateOfMeal.toString() + "_" + mealId) >> {
            return Optional.of(meal)
        }

        IDynamoDBFacadeRepository menuServiceIDynamoDBFacadeService = Mock(IDynamoDBFacadeRepository)
        MenuRepository menuService = new MenuRepository(menuServiceIDynamoDBFacadeService)
        menuServiceIDynamoDBFacadeService.load(Menu.class, "Menu_" + location, name) >> {
            return Optional.of(
                    new Menu(menuItems: List.of(
                            new MenuItem(name: "name", description: "description", price: 1.0)
                    )
                    )
            )
        }
        CreateOrderRequestConverter createOrderRequestConverter = new CreateOrderRequestConverter(mealService, menuService)


        when:
        Order order = createOrderRequestConverter.convertCreateOrderRequestToOrder(createOrderRequest, uid, preferredUsername)

        then:
        assert order.getId() != null
        order.setId(null)
        order == new Order(meal: meal, menuItems: menuItems, orderParticipant:  new AuthenticatedOrderParticipant(preferredUsername,uid), submitted: false)
    }

    def "Make sure that validation fails when adding a order to a private meal without a valid uid "() {
        given:
        String mealId = "797b001f-de8f-47ed-833a-d84e61c73fe7"
        Instant dateOfMeal = Instant.ofEpochSecond(1711487392)
        String uid = "d84e61c73fe7-de8f-47ed-833a-797b001f"
        String preferredUsername = "Steven"
        String organizerUid = "384n4uc73fe7-de8f-47ed-833a-797b001f"
        String location = "London"
        String name = "MacD"
        Set<MenuItem> menuItems = [new MenuItem(name: "name", description: "description", price: 1.0)]
        CreateOrderRequest createOrderRequest = new CreateOrderRequest(dateOfMeal, mealId, menuItems, organizerUid)


        IDynamoDBFacadeRepository orderServiceIDynamoDBFacadeService = Mock(IDynamoDBFacadeRepository)
        MealRepository mealService = new MealRepository(orderServiceIDynamoDBFacadeService)
        PrivateMealConfig privateMealConfig = new PrivateMealConfig()

        Meal meal = new Meal(id: mealId, mealDate: dateOfMeal, location: location, menuName: name, mealConfig: new MealConfig(privateMealConfig:  privateMealConfig))
        orderServiceIDynamoDBFacadeService.load(Meal.class, "Meal_" + organizerUid, dateOfMeal.toString() + "_" + mealId) >> {
            return Optional.of(meal)
        }

        IDynamoDBFacadeRepository menuServiceIDynamoDBFacadeService = Mock(IDynamoDBFacadeRepository)
        MenuRepository menuService = new MenuRepository(menuServiceIDynamoDBFacadeService)
        menuServiceIDynamoDBFacadeService.load(Menu.class, "Menu_" + location, name) >> {
            return Optional.of(
                    new Menu(menuItems: List.of(
                            new MenuItem(name: "name", description: "description", price: 1.0)
                    )
                    )
            )
        }
        CreateOrderRequestConverter createOrderRequestConverter = new CreateOrderRequestConverter(mealService, menuService)

        when:
        createOrderRequestConverter.convertCreateOrderRequestToOrder(createOrderRequest, uid, preferredUsername)

        then:
        thrown(OrderRequestConverterException)
    }
}
