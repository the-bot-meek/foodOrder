import {Component, Input, OnInit} from '@angular/core';
import {flatMap, mergeMap, Observable} from "rxjs";
import {IMeal} from "../../../../../models/meal";
import {AsyncPipe, DatePipe, JsonPipe, NgIf} from "@angular/common";
import {MatButton} from "@angular/material/button";
import {map} from "rxjs/operators";
import {AuthService} from "../../../shared/services/auth/auth.service";
import {IOrder} from "../../../../../models/order";
import {OrderService} from "../../../shared/api/order.service";
import {MealService} from "../../../shared/api/meal.service";
import {OrderTableComponentComponent} from "../../order/order-table-component/order-table-component.component";

@Component({
  selector: 'app-meal-details',
  imports: [
    AsyncPipe,
    DatePipe,
    MatButton,
    NgIf,
    JsonPipe,
    OrderTableComponentComponent
  ],
  templateUrl: './meal-details.component.html',
  standalone: true,
  styleUrl: './meal-details.component.scss'
})
export class MealDetailsComponent implements OnInit {
  @Input()
  meal: Observable<IMeal>
  orders: Observable<IOrder[]>
  isOwner: Observable<boolean>

  constructor(private authService: AuthService, private mealService: MealService) {
  }

  ngOnInit(): void {
    this.isOwner = this.meal.pipe(
      map(meal =>
        meal.uid == this.authService.userDetails.sub
      )
    )

    this.orders = this.meal.pipe(mergeMap(meal => {
      return this.mealService.listAllOrdersForMeal(meal.id)
    }));
  }
}
