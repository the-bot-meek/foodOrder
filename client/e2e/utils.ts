
import {Locator, Page} from "@playwright/test";
import {ICreateMealRequest} from "@the-bot-meek/food-orders-models/models/ICreateMealRequest";
import {ICreateMenuRequest} from "@the-bot-meek/food-orders-models/models/menu";


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

export async function openAddDialog(dialogName: 'meal' | 'menu', page: Page) {
  const addMenuBtn = page.getByTestId(`open-add-${dialogName}-btn-dialog`)
  await addMenuBtn.click()
}

export async function getMealTableCellFromTableRow(cellTestId: string, tableRow: Locator) {
  return tableRow.getByTestId(`meal-table-${cellTestId}-cell`)
}
