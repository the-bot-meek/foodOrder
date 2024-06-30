import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddVenueDialogComponent } from './add-venue-dialog.component';

describe('AddVenueDialogComponent', () => {
  let component: AddVenueDialogComponent;
  let fixture: ComponentFixture<AddVenueDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AddVenueDialogComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AddVenueDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
