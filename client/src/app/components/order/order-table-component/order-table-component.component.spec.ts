import { ComponentFixture, TestBed } from '@angular/core/testing';

import { OrderTableComponentComponent } from './order-table-component.component';

describe('OrderTableComponentComponent', () => {
  let component: OrderTableComponentComponent;
  let fixture: ComponentFixture<OrderTableComponentComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [OrderTableComponentComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(OrderTableComponentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
