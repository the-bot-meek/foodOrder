import { TestBed } from '@angular/core/testing';

import { OrderService } from './order.service';
import {HttpClient} from "@angular/common/http";

describe('OrderService', () => {
  let service: OrderService;
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
    service = TestBed.inject(OrderService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
