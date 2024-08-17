package com.example.controllers

import com.example.converters.CreateOrderRequestConverter
import com.example.dto.request.CreateOrderRequest
import com.example.exceptions.missingRequredLinkedEntityExceptions.MissingMealLinkedEntityException
import com.example.models.AnonymousOrder
import com.example.models.MenuItem
import com.example.models.Order
import com.example.models.meal.Meal
import com.example.models.meal.MealConfig
import com.example.models.meal.PrivateMealConfig
import com.example.services.MealService
import com.example.services.OrderService
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.exceptions.HttpStatusException
import io.micronaut.security.authentication.Authentication
import spock.lang.Specification

import java.time.Instant

class AnonymousOrderControllerSpec extends Specification {
    def "test adding an Anonymous Order"() {
        given:
        OrderService orderService = Mock(OrderService)
        CreateOrderRequestConverter createOrderRequestConverter = Mock(CreateOrderRequestConverter)

        AnonymousOrderController anonymousOrderController = new AnonymousOrderController(orderService, null, createOrderRequestConverter)

        Set<MenuItem> menuItems = [new MenuItem(name: ",", description: "", price: 0)]
        String uid = "79fe4688-604d-4301-9025-e5ff0c8c50c3"
        CreateOrderRequest createOrderRequest = new CreateOrderRequest(Instant.ofEpochSecond(5), "535823f0-1707-4734-8e18-21ac20350b96", menuItems, "1707-535823f0-4734-21ac20350b96-8e18")

        when:
        HttpResponse<AnonymousOrder> resp = anonymousOrderController.addAnonymousOrder(createOrderRequest, uid)

        then:
        assert resp.status() == HttpStatus.OK
        assert resp.body() != null
        1 * orderService.addOrder(_ as Order)
        1 * createOrderRequestConverter.convertCreateOrderRequestToOrder(createOrderRequest, uid, AnonymousOrderController.DEFAULT_AnonymousUser_NAME, true) >> new AnonymousOrder()
    }

    def "test getting a valid Anonymous Order"() {
        given:
        OrderService orderService = Mock(OrderService)
        AnonymousOrderController anonymousOrderController = new AnonymousOrderController(orderService, null, null)
        String uid = "617003fb-60f6-46c1-946a-76dbf8022fc8"
        String mealId = "48c27362-52db-4b57-9652-cdccd0fb698e"

        when:
        Optional<AnonymousOrder> resp = anonymousOrderController.getAnonymousOrder(uid, mealId)

        then:
        assert resp.isPresent()
        1 * orderService.getAnonymousOrder(uid, mealId) >> Optional.of(new AnonymousOrder())
    }

    def "test getting an non existent Anonymous Order"() {
        given:
        OrderService orderService = Mock(OrderService)
        AnonymousOrderController anonymousOrderController = new AnonymousOrderController(orderService, null, null)
        String uid = "617003fb-60f6-46c1-946a-76dbf8022fc8"
        String mealId = "48c27362-52db-4b57-9652-cdccd0fb698e"

        when:
        Optional<AnonymousOrder> resp = anonymousOrderController.getAnonymousOrder(uid, mealId)

        then:
        assert resp.isEmpty()
        1 * orderService.getAnonymousOrder(uid, mealId) >> Optional.empty()
    }

    def "test adding AnonymousOrder from a list of recipient ids"() {
        given:
        MealService mealService = Mock(MealService)
        OrderService orderService = Mock(OrderService)
        AnonymousOrderController anonymousOrderController = new AnonymousOrderController(orderService, mealService, null)
        Instant dateOfMeal = Instant.ofEpochSecond(1723140295)
        String mealId = "3a54a877-388f-4bd7-92af-2f374681b3fd"
        String uid = "d727d708-0391-49e4-81ce-bf000ddc6d6b"
        Authentication authentication = Mock(Authentication)
        authentication.getName() >> uid
        Set<String> recipientIds = ["1ce82b95-5abc-422e-991c-a73c77fae9bd"]
        Meal meal = new Meal(mealConfig: new MealConfig(privateMealConfig: new PrivateMealConfig(recipientIds)))

        when:
        anonymousOrderController.addOrdersForMeal(dateOfMeal, mealId, authentication)

        then:
        1 * mealService.getMeal(uid, (dateOfMeal.toString() + "_" + mealId) as String) >> Optional.of(meal)
        1 * orderService.addOrdersForPrivateMeal(meal, recipientIds)
    }

    def "test adding AnonymousOrder from a list of recipient ids when meal does not exist"() {
        given:
        MealService mealService = Mock(MealService)
        OrderService orderService = Mock(OrderService)
        AnonymousOrderController anonymousOrderController = new AnonymousOrderController(orderService, mealService, null)
        Instant dateOfMeal = Instant.ofEpochSecond(1723140295)
        String mealId = "3a54a877-388f-4bd7-92af-2f374681b3fd"
        String uid = "d727d708-0391-49e4-81ce-bf000ddc6d6b"
        Authentication authentication = Mock(Authentication)
        authentication.getName() >> uid

        when:
        anonymousOrderController.addOrdersForMeal(dateOfMeal, mealId, authentication)

        then:
        1 * mealService.getMeal(uid, (dateOfMeal.toString() + "_" + mealId) as String) >> Optional.empty()
        thrown(MissingMealLinkedEntityException)
    }

    def "test adding AnonymousOrder from a list of recipient ids when when meal has not private meal config"() {
        given:
        MealService mealService = Mock(MealService)
        OrderService orderService = Mock(OrderService)
        AnonymousOrderController anonymousOrderController = new AnonymousOrderController(orderService, mealService, null)
        Instant dateOfMeal = Instant.ofEpochSecond(1723140295)
        String mealId = "3a54a877-388f-4bd7-92af-2f374681b3fd"
        String uid = "d727d708-0391-49e4-81ce-bf000ddc6d6b"
        Authentication authentication = Mock(Authentication)
        authentication.getName() >> uid

        when:
        anonymousOrderController.addOrdersForMeal(dateOfMeal, mealId, authentication)

        then:
        1 * mealService.getMeal(uid, (dateOfMeal.toString() + "_" + mealId) as String) >> Optional.of(new Meal())

        HttpStatusException httpStatusException = thrown(HttpStatusException)
        assert httpStatusException.status == HttpStatus.BAD_REQUEST
    }
}
