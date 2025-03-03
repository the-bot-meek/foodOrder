import { TestBed } from '@angular/core/testing';

import { MenuService } from './menu.service';
import { HttpClient } from "@angular/common/http";

describe('MenuService', () => {
  let service: MenuService;
  let httpClient;

  beforeEach(() => {
    httpClient = jasmine.createSpyObj(['get'])
    TestBed.configureTestingModule({
      providers: [
        {
          provide: HttpClient,
          useValue: httpClient
        }
      ]
    });
    TestBed.configureTestingModule({});
    service = TestBed.inject(MenuService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
