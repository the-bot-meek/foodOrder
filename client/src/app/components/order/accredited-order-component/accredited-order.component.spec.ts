import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AccreditedOrderComponent } from './accredited-order.component';
import {ActivatedRoute} from "@angular/router";
import {AuthService} from "../../../shared/services/auth/auth.service";
import SpyObj = jasmine.SpyObj;
import {VenueService} from "../../../shared/api/venue.service";
import {IUser} from "@the-bot-meek/food-orders-models/models/IUser";
import {of} from "rxjs";
import {IMeal} from "@the-bot-meek/food-orders-models/models/meal";

describe('AccreditedOrderComponentComponent', () => {
  let component: AccreditedOrderComponent;
  let fixture: ComponentFixture<AccreditedOrderComponent>;

  let route = {
    snapshot: {
      params: {
        venueName: "venueName",
        mealId: "mealId"
      }
    }
  }

  let user: any = {
    sub: "1234"
  }
  let authService = jasmine.createSpyObj<AuthService>("authService", ["checkAuth"], {
    get userDetails(): IUser {
      return user
    },
  })

  let venueService: SpyObj<VenueService> = jasmine.createSpyObj('venueService', ['getMealByVenueNameAndMealId'])
  let meal: IMeal = {
    id: "",
    location: "",
    mealConfig: undefined,
    mealDate: 0,
    name: "",
    primaryKey: "",
    sortKey: "",
    uid: "",
    venueName: ""

  }
  venueService.getMealByVenueNameAndMealId.and.returnValue(of(meal))

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AccreditedOrderComponent],
      providers: [
      {
        provide: VenueService,
        useValue: venueService
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

    fixture = TestBed.createComponent(AccreditedOrderComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('expect getMealByVenueNameAndMealId to have been called', () => {
    expect(component).toBeTruthy();
    expect(venueService.getMealByVenueNameAndMealId).toHaveBeenCalledWith("venueName", "mealId")
  });
});
