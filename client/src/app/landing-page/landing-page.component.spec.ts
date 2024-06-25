import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LandingPageComponent } from './landing-page.component';
import {MealService} from "../shared/api/meal.service";
import {IMeal} from "../../../models/meal";
import {of} from "rxjs";

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
    primaryKey: '7f50-07405c0f-4e78-82b0-78f8d730d42b'
  }]

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
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
