import {Component, Input, OnInit} from '@angular/core';
import {mergeMap, Observable} from "rxjs";
import {AsyncPipe, DatePipe, NgIf} from "@angular/common";
import {MatButton} from "@angular/material/button";
import {map} from "rxjs/operators";
import {AuthService} from "../../../shared/services/auth/auth.service";
import {OrderListComponent} from "../../order/order-list/order-list.component";
import {MealService} from "../../../shared/api/meal.service";
import {IMeal} from "../../../../models/meal";
import {IOrder} from "../../../../models/order";

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
