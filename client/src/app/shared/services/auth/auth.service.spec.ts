import { TestBed } from '@angular/core/testing';

import { AuthService } from './auth.service';
import {catchError, EMPTY, of, throwError} from "rxjs";
import {IUser} from "../../../../../models/IUser";
import {UserService} from "../../api/user.service";
import {AuthenticatorService} from "./authenticator.service";

describe('AuthService', () => {
  let service: AuthService;
  let userService;
  let user: IUser = {
    name: "name"
  }

  beforeEach(() => {
    userService = {getUserInfo: jasmine.createSpy().and.returnValue(of(user))}
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
