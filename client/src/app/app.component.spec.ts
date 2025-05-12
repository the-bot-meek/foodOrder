import { TestBed } from '@angular/core/testing';
import { AppComponent } from './app.component';
import {AuthService} from "./shared/services/auth/auth.service";
import {of} from "rxjs";
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {IUser} from "../models/IUser";

describe('AppComponent', () => {
  let authService: any;
  let document;
  let user: IUser = {
    active: true,
    name: "name",
    sub: "",
    email: "",
    email_verified: false,
    exp: 0,
    family_name: "",
    given_name: "",
    iat: 0,
    iss: "",
    nbf: 0,
    nickname: "",
    nonce: "",
    oauth2Provider: "",
    picture: "",
    sid: "",
    updated_at: "",
    username: "",
    foodorder_roles: ["FoodOrderAdminUser"],
  }

  beforeEach(async () => {
    document = {
      location: {
        href: ""
      }
    }
    authService = {checkAuth: jasmine.createSpy().and.returnValue(of(user))}
    await TestBed.configureTestingModule({
      imports: [AppComponent, BrowserAnimationsModule],
      providers: [
        {
          provide: AuthService,
          useValue: authService
        },
        {
          provide: Document,
          useValue: document
        }
      ],
    }).compileComponents();
  });

  it('should create the app', () => {
    const fixture = TestBed.createComponent(AppComponent);
    const app = fixture.componentInstance;
    expect(app).toBeTruthy();
  });

  it(`should have the 'foodOrder' title`, () => {
    const fixture = TestBed.createComponent(AppComponent);
    const app = fixture.componentInstance;
    expect(app.title).toEqual('foodOrder');
  });
});
