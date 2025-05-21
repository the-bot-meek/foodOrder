import {Component, OnInit} from '@angular/core';
import {MealService} from "../../shared/api/meal.service";
import {Observable} from "rxjs";
import {IMeal} from "@the-bot-meek/food-orders-models/models/meal";
import {AsyncPipe, DatePipe} from "@angular/common";
import {MatTableModule} from "@angular/material/table";
import {Router} from "@angular/router";
import {MealTableComponent} from "../meal/meal-table/meal-table.component";
import {UserService} from "../../shared/api/user.service";
import {IUser} from "@the-bot-meek/food-orders-models/models/IUser";
import {AuthService} from "../../shared/services/auth/auth.service";

@Component({
    selector: 'app-landing-page',
    imports: [
        MatTableModule,
        DatePipe,
        MealTableComponent,
        AsyncPipe
    ],
    templateUrl: './landing-page.component.html',
    styleUrl: './landing-page.component.scss'
})
export class LandingPageComponent {
  userInfo: IUser = this.authService.userDetails;
  meals: Observable<IMeal[]> = this.mealService.listMeal()
  constructor(
    private mealService: MealService,
    private authService: AuthService,
  ) {
  }
}
