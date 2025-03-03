import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MenuItemListComponent } from './menu-item-list.component';

describe('MenuItemListComponent', () => {
  let component: MenuItemListComponent;
  let fixture: ComponentFixture<MenuItemListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MenuItemListComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(MenuItemListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
