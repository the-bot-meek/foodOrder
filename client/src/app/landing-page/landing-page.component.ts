import {Component, OnInit} from '@angular/core';
import {MealService} from "../shared/api/meal.service";
import {Observable} from "rxjs";
import {IMeal} from "../../../models/meal";
import {AsyncPipe, JsonPipe} from "@angular/common";

@Component({
  selector: 'app-landing-page',
  standalone: true,
  imports: [
    AsyncPipe,
    JsonPipe
  ],
  templateUrl: './landing-page.component.html',
  styleUrl: './landing-page.component.scss'
})
export class LandingPageComponent implements OnInit {
  meals: Observable<IMeal[]>
  constructor(private mealService: MealService) {
  }

  ngOnInit(): void {
    this.meals = this.mealService.listMeal()
  }
}
