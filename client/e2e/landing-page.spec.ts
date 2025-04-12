import {expect, test} from '@playwright/test';
import {
  addMeal,
  addMenu,
  getMealTableCellFromTableRow,
  openAddDialog,
  populateAddMealDialog, verifyMealTableRowExists
} from "./utils";
import {v4 as uuid} from 'uuid';
import {ICreateMenuRequest} from "@the-bot-meek/food-orders-models/models/menu";
import {ICreateMealRequest} from "@the-bot-meek/food-orders-models/models/ICreateMealRequest";

test('has title', async ({ page }) => {
  await page.goto('/');

  // Expect a title "to contain" a substring.
  await expect(page).toHaveTitle("FoodOrder");
});


test('add menu test', async ({ page, browserName }) => {
  await page.goto('/');


  const createMenuRequest: ICreateMenuRequest = {
    name: `add menu test ${browserName}`,
    description: "description",
    location: "London",
    phoneNumber: "+44 20 7123 4567",
    menuItems: [
      {
        name: 'name',
        description: 'description',
        price: 0.5,
        menuItemCategory: "STARTER"
      }
    ]
  }
  await addMenu(page, createMenuRequest)
});

test('test add meal', async ({ page, browserName }) => {
  await page.goto('/');
  const createMenuRequest: ICreateMenuRequest = {
    name: `test add meal ${browserName}`,
    description: "description",
    location: "London",
    phoneNumber: '+44 20 7123 4567',
    menuItems: [
      {
        name: 'name',
        description: 'description',
        price: 0.5,
        menuItemCategory: "STARTER"
      }
    ]
  }

  const createMealRequest: ICreateMealRequest = {
    dateOfMeal: 1728850944308,
    location: "London",
    mealConfig: {
      draft: false,
      privateMealConfig: undefined
    },
    name: `name-${uuid()}`,
    menuName: `test add meal ${browserName}`
  }

  await addMenu(page, createMenuRequest)
  await addMeal(page, createMealRequest)
  await verifyMealTableRowExists(page, createMealRequest)
});
