import {expect, Page, test} from '@playwright/test';
import {ICreateVenueRequest} from "../models/venue";

async function populateAddVenueDialog(createVenueRequest: ICreateVenueRequest, page: Page) {
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
}

test('has title', async ({ page }) => {
  await page.goto('/');

  // Expect a title "to contain" a substring.
  await expect(page).toHaveTitle("FoodOrder");
});


test('add venue test', async ({ page }) => {
  await page.goto('/');

  const addVenueBtn  = page.getByTestId('open-add-venue-btn-dialog')
  await addVenueBtn.click()
  const createVenueRequest: ICreateVenueRequest = {
    name: "add venue test",
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
  await page.getByTestId('add-venue-btn').click()
  expect(page.getByText('Venue Added')).toBeTruthy()
});
