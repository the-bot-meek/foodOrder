import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddMealDialogComponent } from './add-meal-dialog.component';
import {EMPTY, Observable, of} from "rxjs";
import {IVenue} from "@the-bot-meek/food-orders-models/models/venue";
import {TestbedHarnessEnvironment} from "@angular/cdk/testing/testbed";
import {VenueService} from "../../../shared/api/venue.service";
import {MealService} from "../../../shared/api/meal.service";
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {HarnessLoader} from "@angular/cdk/testing";
import {MatSelectHarness} from "@angular/material/select/testing";
import {MatButtonHarness} from "@angular/material/button/testing";
import {MatDialogRef} from "@angular/material/dialog";
import {MatDatepickerInputHarness} from "@angular/material/datepicker/testing";
import {MatInputHarness} from "@angular/material/input/testing";
import {MatSnackBar} from "@angular/material/snack-bar";
import {Router} from "@angular/router";
import {IMeal} from "@the-bot-meek/food-orders-models/models/meal";
import {ICreateMealRequest} from "@the-bot-meek/food-orders-models/models/ICreateMealRequest";
import {MatCheckboxHarness} from "@angular/material/checkbox/testing";
import SpyObj = jasmine.SpyObj;
import {UUIDService} from "../../../shared/utils/uuid.service";

