import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {OrderService} from "../../../shared/api/order.service";
import {Observable} from "rxjs";
import {IOrder} from "@the-bot-meek/food-orders-models/models/order";

@Component({
  selector: 'app-anonymous-order',
  imports: [],
  templateUrl: './anonymous-order.component.html',
  standalone: true,
  styleUrl: './anonymous-order.component.scss'
})
export class AnonymousOrderComponent implements OnInit{
  private mealId: string
  private userId: string
  private order: Observable<IOrder>;

  constructor(
    private route: ActivatedRoute,
    private orderService: OrderService
  ) {}

  ngOnInit(): void {
    this.userId = this.route.snapshot.params['userId']
    this.mealId = this.route.snapshot.params['mealId']

    this.order = this.orderService.getAnonymousOrder(this.userId, this.mealId)
  }
}
