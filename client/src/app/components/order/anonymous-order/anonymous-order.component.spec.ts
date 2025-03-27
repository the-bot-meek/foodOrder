import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AnonymousOrderComponent } from './anonymous-order.component';

describe('AnonymousOrderComponent', () => {
  let component: AnonymousOrderComponent;
  let fixture: ComponentFixture<AnonymousOrderComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AnonymousOrderComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AnonymousOrderComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
