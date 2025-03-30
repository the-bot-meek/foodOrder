import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {OrderService} from "../../../shared/api/order.service";
import {BehaviorSubject, flatMap, mergeMap, Observable, tap} from "rxjs";
import {IOrder} from "@the-bot-meek/food-orders-models/models/order";
import {IMenu} from "@the-bot-meek/food-orders-models/models/menu";
import {map} from "rxjs/operators";
import {MenuService} from "../../../shared/api/menu.service";
import {AsyncPipe, CurrencyPipe, JsonPipe, NgForOf, NgIf, TitleCasePipe} from "@angular/common";
import {IMenuItems} from "@the-bot-meek/food-orders-models/models/menuItems";
import {MatDialog, MatDialogRef} from "@angular/material/dialog";
import {
  ConfirmAnonomusOrderDetailsModalComponent
} from "../confirm-anonomus-order-deatils/confirm-anonomus-order-details-modal.component";
import {MatCheckbox} from "@angular/material/checkbox";
import {MatButton} from "@angular/material/button";
import {MatTooltip} from "@angular/material/tooltip";

@Component({
  selector: 'app-anonymous-order',
  imports: [
    AsyncPipe,
    NgForOf,
    NgIf,
    CurrencyPipe,
    NgIf,
    NgForOf,
    JsonPipe,
    TitleCasePipe,
    MatCheckbox,
    MatButton,
    MatTooltip,
  ],
  templateUrl: './anonymous-order.component.html',
  standalone: true,
  styleUrl: './anonymous-order.component.scss'
})
export class AnonymousOrderComponent implements OnInit{
  private mealId: string
  private userId: string
  order: Observable<IOrder> = new BehaviorSubject<IOrder>(null);
  menu: Observable<IMenu>
  selectedItems: IMenuItems[] = [];

  constructor(
    private route: ActivatedRoute,
    private orderService: OrderService,
    private menuService: MenuService,
    private dialog: MatDialog
  ) {}

  get menuItemNames() {
    return this.selectedItems.map(menuItem => menuItem.name)
  }

  getMenuCategories(menuItems: IMenuItems[]): string[] {
    return menuItems.map(item => item.menuItemCategory).filter((menuItemCategory, i, pastMenuItemCategories) => pastMenuItemCategories.indexOf(menuItemCategory) === i)
  }

  getMenuItemsForCategory(category: string, menuItems: IMenuItems[]): IMenuItems[] {
    return menuItems.filter(menuItem => menuItem.menuItemCategory === category);
  }

  getTotalPrice(menuItems: IMenuItems[]): number {
    return  menuItems.reduce((partialSum, menuItem) => partialSum + menuItem.price, 0)
  }

  onItemSelect(item: IMenuItems, isSelected: boolean): void {
    if (isSelected) {
      this.selectedItems.push(item);
    } else {
      this.selectedItems = this.selectedItems.filter(i => i.name !== item.name);
    }
  }

  confirmOrder(order: IOrder): void {
    this.dialog.open(ConfirmAnonomusOrderDetailsModalComponent, {
      data: {
        order: order,
        selectedItems: this.selectedItems
      },
      width: '30vw',
    })

    this.dialog.afterAllClosed.subscribe(() => {
      this.fetchOrderAndMenuItem()
    })
  }

  fetchOrderAndMenuItem() {
    this.order = this.orderService.getAnonymousOrder(this.userId, this.mealId).pipe(tap(order => {
      this.selectedItems = order.menuItems ?? [];
      this.menu = this.menuService.fetchMenu(order.meal.location, order.meal.menuName)
    }));
  }

  ngOnInit(): void {
    this.userId = this.route.snapshot.params['userId']
    this.mealId = this.route.snapshot.params['mealId']
    this.fetchOrderAndMenuItem()
  }
}
