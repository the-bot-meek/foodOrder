package com.example.controllers

import com.example.converters.CreateOrderRequestConverter
import com.example.dto.request.CreateOrderRequest
import com.example.exceptions.missingRequredLinkedEntityExceptions.MissingMealLinkedEntityException
import com.example.exceptions.missingRequredLinkedEntityExceptions.MissingOrderLinkedEntityException
import com.example.models.meal.Meal
import com.example.models.MenuItem
import com.example.models.Order
import com.example.models.Venue
import com.example.models.meal.MealConfig
import com.example.models.meal.PrivateMealConfig
import com.example.services.IDynamoDBFacadeService
import com.example.services.MealService
import com.example.services.OrderService
import com.example.services.VenueService
import io.micronaut.http.HttpStatus
import io.micronaut.http.exceptions.HttpStatusException
import io.micronaut.security.authentication.Authentication
import spock.lang.Specification
import java.time.Instant

class OrderControllerSpec extends Specification {
    private Authentication mockAuthentication(String name) {
        Authentication authentication = Mock(Authentication)
        authentication.getName() >> name
        return authentication
    }

    def "AddOrder"() {
        given:
        String mealId = "797b001f-de8f-47ed-833a-d84e61c73fe7"
        Instant dateOfMeal = Instant.ofEpochSecond(1711487392)
        String uid = "d84e61c73fe7-de8f-47ed-833a-797b001f"
        String organizerUid = "d02761c73fe7-de8f-47ed-833a-739b001f"
        String location = "London"
        String name = "MacD"


        IDynamoDBFacadeService mealServiceIDynamoDBFacadeService = Mock(IDynamoDBFacadeService)
        MealService mealService = new MealService(mealServiceIDynamoDBFacadeService)
        mealServiceIDynamoDBFacadeService.load(Meal, organizerUid, (dateOfMeal.toString() + "_" + mealId)) >> {
            return Optional.of(new Meal(location: location, venueName: name, id: mealId, mealDate: dateOfMeal, mealConfig: new MealConfig()))
        }

        IDynamoDBFacadeService venueServiceIDynamoDBFacadeService = Mock(IDynamoDBFacadeService)
        VenueService venueService = new VenueService(venueServiceIDynamoDBFacadeService)
        venueServiceIDynamoDBFacadeService.load(Venue.class, "Venue_" + location, name) >> {
            return Optional.of(
                    new Venue(menuItems: List.of(
                            new MenuItem(name: "name", description: "description", price: 1.0)
                    )
                )
            )
        }

        IDynamoDBFacadeService dynamoDBFacadeService = Mock(IDynamoDBFacadeService)
        CreateOrderRequestConverter createOrderRequestConverter = new CreateOrderRequestConverter(mealService, venueService)
        OrderService orderService = new OrderService(dynamoDBFacadeService, null)
        OrderController orderController = new OrderController(orderService, null, createOrderRequestConverter)
        Authentication authentication = Mock(Authentication)
        authentication.getAttributes() >> Map.of("preferred_username", "usename")
        authentication.getName() >> uid

        Set<MenuItem> menuItems = Set.of(
                new MenuItem(name: "name", description: "description", price: 1.0)
        )
        CreateOrderRequest createOrderRequest = new CreateOrderRequest(
                dateOfMeal,
                mealId,
                menuItems,
                organizerUid
        )

        when:
        orderController.addOrder(createOrderRequest, authentication)

        then:
        1 * dynamoDBFacadeService.save({Order order ->
            assert order.getUid() == uid
            assert order.getMenuItems() == menuItems
            assert order.getParticipantsName() == "usename"
            order.getMeal().with {Meal meal ->
                assert meal.getId() == mealId
                assert meal.getMealDate() == dateOfMeal
            }

        })
    }


