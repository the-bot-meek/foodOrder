import {expect, test} from '@playwright/test';
import {ICreateVenueRequest} from "../models/venue";
import {ICreateMealRequest} from "../models/ICreateMealRequest";
import {getMealTableCellFromTableRow, openAddDialog, populateAddMealDialog, populateAddVenueDialog} from "./utils";
import {v4 as uuid} from 'uuid';

test('has title', async ({ page }) => {
  await page.goto('/');

  // Expect a title "to contain" a substring.
  await expect(page).toHaveTitle("FoodOrder");
});


test('add venue test', async ({ page, browserName }) => {
  await page.goto('/');

  await openAddDialog('venue', page)
  const createVenueRequest: ICreateVenueRequest = {
    name: `add venue test ${browserName}`,
    description: "description",
    location: "London",
    menuItems: [
      {
        name: 'name',
        description: 'description',
        price: 0.5
      }
    ]
  }
  await populateAddVenueDialog(createVenueRequest, page)
  expect(page.getByText('Venue Added')).toBeTruthy()
});

test('test add meal', async ({ page, browserName }) => {
  await page.goto('/');
  const createVenueRequest: ICreateVenueRequest = {
    name: `test add meal ${browserName}`,
    description: "description",
    location: "London",
    menuItems: [
      {
        name: 'name',
        description: 'description',
        price: 0.5
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
    venueName: `test add meal ${browserName}`
  }

  await openAddDialog('venue', page)
  await populateAddVenueDialog(createVenueRequest, page)
  expect(page.getByText('Venue Added')).toBeTruthy()

  await openAddDialog('meal', page)
  await populateAddMealDialog(createMealRequest, page)
  expect(page.getByText('Meal added')).toBeTruthy()



  const mealRow = page.getByTestId(`meal-row-${createMealRequest.name}`)
  await expect(mealRow).toBeVisible()
  const nameCell = await getMealTableCellFromTableRow('name', mealRow)
  const dateCell = await getMealTableCellFromTableRow('date', mealRow)
  const locationCell = await getMealTableCellFromTableRow('location', mealRow)
  const venueNameCell = await getMealTableCellFromTableRow('venueName', mealRow)

  await expect(nameCell).toHaveText(createMealRequest.name)
  await expect(dateCell).toHaveText(new Date(createMealRequest.dateOfMeal).toLocaleDateString('en-US', {year: 'numeric', month: 'short', day: 'numeric',}))
  await expect(locationCell).toHaveText(createMealRequest.location)
  await expect(venueNameCell).toHaveText(createMealRequest.venueName)
});
