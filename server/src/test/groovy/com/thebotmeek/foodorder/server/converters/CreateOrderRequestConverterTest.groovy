package com.thebotmeek.foodorder.server.converters

import com.foodorder.server.converters.CreateOrderRequestConverter
import com.foodorder.server.exceptions.OrderRequestConverterException
import com.foodorder.server.request.CreateOrderRequest
import com.foodorder.server.models.meal.Meal
import com.foodorder.server.models.meal.MealConfig
import com.foodorder.server.models.meal.PrivateMealConfig
import com.foodorder.server.models.MenuItem
import com.foodorder.server.models.Order
import com.foodorder.server.models.Venue
import com.foodorder.server.repository.IDynamoDBFacadeRepository
import com.foodorder.server.repository.MealRepository
import com.foodorder.server.repository.VenueRepository
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
        Meal meal = new Meal(id: mealId, mealDate: dateOfMeal, location: location, venueName: name, mealConfig: new MealConfig())
        orderServiceIDynamoDBFacadeService.load(Meal.class, "Meal_" + organizerUid, dateOfMeal.toString() + "_" + mealId) >> {
            return Optional.of(meal)
        }

        IDynamoDBFacadeRepository venueServiceIDynamoDBFacadeService = Mock(IDynamoDBFacadeRepository)
        VenueRepository venueService = new VenueRepository(venueServiceIDynamoDBFacadeService)
        venueServiceIDynamoDBFacadeService.load(Venue.class, "Venue_" + location, name) >> {
            return Optional.of(
                    new Venue(menuItems: List.of(
                            new MenuItem(name: "name", description: "description", price: 1.0)
                    )
                    )
            )
        }
        CreateOrderRequestConverter createOrderRequestConverter = new CreateOrderRequestConverter(mealService, venueService)

        when:
        Order order = createOrderRequestConverter.convertCreateOrderRequestToOrder(createOrderRequest, uid, preferredUsername)

        then:
        assert order.getId() != null
        order.setId(null)
        order == new Order(meal: meal, uid: uid, menuItems: menuItems, participantsName: preferredUsername, submitted: false)
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

    def "ConvertCreateOrderRequestToOrder with invalid mealId menuItems"(Integer index, Optional<Venue> venue) {
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
            return Optional.of(new Meal(location: location, venueName: name, mealConfig: new MealConfig()))
        }

        IDynamoDBFacadeRepository venueServiceIDynamoDBFacadeService = Mock(IDynamoDBFacadeRepository)
        VenueRepository venueService = new VenueRepository(venueServiceIDynamoDBFacadeService)
        venueServiceIDynamoDBFacadeService.load(Venue.class, "Venue_" + location, name) >> {
            return venue
        }
        CreateOrderRequestConverter createOrderRequestConverter = new CreateOrderRequestConverter(mealService, venueService)


        when:
        createOrderRequestConverter.convertCreateOrderRequestToOrder(createOrderRequest, uid, preferredUsername)

        then:
        thrown(OrderRequestConverterException)

        where:
        index | venue
        1     | Optional.of(new Venue(menuItems: List.of(new MenuItem(name: "name", description: "description", price: 5.0))))
        2     | Optional.of(new Venue(menuItems: new ArrayList<MenuItem>()))
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
        Meal meal = new Meal(id: mealId, mealDate: dateOfMeal, location: location, venueName: name, mealConfig: new MealConfig(privateMealConfig: privateMealConfig))
        orderServiceIDynamoDBFacadeService.load(Meal.class, "Meal_" + organizerUid, dateOfMeal.toString() + "_" + mealId) >> {
            return Optional.of(meal)
        }

        IDynamoDBFacadeRepository venueServiceIDynamoDBFacadeService = Mock(IDynamoDBFacadeRepository)
        VenueRepository venueService = new VenueRepository(venueServiceIDynamoDBFacadeService)
        venueServiceIDynamoDBFacadeService.load(Venue.class, "Venue_" + location, name) >> {
            return Optional.of(
                    new Venue(menuItems: List.of(
                            new MenuItem(name: "name", description: "description", price: 1.0)
                    )
                    )
            )
        }
        CreateOrderRequestConverter createOrderRequestConverter = new CreateOrderRequestConverter(mealService, venueService)


        when:
        Order order = createOrderRequestConverter.convertCreateOrderRequestToOrder(createOrderRequest, uid, preferredUsername)

        then:
        assert order.getId() != null
        order.setId(null)
        order == new Order(meal: meal, uid: uid, menuItems: menuItems, participantsName: preferredUsername)
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

        Meal meal = new Meal(id: mealId, mealDate: dateOfMeal, location: location, venueName: name, mealConfig: new MealConfig(privateMealConfig:  privateMealConfig))
        orderServiceIDynamoDBFacadeService.load(Meal.class, "Meal_" + organizerUid, dateOfMeal.toString() + "_" + mealId) >> {
            return Optional.of(meal)
        }

        IDynamoDBFacadeRepository venueServiceIDynamoDBFacadeService = Mock(IDynamoDBFacadeRepository)
        VenueRepository venueService = new VenueRepository(venueServiceIDynamoDBFacadeService)
        venueServiceIDynamoDBFacadeService.load(Venue.class, "Venue_" + location, name) >> {
            return Optional.of(
                    new Venue(menuItems: List.of(
                            new MenuItem(name: "name", description: "description", price: 1.0)
                    )
                    )
            )
        }
        CreateOrderRequestConverter createOrderRequestConverter = new CreateOrderRequestConverter(mealService, venueService)

        when:
        createOrderRequestConverter.convertCreateOrderRequestToOrder(createOrderRequest, uid, preferredUsername)

        then:
        thrown(OrderRequestConverterException)
    }
}
