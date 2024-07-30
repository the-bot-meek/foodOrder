import { TestBed } from '@angular/core/testing';

import { UUIDService } from './uuid.service';

describe('UUIDService', () => {
  let service: UUIDService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(UUIDService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
