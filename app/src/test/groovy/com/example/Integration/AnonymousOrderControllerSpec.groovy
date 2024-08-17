package com.example.Integration

import com.example.client.AnonymousOrderClient
import com.example.client.MealClient
import com.example.client.VenueClient
import com.example.controllers.AnonymousOrderController
import com.example.controllers.OrderController
import com.example.dto.request.CreateMealRequest
import com.example.dto.request.CreateOrderRequest
import com.example.dto.request.CreateVenueRequest
import com.example.exceptions.missingRequredLinkedEntityExceptions.MissingMealLinkedEntityException
import com.example.exceptions.missingRequredLinkedEntityExceptions.MissingOrderLinkedEntityException
import com.example.models.AnonymousOrder
import com.example.models.MenuItem
import com.example.models.Venue
import com.example.models.meal.Meal
import com.example.models.meal.MealConfig
import com.example.models.meal.PrivateMealConfig
import com.example.services.IDynamoDBFacadeService
import com.example.services.MealService
import com.example.services.OrderService
import com.example.services.VenueService
import io.micronaut.http.HttpStatus
import io.micronaut.http.exceptions.HttpStatusException
import io.micronaut.security.authentication.Authentication
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.IgnoreIf
import spock.lang.Specification

import java.time.Instant

@MicronautTest(environments = ["mock_auth"])
@IgnoreIf({System.getenv("requireIntegrationTests") != 'true'})
class AnonymousOrderControllerSpec extends Specification{
    @Inject
    VenueClient venueClient

    @Inject
    MealClient mealClient

    @Inject
    AnonymousOrderClient anonymousOrderClient

    private Authentication mockAuthentication(String name) {
        Authentication authentication = Mock(Authentication)
        authentication.getName() >> name
        return authentication
    }

    def "test adding anonymous order"() {
        given:
        Instant dateOfMeal = Instant.ofEpochSecond(1711487392)
        String location = "London"
        String name = "MacD"
        String anonymousUserId = "3b0833d9-7e08-4ce4-8bd4-fba1291bef95"

        Set<MenuItem> menuItems = [new MenuItem(name: "name", description: "description", price: 1.01)]
        CreateVenueRequest createVenueRequest = new CreateVenueRequest(menuItems, location, name, "description")
        CreateMealRequest createMealRequest = new CreateMealRequest(name: name, dateOfMeal: dateOfMeal, location: location, venueName: name, mealConfig: new MealConfig())

        when:
        venueClient.addVenue(createVenueRequest)
        Meal meal = mealClient.addMeal(createMealRequest)

        CreateOrderRequest createOrderRequest = new CreateOrderRequest(dateOfMeal, meal.getId(), menuItems, "steven")
        AnonymousOrder anonymousOrder = anonymousOrderClient.addAnonymousOrder(createOrderRequest, anonymousUserId)

        then:
        assert anonymousOrder.getUid() == anonymousUserId
        assert anonymousOrder.getSortKey() =="2024-03-26T21:09:52Z"
        assert anonymousOrder.getParticipantsName() == "AnonymousUser"
        assert anonymousOrder.getMenuItems() == menuItems
        assert anonymousOrder.getGSIPrimaryKey() == "AnonymousOrder_${anonymousUserId}"
        assert anonymousOrder.getPrimaryKey() == "Order_${meal.getId()}"
        assert anonymousOrder.getMeal() == meal
    }

    def "test getting anonymous order"() {
        given:
        Instant dateOfMeal = Instant.ofEpochSecond(1711487392)
        String location = "London"
        String name = "MacD"
        String anonymousUserId = "3b0833d9-7e08-4ce4-8bd4-fba1291bef95"

        Set<MenuItem> menuItems = [new MenuItem(name: "name", description: "description", price: 1.01)]
        CreateVenueRequest createVenueRequest = new CreateVenueRequest(menuItems, location, name, "description")
        CreateMealRequest createMealRequest = new CreateMealRequest(name: name, dateOfMeal: dateOfMeal, location: location, venueName: name, mealConfig: new MealConfig())

        when:
        venueClient.addVenue(createVenueRequest)
        Meal meal = mealClient.addMeal(createMealRequest)

        CreateOrderRequest createOrderRequest = new CreateOrderRequest(dateOfMeal, meal.getId(), menuItems, "steven")
        AnonymousOrder anonymousOrderCreated = anonymousOrderClient.addAnonymousOrder(createOrderRequest, anonymousUserId)
        Optional<AnonymousOrder> anonymousOrder = anonymousOrderClient.getAnonymousOrder(anonymousOrderCreated.getUid(), anonymousOrderCreated.getMeal().getId())

        then:
        assert anonymousOrder.isPresent()
        assert anonymousOrder.get().getUid() == anonymousUserId
        assert anonymousOrder.get().getSortKey() =="2024-03-26T21:09:52Z"
        assert anonymousOrder.get().getParticipantsName() == "AnonymousUser"
        assert anonymousOrder.get().getMenuItems() == menuItems
        assert anonymousOrder.get().getGSIPrimaryKey() == "AnonymousOrder_${anonymousUserId}"
        assert anonymousOrder.get().getPrimaryKey() == "Order_${meal.getId()}"
        assert anonymousOrder.get().getMeal() == meal
    }

    def "Add orders for Meal"() {
        given:
        final String uid = "ce05e2ef-a609-4ca4-8650-a9a5c7aadfcd"
        PrivateMealConfig privateMealConfig = new PrivateMealConfig(["12345"] as Set)
        Meal meal = new Meal(
                id: uid, name: "name", mealDate: Instant.ofEpochSecond(1711405066), uid: "principal_name", location: "London", venueName: "MacD", mealConfig: new MealConfig(privateMealConfig: privateMealConfig)
        )

        IDynamoDBFacadeService dynamoDBFacadeService = Mock(IDynamoDBFacadeService)
        OrderService orderService = new OrderService(dynamoDBFacadeService)
        Authentication authentication = mockAuthentication(uid)
        MealService mealService = new MealService(dynamoDBFacadeService)
        AnonymousOrderController orderController = new AnonymousOrderController(orderService, mealService, null)

        when:
        orderController.addOrdersForMeal(meal.getMealDate(), meal.getId(), authentication)

        then:
        1 * dynamoDBFacadeService.batchSave(_)

        1 * dynamoDBFacadeService.load(Meal.class,uid, "2024-03-25T22:17:46Z_ce05e2ef-a609-4ca4-8650-a9a5c7aadfcd") >> {
            Optional.of(meal)
        }
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
        AnonymousOrderController orderController = new AnonymousOrderController(null, mealService, null)

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
        AnonymousOrderController orderController = new AnonymousOrderController(null, mealService, null)

        when:
        orderController.addOrdersForMeal(meal.getMealDate(), meal.getId(), authentication)

        then:
        HttpStatusException httpStatusException = thrown(HttpStatusException)
        assert httpStatusException.getStatus() == HttpStatus.BAD_REQUEST
    }
}
