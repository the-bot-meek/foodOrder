import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddVenueDialogComponent } from './add-venue-dialog.component';
import {VenueService} from "../../../shared/api/venue.service";
import {MatDialogRef} from "@angular/material/dialog";
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {TestbedHarnessEnvironment} from "@angular/cdk/testing/testbed";
import {MatInputHarness} from "@angular/material/input/testing";
import {HarnessLoader} from "@angular/cdk/testing";
import {MatSelectHarness} from "@angular/material/select/testing";
import {MatButtonHarness} from "@angular/material/button/testing";
import {of} from "rxjs";

describe('AddVenueDialogComponent', () => {
  let component: AddVenueDialogComponent;
  let fixture: ComponentFixture<AddVenueDialogComponent>;
  let venueService: any;
  let dialogRef: any;
  let loader: HarnessLoader;

  beforeEach(async () => {
    venueService = {
      addVenue: jasmine.createSpy().and.returnValue(of())
    }

    dialogRef = {
      close: jasmine.createSpy().and.stub()
    }

    await TestBed.configureTestingModule({
      imports: [AddVenueDialogComponent, BrowserAnimationsModule],
      providers: [
        {
          provide: VenueService,
          useValue: venueService
        },
        {
          provide: MatDialogRef<AddVenueDialogComponent>,
          useValue: dialogRef
        }
      ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AddVenueDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
    loader = TestbedHarnessEnvironment.loader(fixture);
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should close dialog when venue is saved', async () => {
    const venueNameMatInputHarness: MatInputHarness = await loader.getHarness<MatInputHarness>(MatInputHarness.with({
      selector: '[data-testid="venue-name-input"]'
    }))
    await venueNameMatInputHarness.setValue("name")

    const locationMatSelectHarness: MatSelectHarness = await loader.getHarness<MatSelectHarness>(MatSelectHarness.with({
      selector: '[data-testid="venue-location-select"]'
    }))
    await locationMatSelectHarness.clickOptions({text: "London"})

    const descriptionMatInputHarness: MatInputHarness = await loader.getHarness<MatInputHarness>(MatInputHarness.with({
      selector: '[data-testid="venue-description-textarea"]'
    }))
    await descriptionMatInputHarness.setValue("Description")

    const phoneNumberInputHarness: MatInputHarness = await loader.getHarness<MatInputHarness>(MatInputHarness.with({
      selector: '[data-testid="venue-phone-input"]'
    }))
    await phoneNumberInputHarness.setValue("+44 20 7123 4567")

    const addVenueBtn: MatButtonHarness = await loader.getHarness<MatButtonHarness>(MatButtonHarness.with({
      selector: '[data-testid="add-venue-btn"]'
    }))

    const menuItemNameMatInputHarness: MatInputHarness = await loader.getHarness<MatInputHarness>(MatInputHarness.with({
      selector: '[data-testid="menu-item-name"]'
    }))
    await menuItemNameMatInputHarness.setValue("Menu Item Name")

    const menuItemDescriptionMatInputHarness: MatInputHarness = await loader.getHarness<MatInputHarness>(MatInputHarness.with({
      selector: '[data-testid="menu-item-description"]'
    }))
    await menuItemDescriptionMatInputHarness.setValue("Menu Item Description")

    const menuItemPriceMatInputHarness: MatInputHarness = await loader.getHarness<MatInputHarness>(MatInputHarness.with({
      selector: '[data-testid="menu-item-price"]'
    }))
    await menuItemPriceMatInputHarness.setValue("5")

    const menuItemAddMatBtnHarness: MatButtonHarness = await loader.getHarness<MatButtonHarness>(MatButtonHarness.with({
      selector: '[data-testid="add-menu-item-btn"]'
    }))

    expect(await addVenueBtn.isDisabled()).toBeFalse()
    expect(await menuItemAddMatBtnHarness.isDisabled()).toBeFalse()

    await menuItemAddMatBtnHarness.click()
    await addVenueBtn.click()


    expect(dialogRef.close).toHaveBeenCalled()
    expect(venueService.addVenue).toHaveBeenCalled()
  });
});
