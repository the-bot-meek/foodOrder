import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddMenuDialogComponent } from './add-menu-dialog.component';
import {MenuService} from "../../../shared/api/menu.service";
import {MatDialogRef} from "@angular/material/dialog";
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {TestbedHarnessEnvironment} from "@angular/cdk/testing/testbed";
import {MatInputHarness} from "@angular/material/input/testing";
import {HarnessLoader} from "@angular/cdk/testing";
import {MatSelectHarness} from "@angular/material/select/testing";
import {MatButtonHarness} from "@angular/material/button/testing";
import {of} from "rxjs";

describe('AddMenuDialogComponent', () => {
  let component: AddMenuDialogComponent;
  let fixture: ComponentFixture<AddMenuDialogComponent>;
  let menuService: any;
  let dialogRef: any;
  let loader: HarnessLoader;

  beforeEach(async () => {
    menuService = {
      addMenu: jasmine.createSpy().and.returnValue(of())
    }

    dialogRef = {
      close: jasmine.createSpy().and.stub()
    }

    await TestBed.configureTestingModule({
      imports: [AddMenuDialogComponent, BrowserAnimationsModule],
      providers: [
        {
          provide: MenuService,
          useValue: menuService
        },
        {
          provide: MatDialogRef<AddMenuDialogComponent>,
          useValue: dialogRef
        }
      ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AddMenuDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
    loader = TestbedHarnessEnvironment.loader(fixture);
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should close dialog when menu is saved', async () => {
    const menuNameMatInputHarness: MatInputHarness = await loader.getHarness<MatInputHarness>(MatInputHarness.with({
      selector: '[data-testid="menu-name-input"]'
    }))
    await menuNameMatInputHarness.setValue("name")

    const locationMatSelectHarness: MatSelectHarness = await loader.getHarness<MatSelectHarness>(MatSelectHarness.with({
      selector: '[data-testid="menu-location-select"]'
    }))
    await locationMatSelectHarness.clickOptions({text: "London"})

    const descriptionMatInputHarness: MatInputHarness = await loader.getHarness<MatInputHarness>(MatInputHarness.with({
      selector: '[data-testid="menu-description-textarea"]'
    }))
    await descriptionMatInputHarness.setValue("Description")

    const phoneNumberInputHarness: MatInputHarness = await loader.getHarness<MatInputHarness>(MatInputHarness.with({
      selector: '[data-testid="menu-phone-input"]'
    }))
    await phoneNumberInputHarness.setValue("+44 20 7123 4567")

    const addMenuBtn: MatButtonHarness = await loader.getHarness<MatButtonHarness>(MatButtonHarness.with({
      selector: '[data-testid="add-menu-btn"]'
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

    expect(await addMenuBtn.isDisabled()).toBeFalse()
    expect(await menuItemAddMatBtnHarness.isDisabled()).toBeFalse()

    await menuItemAddMatBtnHarness.click()
    await addMenuBtn.click()


    expect(dialogRef.close).toHaveBeenCalled()
    expect(menuService.addMenu).toHaveBeenCalled()
  });
});
