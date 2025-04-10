import {Component, Input} from '@angular/core';
import {Observable} from "rxjs";
import {IMeal} from "@the-bot-meek/food-orders-models/models/meal";
import {Router} from "@angular/router";
import { MatTableModule} from "@angular/material/table";
import {DatePipe} from "@angular/common";

@Component({
  selector: 'app-meal-table',
  imports: [
    MatTableModule,
    DatePipe
  ],
  templateUrl: './meal-table.component.html',
  standalone: true,
  styleUrl: './meal-table.component.scss'
})
export class MealTableComponent {
  @Input()
  meals: Observable<IMeal[]>
  displayedColumns: string[] = ['name', 'mealDate', 'location', 'menuName']
  constructor(
    private router: Router
  ) {
  }

  navigateToMeal(mealSortKey: any) {
    this.router.navigate(['meal', mealSortKey])
  }
}
