import { TestBed } from '@angular/core/testing';
import { HttpClient } from "@angular/common/http";
import { MealService } from './meal.service';

describe('MealService', () => {
  let service: MealService;
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
    service = TestBed.inject(MealService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
