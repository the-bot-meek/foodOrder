import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ShellComponent } from './shell.component';
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {AuthService} from "../services/auth/auth.service";
import {TestbedHarnessEnvironment} from "@angular/cdk/testing/testbed";
import {HarnessLoader} from "@angular/cdk/testing";
import {MatSidenavContainerHarness} from "@angular/material/sidenav/testing";
import {IUser} from "../../../models/IUser";

let user: IUser = {
  active: false,
  email: "",
  email_verified: false,
  exp: 0,
  family_name: "",
  given_name: "",
  iat: 0,
  iss: "",
  name: "",
  nbf: 0,
  foodorder_roles: ["FoodOrderAdminUser"],
  nickname: "",
  nonce: "",
  oauth2Provider: "",
  picture: "",
  sid: "",
  sub: "",
  updated_at: "",
  username: ""
};

describe('ShellComponent failed', () => {
  let component: ShellComponent;
  let fixture: ComponentFixture<ShellComponent>;
  let authService = jasmine.createSpyObj<AuthService>("authService", ["checkAuth"], {
    get userDetails(): IUser {
      return user
    },
  })
  let loader: HarnessLoader

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ShellComponent, BrowserAnimationsModule],
      providers: [
        {
          provide: AuthService,
          useValue: authService
        }
      ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ShellComponent);

    component = fixture.componentInstance;
    fixture.detectChanges();
    loader = TestbedHarnessEnvironment.loader(fixture);
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('expect side nav to be present', async () => {
    const matSidenavContainerHarness: MatSidenavContainerHarness = await loader.getHarness<MatSidenavContainerHarness>(MatSidenavContainerHarness.with({
      selector: ".sidenav-container"
    }))
    expect(matSidenavContainerHarness).toBeTruthy()
  })
});


describe('ShellComponent', () => {
  let component: ShellComponent;
  let fixture: ComponentFixture<ShellComponent>;
  let authService = jasmine.createSpyObj<AuthService>("authService", ["checkAuth"], {
    get userDetails(): IUser {
      return user
    },
  })
  let loader: HarnessLoader

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ShellComponent, BrowserAnimationsModule],
      providers: [
        {
          provide: AuthService,
          useValue: authService
        }
      ]
    })
      .compileComponents();

    fixture = TestBed.createComponent(ShellComponent);

    component = fixture.componentInstance;
    fixture.detectChanges();
    loader = TestbedHarnessEnvironment.loader(fixture);
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('expect side nav to be present', async () => {
    const matSidenavContainerHarness: MatSidenavContainerHarness = await loader.getHarness<MatSidenavContainerHarness>(MatSidenavContainerHarness.with({
      selector: ".sidenav-container"
    }))
    expect(matSidenavContainerHarness).toBeTruthy()
  })
});
