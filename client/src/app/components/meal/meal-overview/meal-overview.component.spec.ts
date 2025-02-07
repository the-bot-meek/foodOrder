import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MealOverviewComponent } from './meal-overview.component';
import {of} from "rxjs";
import {IMeal} from "@the-bot-meek/food-orders-models/models/meal";
import {MealService} from "../../../shared/api/meal.service";
import {ActivatedRoute} from "@angular/router";
import {AuthService} from "../../../shared/services/auth/auth.service";
import {IUser} from "@the-bot-meek/food-orders-models/models/IUser";

describe('MealOverviewComponent', () => {
  let component: MealOverviewComponent;
  let fixture: ComponentFixture<MealOverviewComponent>;
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

  let user: any = {
    sub: "1234"
  }
  let authService = jasmine.createSpyObj<AuthService>("authService", ["checkAuth"], {
    get userDetails(): IUser {
      return user
    },
  })

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
      imports: [MealOverviewComponent],
      providers: [
        {
          provide: MealService,
          useValue: mealService
        },
        {
          provide: ActivatedRoute,
          useValue: route
        },
        {
          provide: AuthService,
          useValue: authService
        }
      ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(MealOverviewComponent);
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
