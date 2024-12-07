import {ICreateVenueRequest} from "../models/venue";
import {expect, Locator, Page} from "@playwright/test";
import {ICreateMealRequest, ICreatePrivateMealRequest} from "../models/ICreateMealRequest";
import {IMeal, IMealDisplayValues} from "../models/meal";

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

  if (createMealRequest.mealType == 'PrivateMeal') {
    await page.getByTestId('private-meal-checkbox').check()
    await page.getByTestId('number-of-recipients-input').fill('10')
  }
  await page.getByTestId('add-meal-button').click()
}

export async function openAddDialog(dialogName: 'meal' | 'venue', page: Page) {
  const addVenueBtn = page.getByTestId(`open-add-${dialogName}-btn-dialog`)
  await addVenueBtn.click()
}

export async function getMealTableCellFromTableRow(cellTestId: string, tableRow: Locator) {
  return tableRow.getByTestId(`meal-table-${cellTestId}-cell`)
}

export async function getMealDetailsFromTableRow(mealName: string, page: Page): Promise<IMealDisplayValues> {
  const mealRow = page.getByTestId(`meal-row-${mealName}`)
  await expect(mealRow).toBeVisible()

  const nameCell = await getMealTableCellFromTableRow('name', mealRow)
  const dateCell = await getMealTableCellFromTableRow('date', mealRow)
  const locationCell = await getMealTableCellFromTableRow('location', mealRow)
  const venueNameCell = await getMealTableCellFromTableRow('venueName', mealRow)
  const venueVisibilityCell = await getMealTableCellFromTableRow('visibility', mealRow)
  const isPrivate = await venueVisibilityCell.locator('mat-icon').getAttribute('fontIcon') === 'visibility_off'

  return {
    name: await nameCell.textContent(),
    date: await dateCell.textContent(),
    location: await locationCell.textContent(),
    venue: await venueNameCell.textContent(),
    private: isPrivate
  }
}
