import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AccreditedOrderComponent } from './accredited-order.component';

describe('AccreditedOrderComponentComponent', () => {
  let component: AccreditedOrderComponent;
  let fixture: ComponentFixture<AccreditedOrderComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AccreditedOrderComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AccreditedOrderComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
