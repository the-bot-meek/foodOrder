import { Routes } from '@angular/router';
import {LandingPageComponent} from "./components/landing-page/landing-page.component";
import {MealDetailsComponent} from "./components/meal/meal-details/meal-details.component";

export const routes: Routes = [
  {
    path: "",
    component: LandingPageComponent
  },
  {
    path: "meal/:sk",
    component: MealDetailsComponent
  }
];
