import {Component, Input, OnInit} from '@angular/core';
import {flatMap, mergeMap, Observable} from "rxjs";
import {IMeal} from "@the-bot-meek/food-orders-models/models/meal";
import {AsyncPipe, DatePipe, NgIf} from "@angular/common";
import {MatButton} from "@angular/material/button";
import {map} from "rxjs/operators";
import {AuthService} from "../../../shared/services/auth/auth.service";
import {OrderListComponent} from "../../order/order-list/order-list.component";
import {IOrder} from "@the-bot-meek/food-orders-models/models/order";
import {MealService} from "../../../shared/api/meal.service";
import {environment} from "../../../../environments/environment";

@Component({
  selector: 'app-meal-details',
  imports: [
    AsyncPipe,
    DatePipe,
    MatButton,
    NgIf,
    OrderListComponent
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

  constructor(
    private authService: AuthService,
    private mealService: MealService
  ) {
  }

  downloadMealReport(meal: IMeal) {
    window.location.href = `http://localhost:8080/meal/export/${meal.mealDate}/${meal.id}`
  }

  ngOnInit(): void {
    this.isOwner = this.meal.pipe(
      map(meal => meal.uid == this.authService.userDetails.sub)
    )
    this.orders = this.meal.pipe(
      mergeMap( meal => this.mealService.listAllOrdersForMeal(meal.id))
    )
  }
}
