package com.thebotmeek.foodorder.server.integration

import com.foodorder.server.client.MealClient
import com.foodorder.server.client.OrderClient
import com.foodorder.server.client.VenueClient
import com.foodorder.server.converters.CreatePrivateMealRequest
import com.foodorder.server.models.MenuItem
import com.foodorder.server.request.CreateMealRequest
import com.foodorder.server.models.meal.DraftMeal
import com.foodorder.server.models.meal.Meal
import com.foodorder.server.models.meal.MealConfig
import com.foodorder.server.models.meal.PrivateMealConfig
import com.foodorder.server.models.Order
import com.foodorder.server.request.CreateOrderRequest
import com.foodorder.server.request.CreateVenueRequest
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.IgnoreIf
import spock.lang.Specification

import java.time.Instant


@MicronautTest(environments = ["mock_auth"])
@IgnoreIf({System.getenv("requireIntegrationTests") != 'true'})
class MealIntegrationSpec extends Specification {
    @Inject
    MealClient mealClient

    @Inject
    OrderClient orderClient

    @Inject
    VenueClient venueClient

    def "Add meal"() {
        given:
        CreateMealRequest createMealRequest = new CreateMealRequest(name: "name", dateOfMeal: Instant.ofEpochSecond(1711405066), location: "London", venueName: "MacD")

        when:
        Meal meal = mealClient.addMeal(createMealRequest)

        then:
        assert meal.getMealDate() == Instant.ofEpochSecond(1711405066)
        assert meal.getSortKey().startsWith("2024-03-25T22:17:46Z")
        assert meal.getUid() == "steven"
        assert meal.getName() == "name"
        assert meal.getVenueName() == "MacD"
        assert meal.getLocation() == "London"
        assert meal.getId() != null
        assert meal.getClass() == Meal
    }

    def "Add draftMeal"() {
        given:
        CreateMealRequest createMealRequest = new CreateMealRequest(name: "name", dateOfMeal:  Instant.ofEpochSecond(1711405066), location:  "London", venueName:  "MacD", draft: true)

        when:
        Meal meal = mealClient.addMeal(createMealRequest)

        then:
        assert meal.getMealDate() == Instant.ofEpochSecond(1711405066)
        assert meal.getSortKey().startsWith("2024-03-25T22:17:46Z")
        assert meal.getUid() == "steven"
        assert meal.getName() == "name"
        assert meal.getVenueName() == "MacD"
        assert meal.getLocation() == "London"
        assert meal.getId() != null
        assert meal instanceof DraftMeal
    }


    def "Add private meal"() {
        CreatePrivateMealRequest createMealRequest = new CreatePrivateMealRequest(name:  "name", dateOfMeal:  Instant.ofEpochSecond(1711405066), location:  "London", venueName:  "MacD", numberOfOrders: 2)

        when:
        Meal meal = mealClient.addMeal(createMealRequest)
        List<Order> orders = mealClient.listAllOrdersForMeal(meal.getId())

        then:
        assert meal.getMealDate() == Instant.ofEpochSecond(1711405066)
        assert meal.getSortKey().startsWith("2024-03-25T22:17:46Z")
        assert meal.getUid() == "steven"
        assert meal.getName() == "name"
        assert meal.getVenueName() == "MacD"
        assert meal.getLocation() == "London"
        assert meal.getId() != null
        assert meal.getClass() == Meal
        assert orders.size() == 2
//        assert meal.getMealConfig().getPrivateMealConfig().getRecipientIds() == ["9def4c16-91fa-4de3-9673-bfb0b09cce81"].toSet()
    }

    def "Get Meal"() {
        given:
        CreateMealRequest createMealRequest = new CreateMealRequest(name:  "name", dateOfMeal:  Instant.ofEpochSecond(1711405066), location:  "London", venueName:  "MacD")

        when:
        Meal createMealResp = mealClient.addMeal(createMealRequest)
        Meal meal = mealClient.fetchMeal(createMealResp.sortKey)

        then:
        meal == createMealResp
    }

    def "get meals by mealDate and id"() {
        given:
        CreateMealRequest createMealRequest = new CreateMealRequest(name:  "name", dateOfMeal:  Instant.ofEpochSecond(1711405066), location:  "London", venueName:  "MacD")

        when:
        Meal createMealResp = mealClient.addMeal(createMealRequest)
        Meal meal = mealClient.getMeal(createMealResp.getMealDate().toEpochMilli(), createMealResp.getId()).get()

        then:
        meal == createMealResp
    }

    def "List all meals for current user"() {
        given:
        CreateMealRequest createMealRequest = new CreateMealRequest(name:  "name", dateOfMeal:  Instant.ofEpochSecond(1711405066), location:  "London", venueName:  "MacD")

        when:
        mealClient.addMeal(createMealRequest)
        List<Meal> mealList = mealClient.listAllMealsForUser()
        then:
        assert !mealList.isEmpty()
        assert mealList.every {Meal meal -> meal.uid == "steven"}
    }

    // Need to refactor the order query before this will work
    def "Ensure meal is deleted"() {
        given:
        CreateMealRequest createMealRequest = new CreateMealRequest(name:  "name", dateOfMeal:  Instant.ofEpochSecond(1711405066), location:  "London", venueName:  "name")

        when:
        Meal meal = mealClient.addMeal(createMealRequest)

        Set<MenuItem> menuItems = [new MenuItem(name: "name", description: "description", price: 1.0)]
        CreateVenueRequest createVenueRequest = new CreateVenueRequest(menuItems, "London", "name", "description")
        venueClient.addVenue(createVenueRequest)

        CreateOrderRequest createOrderRequest = new CreateOrderRequest(meal.getMealDate(), meal.getId(), menuItems, meal.getUid())
        orderClient.addOrder(createOrderRequest)

        mealClient.deleteMeal(
                meal.getMealDate(),
                meal.getId()
        )
        Meal mealAfterDelete = mealClient.fetchMeal(meal.getSortKey())
        List<Order> orderAfterDelete = mealClient.listAllOrdersForMeal(meal.getId())

        then:
        assert mealAfterDelete == null
        assert orderAfterDelete.size() == 0

    }
}
