import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {MealService} from "../../../shared/api/meal.service";
import {Observable} from "rxjs";
import {MealDetailsComponent} from "../meal-details/meal-details.component";
import {IMeal} from "../../../../models/meal";

@Component({
  selector: 'app-overview',
  imports: [
    MealDetailsComponent
  ],
  templateUrl: './meal-overview.component.html',
  standalone: true,
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
