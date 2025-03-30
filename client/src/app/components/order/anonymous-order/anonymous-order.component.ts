import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {OrderService} from "../../../shared/api/order.service";
import {mergeMap, Observable} from "rxjs";
import {IOrder} from "@the-bot-meek/food-orders-models/models/order";
import {IMenu} from "@the-bot-meek/food-orders-models/models/menu";
import {map} from "rxjs/operators";
import {MenuService} from "../../../shared/api/menu.service";
import {AsyncPipe, CurrencyPipe, JsonPipe, NgForOf, NgIf} from "@angular/common";
import {IMenuItems} from "@the-bot-meek/food-orders-models/models/menuItems";

@Component({
  selector: 'app-anonymous-order',
  imports: [
    AsyncPipe,
    JsonPipe,
    NgForOf,
    NgIf,
    CurrencyPipe
  ],
  templateUrl: './anonymous-order.component.html',
  standalone: true,
  styleUrl: './anonymous-order.component.scss'
})
export class AnonymousOrderComponent implements OnInit{
  private mealId: string
  private userId: string
  order: Observable<IOrder>;
  menu: Observable<IMenu>
  selectedItems: IMenuItems[] = [];

  constructor(
    private route: ActivatedRoute,
    private orderService: OrderService,
    private menuService: MenuService
  ) {}

  onItemSelect(item: IMenuItems, isSelected: HTMLElement | any): void {
    if (isSelected.checked) {
      this.selectedItems.push(item);
    } else {
      this.selectedItems = this.selectedItems.filter(i => i !== item);
    }
  }

  ngOnInit(): void {
    this.userId = this.route.snapshot.params['userId']
    this.mealId = this.route.snapshot.params['mealId']

    this.order = this.orderService.getAnonymousOrder(this.userId, this.mealId)
    this.menu = this.order.pipe(mergeMap(
      order => this.menuService.fetchMenu(order.meal.location, order.meal.menuName)
    ))
  }
}
