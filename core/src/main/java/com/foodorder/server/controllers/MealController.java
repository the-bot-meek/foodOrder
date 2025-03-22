package com.foodorder.server.controllers;

import com.foodorder.server.converters.CreateMealRequestConverter;
import com.foodorder.server.exceptions.MealRequestConverterException;
import com.foodorder.server.request.CreateMealRequest;
import com.foodorder.server.models.meal.Meal;
import com.foodorder.server.repository.MealRepository;
import com.foodorder.server.repository.OrderRepository;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.*;
import io.micronaut.http.exceptions.HttpStatusException;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.rules.SecurityRule;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.Instant;
import java.util.List;
import java.util.Optional;


@Controller("meal")
@Secured(SecurityRule.IS_AUTHENTICATED)
public class MealController {
    private final MealRepository mealRepository;
    private final Logger log = LoggerFactory.getLogger(MealController.class);
    private final OrderRepository orderRepository;
    private final CreateMealRequestConverter createMealRequestConverter;

    MealController(
            MealRepository mealRepository,
            OrderRepository orderRepository,
            CreateMealRequestConverter createMealRequestConverter
    ) {
        this.mealRepository = mealRepository;
        this.orderRepository = orderRepository;
        this.createMealRequestConverter = createMealRequestConverter;
    }
    @Post
    public HttpResponse<Meal> addMeal(@Valid @Body CreateMealRequest createMealRequest, Authentication authentication) {
        try {
            log.info("Adding new Meal. CreateMealRequest: {}, uid: {}", createMealRequest, authentication.getName());
            Meal meal = createMealRequestConverter.convertCreateMealRequestToNewMeal(createMealRequest, authentication.getName());
            mealRepository.saveMeal(meal);
            return HttpResponse.ok(meal);
        } catch (MealRequestConverterException e) {
            log.error("Error adding Meal", e);
            throw new HttpStatusException(HttpStatus.BAD_REQUEST, e);
        }
    }

    @Get("{mealSortKey}")
    public Optional<Meal> getMeal(@NotNull @NotBlank String mealSortKey, Authentication authentication) {
        log.info("Getting Meal. mealSortKey: {}, uid:{}", mealSortKey, authentication.getName());
        return mealRepository.getMeal(authentication.getName(), mealSortKey);
    }

    @Get("{mealDateTimeStamp}/{id}")
    public Optional<Meal> getMeal(long mealDateTimeStamp, @NotNull String id, Authentication authentication) {
        Instant mealDate = Instant.ofEpochMilli(mealDateTimeStamp);
        log.info("Getting Meal. mealDate: {}, id: {}, uid: {}", mealDate, id, authentication.getName());
        return mealRepository.getMeal(authentication.getName(), mealDate + "_" + id);
    }

    @Get
    public List<Meal> listMeals(Authentication authentication) {
        log.info("Getting all meals for uid: {}", authentication.getName());
        return mealRepository.getListOfMeals(authentication.getName());
    }

    @Delete
    public void deleteMeal(
            @NotNull Authentication authentication,
            @NotNull @QueryValue Instant mealDate,
            @NotNull @NotEmpty @QueryValue String id
    ) {
        log.info("Deleting meal. uid: {}, mealDate: {}, id:{}", authentication.getName(), mealDate, id);
        mealRepository.deleteMeal(authentication.getName(), mealDate, id);
        log.info("Deleting all order for mealId: {}", id);
        orderRepository.deleteAllOrdersForMeal(id);
    }
}
