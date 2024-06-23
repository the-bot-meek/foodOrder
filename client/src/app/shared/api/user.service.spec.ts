import { TestBed } from '@angular/core/testing';

import { UserService } from './user.service';
import {HttpClient} from "@angular/common/http";
import {of} from "rxjs";
import {IUser} from "../../../../models/IUser";

describe('UserService', () => {
  let service: UserService;
  let httpClient;
  let user: IUser = {
    name: 'name'
  }

  beforeEach(() => {
    httpClient = {get: jasmine.createSpy().and.returnValue(of(user))}
    TestBed.configureTestingModule({
      providers: [
        {
          provide: HttpClient,
          useValue: httpClient
        }
      ]
    });
    TestBed.configureTestingModule({});
    service = TestBed.inject(UserService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('isAuthenticated should be false by default', () => {
    expect(service.isAuthenticated()).toBeFalse()
  })

  it('isAuthenticated should be true when user endpoint returns 200', () => {
    service.getUserInfo().subscribe(it => {
      expect(service.isAuthenticated()).toBeTrue()
    })
  })
});
