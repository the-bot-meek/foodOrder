import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {OrderService} from "../../../shared/api/order.service";
import {BehaviorSubject, Observable, tap} from "rxjs";
import {MenuService} from "../../../shared/api/menu.service";
import {AsyncPipe, CurrencyPipe, NgForOf, NgIf, TitleCasePipe} from "@angular/common";
import {MatDialog} from "@angular/material/dialog";
import {
  ConfirmAnonomusOrderDetailsModalComponent
} from "../confirm-anonomus-order-deatils/confirm-anonomus-order-details-modal.component";
import {MatCheckbox} from "@angular/material/checkbox";
import {MatButton} from "@angular/material/button";
import {MatTooltip} from "@angular/material/tooltip";
import {MatChipsModule} from "@angular/material/chips";
import {MatBadgeModule} from "@angular/material/badge";
import {ScrollingModule} from '@angular/cdk/scrolling';
import {IMenuItems} from "../../../../models/menuItems";
import {IOrder} from "../../../../models/order";
import {IMenu} from "../../../../models/menu";

@Component({
  selector: 'app-anonymous-order',
  imports: [
    AsyncPipe,
    NgForOf,
    CurrencyPipe,
    NgIf,
    NgForOf,
    TitleCasePipe,
    MatCheckbox,
    MatButton,
    MatTooltip,
    MatChipsModule,
    MatBadgeModule,
    ScrollingModule
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
  menuItemCategoryOrder: ("STARTER" | "MAIN" | "DESSERT" | "DRINK" | "SIDE")[] = ["STARTER", "MAIN", "SIDE", "DESSERT", "DRINK"]
  isNewOrder: boolean = false
  selectedMenuItemCategory: string
  backgroundImage: string = "https://images.unsplash.com/photo-1550367363-ea12860cc124?q=80&w=3024&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"

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
    const availableMenuItems = menuItems.map(item => item.menuItemCategory)
      .filter((menuItemCategory, i, pastMenuItemCategories) => pastMenuItemCategories.indexOf(menuItemCategory) === i)
      .sort()

    return this.menuItemCategoryOrder.filter(menuItemCategory => availableMenuItems.includes(menuItemCategory))
  }

  getOrderedMenuItemsNamesForCategory(category: string): string[]  {
    return this.getMenuItemsForCategory(category, this.selectedItems).map(menuItem => menuItem.name)
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
      this.isNewOrder = order.orderParticipant.name == "AnonymousUser"
    }));
  }

  ngOnInit(): void {
    this.userId = this.route.snapshot.params['userId']
    this.mealId = this.route.snapshot.params['mealId']
    this.fetchOrderAndMenuItem()
  }


}
