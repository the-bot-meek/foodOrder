import {expect, test} from '@playwright/test';
import {getMealTableCellFromTableRow, openAddDialog, populateAddMealDialog, populateAddMenuDialog} from "./utils";
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

  await openAddDialog('menu', page)
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
  await populateAddMenuDialog(createMenuRequest, page)
  expect(page.getByText('Menu Added')).toBeTruthy()
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

  await openAddDialog('menu', page)
  await populateAddMenuDialog(createMenuRequest, page)
  expect(page.getByText('Menu Added')).toBeTruthy()

  await openAddDialog('meal', page)
  await populateAddMealDialog(createMealRequest, page)
  expect(page.getByText('Meal added')).toBeTruthy()



  const mealRow = page.getByTestId(`meal-row-${createMealRequest.name}`)
  await expect(mealRow).toBeVisible()
  const nameCell = await getMealTableCellFromTableRow('name', mealRow)
  const dateCell = await getMealTableCellFromTableRow('date', mealRow)
  const locationCell = await getMealTableCellFromTableRow('location', mealRow)
  const menuNameCell = await getMealTableCellFromTableRow('menuName', mealRow)

  await expect(nameCell).toHaveText(createMealRequest.name)
  await expect(dateCell).toHaveText(new Date(createMealRequest.dateOfMeal).toLocaleDateString('en-US', {year: 'numeric', month: 'short', day: 'numeric',}))
  await expect(locationCell).toHaveText(createMealRequest.location)
  await expect(menuNameCell).toHaveText(createMealRequest.menuName)
});
