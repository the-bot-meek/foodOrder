import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {Observable} from "rxjs";
import {IMeal} from "../../../../../models/meal";
import {VenueService} from "../../../shared/api/venue.service";
import {AsyncPipe, JsonPipe} from "@angular/common";
import {MealOverviewComponent} from "../../meal/meal-overview/meal-overview.component";
import {MealDetailsComponent} from "../../meal/meal-details/meal-details.component";

@Component({
    selector: 'app-accredited-order',
    imports: [
        AsyncPipe,
        JsonPipe,
        MealOverviewComponent,
        MealDetailsComponent
    ],
    templateUrl: './accredited-order.component.html',
    styleUrl: './accredited-order.component.scss'
})
export class AccreditedOrderComponent implements OnInit {
  private mealId: string
  private venueName: string
  meal: Observable<IMeal>
  constructor(private route:ActivatedRoute, private venueService: VenueService) {
  }

  ngOnInit(): void {
      this.mealId = this.route.snapshot.params['mealId']
      this.venueName = this.route.snapshot.params['venueName']
      this.meal = this.venueService.getMealByVenueNameAndMealId(this.venueName, this.mealId)
  }
}
