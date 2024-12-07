import {Component, OnInit} from '@angular/core';
import {MealService} from "../../shared/api/meal.service";
import {Observable} from "rxjs";
import {IMeal} from "../../../../models/meal";
import {DatePipe} from "@angular/common";
import {MatTableModule} from "@angular/material/table";
import {Router} from "@angular/router";
import {MatIcon} from "@angular/material/icon";

@Component({
    selector: 'app-landing-page',
    imports: [
        MatTableModule,
        DatePipe,
        MatIcon
    ],
    templateUrl: './landing-page.component.html',
    styleUrl: './landing-page.component.scss'
})
export class LandingPageComponent implements OnInit {
  meals: Observable<IMeal[]>
  displayedColumns: string[] = ['name', 'mealDate', 'location', 'venueName', 'public']
  constructor(
    private mealService: MealService,
    private router: Router
  ) {
  }

  navigateToMeal(mealSortKey: any) {
    this.router.navigate(['meal', mealSortKey])
  }

  ngOnInit(): void {
    this.meals = this.mealService.listMeal()
  }
}
