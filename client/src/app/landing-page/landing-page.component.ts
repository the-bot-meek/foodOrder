import {Component, OnInit} from '@angular/core';
import {MealService} from "../shared/api/meal.service";
import {Observable} from "rxjs";
import {IMeal} from "../../../models/meal";
import {AsyncPipe, DatePipe, JsonPipe} from "@angular/common";
import {MatTableModule} from "@angular/material/table";

@Component({
  selector: 'app-landing-page',
  standalone: true,
  imports: [
    AsyncPipe,
    JsonPipe,
    MatTableModule,
    DatePipe
  ],
  templateUrl: './landing-page.component.html',
  styleUrl: './landing-page.component.scss'
})
export class LandingPageComponent implements OnInit {
  meals: Observable<IMeal[]>
  displayedColumns: string[] = ['name', 'mealDate', 'location', 'venueName']
  constructor(private mealService: MealService) {
  }

  ngOnInit(): void {
    this.meals = this.mealService.listMeal()
  }
}
