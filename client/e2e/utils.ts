import {ICreateVenueRequest} from "../models/venue";
import {Page} from "@playwright/test";
import {ICreateMealRequest} from "../models/ICreateMealRequest";

export async function populateAddVenueDialog(createVenueRequest: ICreateVenueRequest, page: Page) {
  await page.getByTestId('venue-name-input').fill(createVenueRequest.name)
  await page.getByTestId('venue-location-select').click()
  await page.waitForSelector('mat-option')
  await page.click(`mat-option >> text="${createVenueRequest.location}"`)

  await page.getByTestId('venue-description-textarea').fill(createVenueRequest.description)
  for (const menuItem of createVenueRequest.menuItems) {
    await page.getByTestId('menu-item-name').fill(menuItem.name)
    await page.getByTestId('menu-item-description').fill(menuItem.description)
    await page.getByTestId('menu-item-price').fill(String(menuItem.price))
    await page.getByTestId('add-menu-item-btn').click()
  }
  await page.getByTestId('add-venue-btn').click()
}

export async function populateAddMealDialog(createMealRequest: ICreateMealRequest, page: Page) {
  await page.getByTestId('name-input').fill(createMealRequest.name)
  await page.getByTestId('meal-date-input').fill(new Date(createMealRequest.dateOfMeal).toLocaleDateString('en-US'))

  await page.getByTestId('location-select').click()
  await page.waitForSelector('mat-option')
  await page.click(`mat-option >> text="${createMealRequest.location}"`)

  await page.getByTestId('venue-select').click()
  await page.waitForSelector('mat-option')
  await page.click(`mat-option >> text="${createMealRequest.venueName}"`)

  if (createMealRequest.mealConfig.privateMealConfig) {
    await page.getByTestId('private-meal-checkbox').check()
    await page.getByTestId('number-of-recipients-input').fill('10')
  }
  await page.getByTestId('add-meal-button').click()
}

export async function openAddDialog(dialogName: 'meal' | 'venue', page: Page) {
  const addVenueBtn = page.getByTestId(`open-add-${dialogName}-btn-dialog`)
  await addVenueBtn.click()
}
