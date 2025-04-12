import {expect, test, TestInfo} from '@playwright/test';
import {
  addMeal,
  addMenu, buildCreateMealRequest, buildCreateMenuRequest,
  verifyMealTableRowExists
} from "./utils";
import {ICreateMenuRequest} from "@the-bot-meek/food-orders-models/models/menu";
import {ICreateMealRequest} from "@the-bot-meek/food-orders-models/models/ICreateMealRequest";
import {MealConfig} from "@the-bot-meek/food-orders-models/models/MealConfig";

test('has title', async ({ page }) => {
  await page.goto('/');

  // Expect a title "to contain" a substring.
  await expect(page).toHaveTitle("FoodOrder");
});


test('add menu test', async ({ page, browserName }, testInfo: TestInfo) => {
  await page.goto('/');
  const createMenuRequest: ICreateMenuRequest = buildCreateMenuRequest(testInfo, browserName)
  await addMenu(page, createMenuRequest)
});

test('test add meal', async ({ page, browserName }, testInfo: TestInfo) => {
  await page.goto('/');

  const mealConfig: MealConfig = {
      draft: false,
      privateMealConfig: undefined
  }
  const createMenuRequest: ICreateMenuRequest = buildCreateMenuRequest(testInfo, browserName)
  const createMealRequest: ICreateMealRequest = buildCreateMealRequest(mealConfig, testInfo, browserName)
  await addMenu(page, createMenuRequest)
  await addMeal(page, createMealRequest)
  await verifyMealTableRowExists(page, createMealRequest)
});