describe('AddMealDialogComponent', () => {
  let component: AddMealDialogComponent;
  let fixture: ComponentFixture<AddMealDialogComponent>;
  let loader: HarnessLoader
  let venueService: any;
  let mealService: any;
  let dialogRef: any;
  let snackBar: any;
  let router: any;
  let matSnackBarRef: any;
  let uuidService: SpyObj<UUIDService>;

  let venue: IVenue = {
    description: "description",
    id: "569fd798-2b1b-4864-8269-65115a5c94c1",
    location: "London",
    menuItems: [{
      name: 'name',
      description: 'description',
      price: 1.01
    }],
    name: "Venue Name"
  }

  let mealSortKey = "2024-07-01T20:09:35.796Z_6e30e2b2-e0dd-4345-8422-697e705c746b"
  let meal: IMeal = {
    id: "6e30e2b2-e0dd-4345-8422-697e705c746b",
    location: "London",
    mealConfig: {
      draft: false,
      privateMealConfig: undefined
    },
    mealDate: 1719865709058,
    name: "name",
    primaryKey: "8422-6e30e2b2-e0dd-4345-697e705c746b",
    sortKey: mealSortKey,
    uid: "8422-6e30e2b2-e0dd-4345-697e705c746b",
    venueName: "MacD"
  }

  beforeEach(async () => {
    venueService = {
      listVenuesForLocation: jasmine.createSpy().and.callFake((location: string): Observable<IVenue[]> => {
        expect(location).toEqual("London")
        return of([venue])
      })
    }

    mealService = {
      addMeal: jasmine.createSpy().and.returnValue(of(meal)),
      listMeal: jasmine.createSpy().and.returnValue(of([]))
    }

    dialogRef = {
      close: jasmine.createSpy().and.stub()
    }

    matSnackBarRef = {
      onAction: jasmine.createSpy().and.returnValue(of(EMPTY))
    }

    snackBar = {
      open: jasmine.createSpy("snackBar open").and.returnValue(matSnackBarRef)
    }

    router = {
      navigate: jasmine.createSpy("navigate spy").and.stub()
    }

    uuidService = jasmine.createSpyObj("uuid spy", ["randomUUID"])
    uuidService.randomUUID = jasmine.createSpy().and.returnValue("b29dca60-2373-4fae-829d-1dd9f2425de3")



    await TestBed.configureTestingModule({
      imports: [AddMealDialogComponent, BrowserAnimationsModule],
      providers: [
        {
          provide: VenueService,
          useValue: venueService
        },
        {
          provide: MatDialogRef<AddMealDialogComponent>,
          useValue: dialogRef
        },
        {
          provide: MealService,
          useValue: mealService
        },
        {
          provide: MatSnackBar,
          useValue: snackBar
        },
        {
          provide: Router,
          useValue: router
        },
        {
          provide: UUIDService,
          useValue: uuidService
        }
      ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AddMealDialogComponent);
    component = fixture.componentInstance;
    loader = TestbedHarnessEnvironment.loader(fixture);
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('Should request venues for location', async () => {
    const locationMatSelectHarness: MatSelectHarness = await loader.getHarness<MatSelectHarness>(MatSelectHarness.with({
      selector: '[data-testid="location-select"]'
    }))
    await locationMatSelectHarness.clickOptions({text: "London"})
    expect(await locationMatSelectHarness.getValueText()).toEqual("London")

  })

  it('Should not allow meal to be saved if form is invalid', async () => {
     const addMealMatButtonHarness: MatButtonHarness = await loader.getHarness<MatButtonHarness>(MatButtonHarness.with({
       selector: '[data-testid="add-meal-button"]'
     }));

    expect(await addMealMatButtonHarness.isDisabled()).toEqual(true)
  })

  it("Should close dialog when meal is saved", async () => {
    const nameMatInputHarness: MatInputHarness = await loader.getHarness<MatInputHarness>(MatInputHarness.with({
      selector: '[data-testid="name-input"]'
    }))
    await nameMatInputHarness.setValue("Name")

    const mealMatDatepickerInputHarness: MatDatepickerInputHarness = await loader.getHarness<MatDatepickerInputHarness>(MatDatepickerInputHarness.with({
      selector: '[data-testid="meal-date-input"]'
    }))
    await mealMatDatepickerInputHarness.setValue("6/29/2024")

    const locationMatSelectHarness:MatSelectHarness = await loader.getHarness<MatSelectHarness>(MatSelectHarness.with({
      selector: '[data-testid="location-select"]'
    }))
    await locationMatSelectHarness.clickOptions({text: "London"})

    const venueMatSelectHarness:MatSelectHarness = await loader.getHarness<MatSelectHarness>(MatSelectHarness.with({
      selector: '[data-testid="venue-select"]'
    }))
    await venueMatSelectHarness.clickOptions({text: "Venue Name"})


    const addMealMatButtonHarness: MatButtonHarness = await loader.getHarness<MatButtonHarness>(MatButtonHarness.with({
      selector: '[data-testid="add-meal-button"]'
    }));
    await addMealMatButtonHarness.click()

    let createMealRequest: ICreateMealRequest = {
      dateOfMeal: 1719619200000 + (new Date().getTimezoneOffset() * 60000),
      location: 'London',
      mealConfig: {
        draft: false,
        privateMealConfig: null
      },
      name: 'Name',
      venueName: 'Venue Name'
    }

    expect(dialogRef.close).toHaveBeenCalled()
    expect(mealService.listMeal).toHaveBeenCalled()
    expect(mealService.addMeal).toHaveBeenCalledWith(createMealRequest)
    expect(snackBar.open).toHaveBeenCalledWith("Meal added", "Open Meal", jasmine.any(Object))
    expect(router.navigate).toHaveBeenCalledWith(['meal', mealSortKey])
  })

  it('Should not allow meal to be saved if form is invalid', async () => {
    const addMealMatButtonHarness: MatButtonHarness = await loader.getHarness<MatButtonHarness>(MatButtonHarness.with({
      selector: '[data-testid="add-meal-button"]'
    }));

    expect(await addMealMatButtonHarness.isDisabled()).toEqual(true)
  })

  it("Should close dialog when meal is saved", async () => {
    const nameMatInputHarness: MatInputHarness = await loader.getHarness<MatInputHarness>(MatInputHarness.with({
      selector: '[data-testid="name-input"]'
    }))
    await nameMatInputHarness.setValue("Name")

    const mealMatDatepickerInputHarness: MatDatepickerInputHarness = await loader.getHarness<MatDatepickerInputHarness>(MatDatepickerInputHarness.with({
      selector: '[data-testid="meal-date-input"]'
    }))
    await mealMatDatepickerInputHarness.setValue("6/29/2024")

    const locationMatSelectHarness:MatSelectHarness = await loader.getHarness<MatSelectHarness>(MatSelectHarness.with({
      selector: '[data-testid="location-select"]'
    }))
    await locationMatSelectHarness.clickOptions({text: "London"})

    const venueMatSelectHarness:MatSelectHarness = await loader.getHarness<MatSelectHarness>(MatSelectHarness.with({
      selector: '[data-testid="venue-select"]'
    }))
    await venueMatSelectHarness.clickOptions({text: "Venue Name"})

    const privateMealCheckBox: MatCheckboxHarness = await loader.getHarness<MatCheckboxHarness>(MatCheckboxHarness.with({
      selector: '[data-testid="private-meal-checkbox"]'
    }))
    await privateMealCheckBox.check()

    const numberOfRecipients: MatInputHarness = await loader.getHarness<MatInputHarness>(MatInputHarness.with({
      selector: '[data-testid="number-of-recipients-input"]'
    }))
    await numberOfRecipients.setValue("1")

    const addMealMatButtonHarness: MatButtonHarness = await loader.getHarness<MatButtonHarness>(MatButtonHarness.with({
      selector: '[data-testid="add-meal-button"]'
    }));
    await addMealMatButtonHarness.click()

    let createMealRequest: ICreateMealRequest = {
      dateOfMeal: 1719619200000 + (new Date().getTimezoneOffset() * 60000),
      location: 'London',
      mealConfig: {
        draft: false,
        privateMealConfig: {
          recipientIds: ["b29dca60-2373-4fae-829d-1dd9f2425de3"]
        }
      },
      name: 'Name',
      venueName: 'Venue Name'
    }

    expect(uuidService.randomUUID).toHaveBeenCalled()
    expect(dialogRef.close).toHaveBeenCalled()
    expect(mealService.listMeal).toHaveBeenCalled()
    expect(mealService.addMeal).toHaveBeenCalledWith(createMealRequest)
    expect(snackBar.open).toHaveBeenCalledWith("Meal added", "Open Meal", jasmine.any(Object))
    expect(router.navigate).toHaveBeenCalledWith(['meal', mealSortKey])
  })
});