    def "Add orders for Meal"() {
        given:
        final String uid = "ce05e2ef-a609-4ca4-8650-a9a5c7aadfcd"
        PrivateMealConfig privateMealConfig = new PrivateMealConfig(["12345"] as Set)
        Meal meal = new Meal(
                id: uid, name: "name", mealDate: Instant.ofEpochSecond(1711405066), uid: "principal_name", location: "London", venueName: "MacD", mealConfig: new MealConfig(privateMealConfig: privateMealConfig)
        )

        IDynamoDBFacadeService dynamoDBFacadeService = Mock(IDynamoDBFacadeService)
        1 * dynamoDBFacadeService.load(Venue, "Venue_" + meal.getLocation(), meal.getVenueName()) >> {
            return Optional.of(new Venue(menuItems: []))
        }
        VenueService venueService = new VenueService(dynamoDBFacadeService)

        OrderService orderService = new OrderService(dynamoDBFacadeService, venueService)


        Authentication authentication = mockAuthentication(uid)

        1 * dynamoDBFacadeService.load(Meal.class,uid, "2024-03-25T22:17:46Z_ce05e2ef-a609-4ca4-8650-a9a5c7aadfcd") >> {
            Optional.of(meal)
        }
        MealService mealService = new MealService(dynamoDBFacadeService)
        OrderController orderController = new OrderController(orderService, mealService, null)

        when:
        orderController.addOrdersForMeal(meal.getMealDate(), meal.getId(), authentication)

        then:
        1 * dynamoDBFacadeService.batchSave(_)
    }


    def "Adding orders for Meal with a non existent Venue throws MissingOrderLinkedEntityException"() {
        given:
        final String uid = "ce05e2ef-a609-4ca4-8650-a9a5c7aadfcd"
        PrivateMealConfig privateMealConfig = new PrivateMealConfig(["12345"] as Set)
        Meal meal = new Meal(
                id: uid, name: "name", mealDate: Instant.ofEpochSecond(1711405066), uid: "principal_name", location: "London", venueName: "MacD", mealConfig: new MealConfig(privateMealConfig: privateMealConfig)
        )

        IDynamoDBFacadeService dynamoDBFacadeService = Mock(IDynamoDBFacadeService)
        1 * dynamoDBFacadeService.load(Venue, "Venue_" + meal.getLocation(), meal.getVenueName()) >> {
            return Optional.empty()
        }
        VenueService venueService = new VenueService(dynamoDBFacadeService)

        OrderService orderService = new OrderService(dynamoDBFacadeService, venueService)


        Authentication authentication = mockAuthentication(uid)

        1 * dynamoDBFacadeService.load(Meal.class,uid, "2024-03-25T22:17:46Z_ce05e2ef-a609-4ca4-8650-a9a5c7aadfcd") >> {
            Optional.of(meal)
        }
        MealService mealService = new MealService(dynamoDBFacadeService)
        OrderController orderController = new OrderController(orderService, mealService, null)

        when:
        orderController.addOrdersForMeal(meal.getMealDate(), meal.getId(), authentication)

        then:
        thrown(MissingOrderLinkedEntityException)
    }


    def "Adding orders for a non existent Meal throws MissingOrderLinkedEntityException"() {
        given:
        final String uid = "ce05e2ef-a609-4ca4-8650-a9a5c7aadfcd"

        IDynamoDBFacadeService dynamoDBFacadeService = Mock(IDynamoDBFacadeService)
        Authentication authentication = mockAuthentication(uid)
        1 * dynamoDBFacadeService.load(*_) >> {
            Optional.empty()
        }
        MealService mealService = new MealService(dynamoDBFacadeService)
        OrderController orderController = new OrderController(null, mealService, null)

        when:
        orderController.addOrdersForMeal(Instant.ofEpochSecond(1722804502), "Invalid Id", authentication)

        then:
        thrown(MissingMealLinkedEntityException)
    }

    def "Adding orders for Meal with with out a privateMealConfig throws a BAD_REQUEST exception"() {
        given:
        final String uid = "ce05e2ef-a609-4ca4-8650-a9a5c7aadfcd"
        Meal meal = new Meal(
                id: uid, name: "name", mealDate: Instant.ofEpochSecond(1711405066), uid: "principal_name", location: "London", venueName: "MacD", mealConfig: new MealConfig()
        )

        IDynamoDBFacadeService dynamoDBFacadeService = Mock(IDynamoDBFacadeService)
        1 * dynamoDBFacadeService.load(Meal.class,uid, "2024-03-25T22:17:46Z_ce05e2ef-a609-4ca4-8650-a9a5c7aadfcd") >> {
            Optional.of(meal)
        }


        Authentication authentication = mockAuthentication(uid)

        MealService mealService = new MealService(dynamoDBFacadeService)
        OrderController orderController = new OrderController(null, mealService, null)

        when:
        orderController.addOrdersForMeal(meal.getMealDate(), meal.getId(), authentication)

        then:
        HttpStatusException httpStatusException = thrown(HttpStatusException)
        assert httpStatusException.getStatus() == HttpStatus.BAD_REQUEST
    }
}
