package com.thebotmeek.foodorder.server.controllers

import com.foodorder.server.controllers.AnonymousOrderController
import com.foodorder.server.converters.CreateOrderRequestConverter
import com.foodorder.server.request.CreateOrderRequest
import com.foodorder.server.exceptions.missingRequredLinkedEntityExceptions.MissingMealLinkedEntityException
import com.foodorder.server.models.AnonymousOrder
import com.foodorder.server.models.MenuItem
import com.foodorder.server.models.Order
import com.foodorder.server.models.meal.Meal
import com.foodorder.server.models.meal.MealConfig
import com.foodorder.server.models.meal.PrivateMealConfig
import com.foodorder.server.services.IDynamoDBFacadeService
import com.foodorder.server.services.MealService
import com.foodorder.server.services.OrderService
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.exceptions.HttpStatusException
import io.micronaut.security.authentication.Authentication
import spock.lang.Specification

import java.time.Instant

class AnonymousOrderControllerSpec extends Specification {
    OrderService orderService
    CreateOrderRequestConverter createOrderRequestConverter
    AnonymousOrderController anonymousOrderController

    def "setup"() {
        orderService = Mock(OrderService)
        createOrderRequestConverter = Mock(CreateOrderRequestConverter)
        anonymousOrderController = new AnonymousOrderController(orderService, null)
    }

    private Authentication mockAuthentication(String name) {
        Authentication authentication = Mock(Authentication)
        authentication.getName() >> name
        return authentication
    }

    def "test adding an Anonymous Order"() {
        given:
        AnonymousOrderController anonymousOrderController = new AnonymousOrderController(orderService, createOrderRequestConverter)

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
        String uid = "617003fb-60f6-46c1-946a-76dbf8022fc8"
        String mealId = "48c27362-52db-4b57-9652-cdccd0fb698e"

        when:
        Optional<AnonymousOrder> resp = anonymousOrderController.getAnonymousOrder(uid, mealId)

        then:
        assert resp.isEmpty()
        1 * orderService.getAnonymousOrder(uid, mealId) >> Optional.empty()
    }
}
