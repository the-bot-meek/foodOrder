package com.thebotmeek.foodorder.server.controllers

import com.foodorder.server.controllers.AnonymousOrderController
import com.foodorder.server.converters.CreateOrderRequestConverter
import com.foodorder.server.request.CreateOrderRequest
import com.foodorder.server.exceptions.missingRequredLinkedEntityExceptions.MissingMealLinkedEntityException

import com.foodorder.server.models.MenuItem
import com.foodorder.server.models.Order
import com.foodorder.server.models.meal.Meal
import com.foodorder.server.models.meal.MealConfig
import com.foodorder.server.models.meal.PrivateMealConfig
import com.foodorder.server.repository.IDynamoDBFacadeRepository
import com.foodorder.server.repository.MealRepository
import com.foodorder.server.repository.OrderRepository
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.exceptions.HttpStatusException
import io.micronaut.security.authentication.Authentication
import spock.lang.Specification

import java.time.Instant

class AnonymousOrderControllerSpec extends Specification {
    OrderRepository orderRepository
    CreateOrderRequestConverter createOrderRequestConverter
    AnonymousOrderController anonymousOrderController
    MealRepository mealRepository

    def "setup"() {
        orderRepository = Mock(OrderRepository)
        mealRepository = Mock(MealRepository)
        createOrderRequestConverter = Mock(CreateOrderRequestConverter)
        anonymousOrderController = new AnonymousOrderController(orderRepository, mealRepository, null)
    }

    private Authentication mockAuthentication(String name) {
        Authentication authentication = Mock(Authentication)
        authentication.getName() >> name
        return authentication
    }

    def "test adding an Anonymous Order"() {
        given:
        AnonymousOrderController anonymousOrderController = new AnonymousOrderController(orderRepository, null, createOrderRequestConverter)

        Set<MenuItem> menuItems = [new MenuItem(name: ",", description: "", price: 0)]
        String uid = "79fe4688-604d-4301-9025-e5ff0c8c50c3"
        CreateOrderRequest createOrderRequest = new CreateOrderRequest(Instant.ofEpochSecond(5), "535823f0-1707-4734-8e18-21ac20350b96", menuItems, "1707-535823f0-4734-21ac20350b96-8e18")

        when:
        HttpResponse<Order> resp = anonymousOrderController.addAnonymousOrder(createOrderRequest, uid)

        then:
        assert resp.status() == HttpStatus.OK
        assert resp.body() != null
        1 * orderRepository.addOrder(_ as Order)
        1 * createOrderRequestConverter.convertCreateOrderRequestToOrder(createOrderRequest, uid, AnonymousOrderController.DEFAULT_AnonymousUser_NAME, true) >> new Order()
    }

    def "test getting a valid Anonymous Order"() {
        given:
        String uid = "617003fb-60f6-46c1-946a-76dbf8022fc8"
        String mealId = "48c27362-52db-4b57-9652-cdccd0fb698e"

        when:
        Optional<Order> resp = anonymousOrderController.getAnonymousOrder(uid, mealId)

        then:
        assert resp.isPresent()
        1 * orderRepository.getAnonymousOrder(uid, mealId) >> Optional.of(new Order())
    }

    def "test getting an non existent Anonymous Order"() {
        given:
        String uid = "617003fb-60f6-46c1-946a-76dbf8022fc8"
        String mealId = "48c27362-52db-4b57-9652-cdccd0fb698e"

        when:
        Optional<Order> resp = anonymousOrderController.getAnonymousOrder(uid, mealId)

        then:
        assert resp.isEmpty()
        1 * orderRepository.getAnonymousOrder(uid, mealId) >> Optional.empty()
    }

    def "test adding AnonymousOrder from a list of recipient ids"() {
        given:
        MealRepository mealService = Mock(MealRepository)
        OrderRepository orderService = Mock(OrderRepository)
        AnonymousOrderController anonymousOrderController = new AnonymousOrderController(orderService, mealService, null)
        Instant dateOfMeal = Instant.ofEpochSecond(1723140295)
        String mealId = "3a54a877-388f-4bd7-92af-2f374681b3fd"
        String uid = "d727d708-0391-49e4-81ce-bf000ddc6d6b"
        Authentication authentication = Mock(Authentication)
        authentication.getName() >> uid
        Set<String> recipientIds = ["1ce82b95-5abc-422e-991c-a73c77fae9bd"]
        Meal meal = new Meal(mealConfig: new MealConfig(privateMealConfig: new PrivateMealConfig(recipientIds)))

        when:
        anonymousOrderController.addOrdersForMeal(dateOfMeal.toString() + "_" + mealId, authentication)

        then:
        1 * mealService.getMeal(uid, (dateOfMeal.toString() + "_" + mealId) as String) >> Optional.of(meal)
        1 * orderService.addOrdersForPrivateMeal(meal, recipientIds)
    }

