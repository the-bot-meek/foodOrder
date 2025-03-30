import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ConfirmAnonomusOrderDetailsModalComponent } from './confirm-anonomus-order-details-modal.component';

describe('ConfirmAnonomusOrderDeatilsComponent', () => {
  let component: ConfirmAnonomusOrderDetailsModalComponent;
  let fixture: ComponentFixture<ConfirmAnonomusOrderDetailsModalComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ConfirmAnonomusOrderDetailsModalComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ConfirmAnonomusOrderDetailsModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
