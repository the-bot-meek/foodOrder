import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {MealService} from "../../../shared/api/meal.service";
import {Observable} from "rxjs";
import {IMeal} from "../../../../../models/meal";
import {AsyncPipe, JsonPipe} from "@angular/common";

@Component({
  selector: 'app-meal-details',
  standalone: true,
  imports: [
    JsonPipe,
    AsyncPipe
  ],
  templateUrl: './meal-details.component.html',
  styleUrl: './meal-details.component.scss'
})
export class MealDetailsComponent implements OnInit {
  meal: Observable<IMeal>;
  constructor(private route:ActivatedRoute, private mealService: MealService) {
  }

  ngOnInit(): void {
    this.meal = this.mealService.getMeal(this.route.snapshot.params['sk'])
  }
}