    def "test adding AnonymousOrder from a list of recipient ids when meal does not exist"() {
        given:
        Instant dateOfMeal = Instant.ofEpochSecond(1723140295)
        String mealId = "3a54a877-388f-4bd7-92af-2f374681b3fd"
        String uid = "d727d708-0391-49e4-81ce-bf000ddc6d6b"
        Authentication authentication = Mock(Authentication)
        authentication.getName() >> uid

        when:
        anonymousOrderController.addOrdersForMeal(dateOfMeal.toString() + "_" + mealId, authentication)

        then:
        1 * mealRepository.getMeal(uid, (dateOfMeal.toString() + "_" + mealId) as String) >> Optional.empty()
        thrown(MissingMealLinkedEntityException)
    }

    def "test adding AnonymousOrder from a list of recipient ids when when meal has not private meal config"() {
        given:
        Instant dateOfMeal = Instant.ofEpochSecond(1723140295)
        String mealId = "3a54a877-388f-4bd7-92af-2f374681b3fd"
        String uid = "d727d708-0391-49e4-81ce-bf000ddc6d6b"
        Authentication authentication = Mock(Authentication)
        authentication.getName() >> uid

        when:
        anonymousOrderController.addOrdersForMeal(dateOfMeal.toString() + "_" + mealId, authentication)

        then:
        1 * mealRepository.getMeal(uid, (dateOfMeal.toString() + "_" + mealId) as String) >> Optional.of(new Meal())

        HttpStatusException httpStatusException = thrown(HttpStatusException)
        assert httpStatusException.status == HttpStatus.BAD_REQUEST
    }

    def "Add orders for Meal"() {
        given:
        final String uid = "ce05e2ef-a609-4ca4-8650-a9a5c7aadfcd"
        PrivateMealConfig privateMealConfig = new PrivateMealConfig(["12345"] as Set)
        Meal meal = new Meal(
                id: uid, name: "name", mealDate: Instant.ofEpochSecond(1711405066), uid: "principal_name", location: "London", menuName: "MacD", mealConfig: new MealConfig(privateMealConfig: privateMealConfig)
        )

        IDynamoDBFacadeRepository dynamoDBFacadeService = Mock(IDynamoDBFacadeRepository)
        OrderRepository orderService = new OrderRepository(dynamoDBFacadeService)
        Authentication authentication = mockAuthentication(uid)
        MealRepository mealService = new MealRepository(dynamoDBFacadeService)
        AnonymousOrderController orderController = new AnonymousOrderController(orderService, mealService, null)

        when:
        orderController.addOrdersForMeal(meal.getSortKey(), authentication)

        then:
        1 * dynamoDBFacadeService.batchSave(_)

        1 * dynamoDBFacadeService.load(Meal.class, "Meal_" + uid, "2024-03-25T22:17:46Z_ce05e2ef-a609-4ca4-8650-a9a5c7aadfcd") >> {
            Optional.of(meal)
        }
    }

    def "Adding orders for a non existent Meal throws MissingOrderLinkedEntityException"() {
        given:
        final String uid = "ce05e2ef-a609-4ca4-8650-a9a5c7aadfcd"

        IDynamoDBFacadeRepository dynamoDBFacadeService = Mock(IDynamoDBFacadeRepository)
        Authentication authentication = mockAuthentication(uid)
        1 * dynamoDBFacadeService.load(*_) >> {
            Optional.empty()
        }
        MealRepository mealService = new MealRepository(dynamoDBFacadeService)
        AnonymousOrderController orderController = new AnonymousOrderController(null, mealService, null)

        when:
        orderController.addOrdersForMeal("1004298765_invalidID", authentication)

        then:
        thrown(MissingMealLinkedEntityException)
    }
}
