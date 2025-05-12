import { TestBed } from '@angular/core/testing';

import { AuthService } from './auth.service';
import {catchError, EMPTY, of, throwError} from "rxjs";
import {UserService} from "../../api/user.service";
import {AuthenticatorService} from "./authenticator.service";
import {IUser} from "../../../../models/IUser";

describe('AuthService', () => {
  let service: AuthService;
  let userService;
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

  beforeEach(() => {
    userService = {getUserInfo: jasmine.createSpy().and.returnValue(of(user))}
    // jasmine.clock().install()
    jasmine.clock().mockDate(new Date(1726439057853))
    user.exp = 1726439057 + 1000
    TestBed.configureTestingModule({
      providers: [
        {
          provide: UserService,
          useValue: userService
        }
      ]
    });
    service = TestBed.inject(AuthService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should only check user info end point once', (done) => {
    service.checkAuth().subscribe(() => {
      service.checkAuth().subscribe(() => {
        expect(userService.getUserInfo).toHaveBeenCalledTimes(1)
        done()
      })
    })

  })
});

describe('AuthService unauthorised', () => {
  let service: AuthService;
  let userService;
  let authenticatorService: any;

  beforeEach(() => {
    userService = {getUserInfo: jasmine.createSpy().and.returnValue(throwError('woops'))}
    authenticatorService = {auth: jasmine.createSpy().and.stub()}
    TestBed.configureTestingModule({
      providers: [
        {
          provide: UserService,
          useValue: userService
        },
        {
          provide: AuthenticatorService,
          useValue: authenticatorService
        }
      ]
    });
    service = TestBed.inject(AuthService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should redirect to auth path if user call fails', (done: DoneFn) => {
    service.checkAuth().pipe(catchError(() => {
      expect(authenticatorService.auth).toHaveBeenCalled()
      done()
      return EMPTY
    })).subscribe()
  });
});
