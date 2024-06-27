import { TestBed } from '@angular/core/testing';

import { VenueService } from './venue.service';
import { HttpClient } from "@angular/common/http";

describe('VenueService', () => {
  let service: VenueService;
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
    service = TestBed.inject(VenueService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
