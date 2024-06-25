import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LandingPageComponent } from './landing-page.component';
import {MealService} from "../shared/api/meal.service";
import {IMeal} from "../../../models/meal";
import {of} from "rxjs";
import {TestbedHarnessEnvironment} from "@angular/cdk/testing/testbed";
import {HarnessLoader} from "@angular/cdk/testing";
import {MatRowHarness, MatTableHarness} from "@angular/material/table/testing";

describe('LandingPageComponent', () => {
  let component: LandingPageComponent;
  let fixture: ComponentFixture<LandingPageComponent>;
  let mealService;
  let meals: IMeal[] = [{
    id: '07405c0f-7f50-4e78-82b0-78f8d730d42b',
    uid: '7f50-07405c0f-4e78-82b0-78f8d730d42b',
    name: 'name',
    mealDate: 1718488580592,
    location: 'London',
    sortKey: 'sortKey": "2024-06-15T21:56:20.592Z_07405c0f-7f50-4e78-82b0-78f8d730d42b',
    primaryKey: '7f50-07405c0f-4e78-82b0-78f8d730d42b',
    venueName: 'MacD',
    mealConfig: {
      draft: false,
      privateMealConfig: undefined
    }
  }]
  let loader: HarnessLoader

  beforeEach(async () => {
    mealService = {listMeal: jasmine.createSpy().and.returnValue(of(meals))}
    await TestBed.configureTestingModule({
      imports: [LandingPageComponent],
      providers: [{
        provide: MealService,
        useValue: mealService
      }]
    })
    .compileComponents();

    fixture = TestBed.createComponent(LandingPageComponent);
    component = fixture.componentInstance;
    loader = TestbedHarnessEnvironment.loader(fixture);
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should add item to table', async () => {
    const matTableHarness: MatTableHarness = await loader.getHarness<MatTableHarness>(MatTableHarness.with({
      selector: '#meal-table'
    }))
    const rows: MatRowHarness[] = await matTableHarness.getRows()

    console.log(await rows[0].getCellTextByColumnName())
    expect(await rows[0].getCellTextByColumnName()).toEqual({
      name: 'name',
      mealDate: 'Jun 15, 2024',
      location: 'London',
      venueName: 'MacD'
    })
    expect(rows.length).toEqual(1)
  })
});
