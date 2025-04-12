
import {expect, Locator, Page, TestInfo} from "@playwright/test";
import {ICreateMealRequest} from "@the-bot-meek/food-orders-models/models/ICreateMealRequest";
import {ICreateMenuRequest} from "@the-bot-meek/food-orders-models/models/menu";
import {v4 as uuid} from "uuid";
import {MealConfig} from "@the-bot-meek/food-orders-models/models/MealConfig";

function capitalizeFirstLetter(str) {
  return str[0].toUpperCase() + str.slice(1);
}

export function buildCreateMenuRequest(testInfo: TestInfo, browser: string): ICreateMenuRequest  {
  return {
    name: `${testInfo.title} ${browser}`,
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
}

export function buildCreateMealRequest(mealConfig: MealConfig, testInfo: TestInfo, browser: string): ICreateMealRequest {
  return {
    dateOfMeal: 1728850944308,
    location: "London",
    mealConfig: mealConfig,
    name: `name-${uuid()}`,
    menuName: `${testInfo.title} ${browser}`
  }
}

export async function populateAddMenuDialog(createMenuRequest: ICreateMenuRequest, page: Page) {
  await page.getByTestId('menu-name-input').fill(createMenuRequest.name)
  await page.getByTestId('menu-location-select').click()
  await page.waitForSelector('mat-option')
  await page.click(`mat-option >> text="${createMenuRequest.location}"`)
  await page.getByTestId('menu-description-textarea').fill(createMenuRequest.description)
  await page.getByTestId('menu-phone-input').fill(createMenuRequest.phoneNumber)

  for (const menuItem of createMenuRequest.menuItems) {
    await page.getByTestId('menu-item-name').fill(menuItem.name)
    await page.getByTestId('menu-item-description').fill(menuItem.description)
    await page.getByTestId('menu-item-price').fill(String(menuItem.price))

    const menuItemSelectTile = capitalizeFirstLetter(menuItem.menuItemCategory.toLowerCase())
    await page.getByTestId('menu-item-category-select').click()
    await page.waitForSelector('mat-option')
    await page.click(`mat-option >> text="${menuItemSelectTile}"`)
    await page.getByTestId('add-menu-item-btn').click()
  }
  await page.getByTestId('add-menu-btn').click()
}

export async function populateAddMealDialog(createMealRequest: ICreateMealRequest, page: Page) {
  await page.getByTestId('name-input').fill(createMealRequest.name)
  await page.getByTestId('meal-date-input').fill(new Date(createMealRequest.dateOfMeal).toLocaleDateString('en-US'))

  await page.getByTestId('location-select').click()
  await page.waitForSelector('mat-option')
  await page.click(`mat-option >> text="${createMealRequest.location}"`)

  await page.getByTestId('menu-select').click()
  await page.waitForSelector('mat-option')
  await page.click(`mat-option >> text="${createMealRequest.menuName}"`)

  if (createMealRequest.mealConfig.privateMealConfig) {
    await page.getByTestId('private-meal-checkbox').check()
    await page.getByTestId('number-of-recipients-input').fill('10')
  }
  await page.getByTestId('add-meal-button').click()
}

export async function verifyMealTableRowExists(page: Page, createMealRequest: ICreateMealRequest) {
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
}

export async function openAddDialog(dialogName: 'meal' | 'menu', page: Page) {
  const addMenuBtn = page.getByTestId(`open-add-${dialogName}-btn-dialog`)
  await addMenuBtn.click()
}

export async function getMealTableCellFromTableRow(cellTestId: string, tableRow: Locator) {
  return tableRow.getByTestId(`meal-table-${cellTestId}-cell`)
}

export async function addMenu(page: Page, createMenuRequest: ICreateMenuRequest) {
  await openAddDialog('menu', page)
  await populateAddMenuDialog(createMenuRequest, page)
  expect(page.getByText('Menu Added')).toBeTruthy()
}

export async function addMeal(page: Page, createMealRequest: ICreateMealRequest) {
  await openAddDialog('meal', page)
  await populateAddMealDialog(createMealRequest, page)
  expect(page.getByText('Meal added')).toBeTruthy()
}
