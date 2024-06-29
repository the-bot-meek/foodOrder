import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddMealDialogComponent } from './add-meal-dialog.component';
import {Observable, of} from "rxjs";
import {IVenue} from "../../../../../models/venue";
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

describe('AddMealDialogComponent', () => {
  let component: AddMealDialogComponent;
  let fixture: ComponentFixture<AddMealDialogComponent>;
  let loader: HarnessLoader
  let venueService: any;
  let mealService: any;
  let dialogRef: any;

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

  beforeEach(async () => {
    venueService = {
      listVenuesForLocation: jasmine.createSpy().and.callFake((location: string): Observable<IVenue[]> => {
        expect(location).toEqual("London")
        return of([venue])
      })
    }

    mealService = {
      addMeal: jasmine.createSpy().and.returnValue(of({}))
    }

    dialogRef = {
      close: jasmine.createSpy().and.stub()
    }
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
      selector: "#location-select"
    }))
    await locationMatSelectHarness.clickOptions({text: "London"})
    expect(await locationMatSelectHarness.getValueText()).toEqual("London")

  })

  it('Should not allow meal to be saved if form is invalid', async () => {
     const addMealMatButtonHarness: MatButtonHarness = await loader.getHarness<MatButtonHarness>(MatButtonHarness.with({
       selector: "#add-meal-button"
     }));

    expect(await addMealMatButtonHarness.isDisabled()).toEqual(true)
  })

  it("Should close dialog when meal is saved", async () => {
    const nameMatInputHarness: MatInputHarness = await loader.getHarness<MatInputHarness>(MatInputHarness.with({
      selector: "#name-input"
    }))
    await nameMatInputHarness.setValue("Name")

    const mealMatDatepickerInputHarness: MatDatepickerInputHarness = await loader.getHarness<MatDatepickerInputHarness>(MatDatepickerInputHarness.with({
      selector: "#meal-date-input"
    }))
    await mealMatDatepickerInputHarness.setValue("6/29/2024")

    const locationMatSelectHarness:MatSelectHarness = await loader.getHarness<MatSelectHarness>(MatSelectHarness.with({
      selector: "#location-select"
    }))
    await locationMatSelectHarness.clickOptions({text: "London"})

    const venueMatSelectHarness:MatSelectHarness = await loader.getHarness<MatSelectHarness>(MatSelectHarness.with({
      selector: "#venue-select"
    }))
    await venueMatSelectHarness.clickOptions({text: "Venue Name"})


    const addMealMatButtonHarness: MatButtonHarness = await loader.getHarness<MatButtonHarness>(MatButtonHarness.with({
      selector: "#add-meal-button"
    }));
    await addMealMatButtonHarness.click()
    expect(dialogRef.close).toHaveBeenCalled()
  })
});
