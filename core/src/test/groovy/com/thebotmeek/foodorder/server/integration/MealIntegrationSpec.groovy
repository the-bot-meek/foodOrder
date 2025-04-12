package com.thebotmeek.foodorder.server.integration

import com.foodorder.server.client.MealClient
import com.foodorder.server.client.OrderClient
import com.foodorder.server.client.MenuClient
import com.foodorder.server.models.MenuItem
import com.foodorder.server.models.MenuItemCategory
import com.foodorder.server.request.CreateMealConfig
import com.foodorder.server.request.CreateMealRequest
import com.foodorder.server.models.meal.DraftMeal
import com.foodorder.server.models.meal.Meal
import com.foodorder.server.models.meal.MealConfig
import com.foodorder.server.models.meal.PrivateMealConfig
import com.foodorder.server.models.Order
import com.foodorder.server.request.CreateOrderRequest
import com.foodorder.server.request.CreateMenuRequest
import com.foodorder.server.request.CreatePrivateMealConfig
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
    MenuClient menuClient

    def "Add meal"() {
        given:
        CreateMealRequest createMealRequest = new CreateMealRequest(name: "name", dateOfMeal: Instant.ofEpochSecond(1711405066), location: "London", menuName: "MacD", createMealConfig: new CreateMealConfig())

        when:
        Meal meal = mealClient.addMeal(createMealRequest)

        then:
        assert meal.getMealDate() == Instant.ofEpochSecond(1711405066)
        assert meal.getSortKey().startsWith("2024-03-25T22:17:46Z")
        assert meal.getUid() == "steven"
        assert meal.getName() == "name"
        assert meal.getMenuName() == "MacD"
        assert meal.getLocation() == "London"
        assert meal.getId() != null
        assert meal.getClass() == Meal
        assert meal.getMealConfig().getClass() == MealConfig
    }

    def "Add draftMeal"() {
        given:
        CreateMealRequest createMealRequest = new CreateMealRequest(name: "name", dateOfMeal:  Instant.ofEpochSecond(1711405066), location:  "London", menuName:  "MacD", createMealConfig: new CreateMealConfig(draft: true))

        when:
        Meal meal = mealClient.addMeal(createMealRequest)

        then:
        assert meal.getMealDate() == Instant.ofEpochSecond(1711405066)
        assert meal.getSortKey().startsWith("2024-03-25T22:17:46Z")
        assert meal.getUid() == "steven"
        assert meal.getName() == "name"
        assert meal.getMenuName() == "MacD"
        assert meal.getLocation() == "London"
        assert meal.getId() != null
        assert meal instanceof DraftMeal
    }


    def "Add private meal"() {
        CreateMealRequest createMealRequest = new CreateMealRequest(
                name:  "name",
                dateOfMeal:  Instant.ofEpochSecond(1711405066),
                location:  "London",
                menuName:  "MacD",
                createMealConfig: new CreateMealConfig(
                        createPrivateMealConfig: new CreatePrivateMealConfig(5)
                )
        )

        when:
        Meal meal = mealClient.addMeal(createMealRequest)

        then:
        assert meal.getMealDate() == Instant.ofEpochSecond(1711405066)
        assert meal.getSortKey().startsWith("2024-03-25T22:17:46Z")
        assert meal.getUid() == "steven"
        assert meal.getName() == "name"
        assert meal.getMenuName() == "MacD"
        assert meal.getLocation() == "London"
        assert meal.getId() != null
        assert meal.getClass() == Meal
        assert meal.getMealConfig().getPrivateMealConfig().getRecipientIds().size() == 5
    }

    def "Get Meal"() {
        given:
        CreateMealRequest createMealRequest = new CreateMealRequest(name:  "name", dateOfMeal:  Instant.ofEpochSecond(1711405066), location:  "London", menuName:  "MacD", createMealConfig: new CreateMealConfig())

        when:
        Meal createMealResp = mealClient.addMeal(createMealRequest)
        Meal meal = mealClient.fetchMeal(createMealResp.sortKey)

        then:
        meal == createMealResp
    }

    def "get meals by mealDate and id"() {
        given:
        CreateMealRequest createMealRequest = new CreateMealRequest(name:  "name", dateOfMeal:  Instant.ofEpochSecond(1711405066), location:  "London", menuName:  "MacD", createMealConfig: new CreateMealConfig())

        when:
        Meal createMealResp = mealClient.addMeal(createMealRequest)
        Meal meal = mealClient.getMeal(createMealResp.getMealDate().toEpochMilli(), createMealResp.getId()).get()

        then:
        meal == createMealResp
    }

    def "List all meals for current user"() {
        given:
        CreateMealRequest createMealRequest = new CreateMealRequest(name:  "name", dateOfMeal:  Instant.ofEpochSecond(1711405066), location:  "London", menuName:  "MacD", createMealConfig: new CreateMealConfig())

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
        CreateMealRequest createMealRequest = new CreateMealRequest(name:  "name", dateOfMeal:  Instant.ofEpochSecond(1711405066), location:  "London", menuName:  "name", createMealConfig: new CreateMealConfig())

        when:
        Meal meal = mealClient.addMeal(createMealRequest)

        Set<MenuItem> menuItems = [new MenuItem(name: "name", description: "description", price: 1.0, menuItemCategory: MenuItemCategory.MAIN)]
        CreateMenuRequest createMenuRequest = new CreateMenuRequest(menuItems, "London", "name", "description", "+44 20 7123 4567")
        menuClient.addMenu(createMenuRequest)

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
