import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AccreditedOrderComponent } from './accredited-order.component';
import {ActivatedRoute} from "@angular/router";
import {AuthService} from "../../../shared/services/auth/auth.service";
import SpyObj = jasmine.SpyObj;
import {MenuService} from "../../../shared/api/menu.service";
import {IUser} from "@the-bot-meek/food-orders-models/models/IUser";
import {of} from "rxjs";
import {IMeal} from "@the-bot-meek/food-orders-models/models/meal";

describe('AccreditedOrderComponentComponent', () => {
  let component: AccreditedOrderComponent;
  let fixture: ComponentFixture<AccreditedOrderComponent>;

  let route = {
    snapshot: {
      params: {
        menuName: "menuName",
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

  let menuService: SpyObj<MenuService> = jasmine.createSpyObj('menuService', ['getMealByMenuNameAndMealId'])
  let meal: IMeal = {
    id: "",
    location: "",
    mealConfig: undefined,
    mealDate: 0,
    name: "",
    primaryKey: "",
    sortKey: "",
    uid: "",
    menuName: ""

  }
  menuService.getMealByMenuNameAndMealId.and.returnValue(of(meal))

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AccreditedOrderComponent],
      providers: [
      {
        provide: MenuService,
        useValue: menuService
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

  it('expect getMealByMenuNameAndMealId to have been called', () => {
    expect(component).toBeTruthy();
    expect(menuService.getMealByMenuNameAndMealId).toHaveBeenCalledWith("menuName", "mealId")
  });
});
