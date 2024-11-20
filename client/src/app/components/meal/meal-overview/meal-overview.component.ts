import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {MealService} from "../../../shared/api/meal.service";
import {Observable} from "rxjs";
import {IMeal} from "../../../../../models/meal";
import {AsyncPipe, DatePipe, JsonPipe, NgIf} from "@angular/common";
import {MatButton} from "@angular/material/button";
import {MealDetailsComponent} from "../meal-details/meal-details.component";

@Component({
    selector: 'app-overview',
    imports: [
        JsonPipe,
        AsyncPipe,
        NgIf,
        DatePipe,
        MatButton,
        MealDetailsComponent
    ],
    templateUrl: './meal-overview.component.html',
    styleUrl: './meal-overview.component.scss'
})
export class MealOverviewComponent implements OnInit {
  meal: Observable<IMeal>;
  constructor(private route:ActivatedRoute, private mealService: MealService) {
  }

  ngOnInit(): void {
    this.meal = this.mealService.getMeal(this.route.snapshot.params['sk'])
  }
}
