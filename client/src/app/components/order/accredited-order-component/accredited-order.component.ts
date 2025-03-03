import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {Observable} from "rxjs";
import {IMeal} from "@the-bot-meek/food-orders-models/models/meal";
import {MenuService} from "../../../shared/api/menu.service";
import {MealDetailsComponent} from "../../meal/meal-details/meal-details.component";

@Component({
    selector: 'app-accredited-order',
    imports: [
        MealDetailsComponent
    ],
    templateUrl: './accredited-order.component.html',
    styleUrl: './accredited-order.component.scss'
})
export class AccreditedOrderComponent implements OnInit {
  private mealId: string
  private menuName: string
  meal: Observable<IMeal>
  constructor(private route:ActivatedRoute, private menuService: MenuService) {
  }

  ngOnInit(): void {
      this.mealId = this.route.snapshot.params['mealId']
      this.menuName = this.route.snapshot.params['menuName']
      this.meal = this.menuService.getMealByMenuNameAndMealId(this.menuName, this.mealId)
  }
}
