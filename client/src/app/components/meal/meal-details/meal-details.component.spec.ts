import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MealDetailsComponent } from './meal-details.component';
import {HarnessLoader} from "@angular/cdk/testing";
import {TestbedHarnessEnvironment} from "@angular/cdk/testing/testbed";
import {AuthService} from "../../../shared/services/auth/auth.service";
import {IUser} from "../../../../../models/IUser";
import {MatButtonHarness} from "@angular/material/button/testing";
import {of} from "rxjs";

describe('MealDetailsComponent', () => {
  let component: MealDetailsComponent;
  let fixture: ComponentFixture<MealDetailsComponent>;
  let loader: HarnessLoader;

  let user: any = {
    sub: "1234"
  }
  let authService = jasmine.createSpyObj<AuthService>("authService", ["checkAuth"], {
    get userDetails(): IUser {
      return user
    },
  })

  beforeEach(async () => {

    await TestBed.configureTestingModule({
      imports: [MealDetailsComponent],
      providers: [{
        provide: AuthService,
        useValue: authService
      }]
    })
    .compileComponents();

    fixture = TestBed.createComponent(MealDetailsComponent);
    component = fixture.componentInstance;

    // @ts-ignore
    component.meal = of({uid: "1234"})

    fixture.detectChanges();
    loader = TestbedHarnessEnvironment.loader(fixture);

  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('isOwner',  (done) => {
    expect(component).toBeTruthy();
    component.isOwner.subscribe(async isOwner => {
      let matButtonHarness: MatButtonHarness = await loader.getHarness(MatButtonHarness.with({
        selector: "#edit-btn"
      }))
      expect(matButtonHarness).toBeTruthy()
      done()
    })
  });

  it('isNotOwner',  (done) => {
    expect(component).toBeTruthy();
    // @ts-ignore
    component.isOwner = of(false)
    component.isOwner.subscribe(async isOwner => {
      let matButtonHarness: MatButtonHarness[] = await loader.getAllHarnesses(MatButtonHarness.with({
        selector: "#edit-btn"
      }))
      expect(matButtonHarness.length).toEqual(0)
      done()
    })
  });
});
