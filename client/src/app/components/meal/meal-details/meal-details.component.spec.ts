import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MealDetailsComponent } from './meal-details.component';
import {of} from "rxjs";
import {IMeal} from "../../../../../models/meal";
import {MealService} from "../../../shared/api/meal.service";
import {ActivatedRoute} from "@angular/router";

describe('MealDetailsComponent', () => {
  let component: MealDetailsComponent;
  let fixture: ComponentFixture<MealDetailsComponent>;
  let route: any;
  let mealService: any;
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
    sortKey: "2024-07-01T20:09:35.796Z_6e30e2b2-e0dd-4345-8422-697e705c746b",
    uid: "8422-6e30e2b2-e0dd-4345-697e705c746b",
    venueName: "MacD"
  }
  let mealSk: string = "2024-07-01T20:09:35.796Z_6e30e2b2-e0dd-4345-8422-697e705c746b"

  beforeEach(async () => {
    route = {
      snapshot: {
        params: {
          sk: mealSk
        }
      }
    }

    mealService = {
      getMeal: jasmine.createSpy().and.returnValue(of(meal))
    }
    await TestBed.configureTestingModule({
      imports: [MealDetailsComponent],
      providers: [
        {
          provide: MealService,
          useValue: mealService
        },
        {
          provide: ActivatedRoute,
          useValue: route
        }
      ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(MealDetailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should create', async () => {
    await fixture.whenStable();
    expect(mealService.getMeal).toHaveBeenCalledWith(mealSk)
  })
});
