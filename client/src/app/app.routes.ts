import {Routes} from '@angular/router';
import {LandingPageComponent} from "./components/landing-page/landing-page.component";
import {MealDetailsComponent} from "./components/meal/meal-details/meal-details.component";
import {authGuardCanActivateFn} from "./shared/authGuards/authGuard";

export const routes: Routes = [
  {
    path: "",
    component: LandingPageComponent,
    canActivate: [authGuardCanActivateFn]
  },
  {
    path: "meal/:sk",
    component: MealDetailsComponent,
    canActivate: [authGuardCanActivateFn]
  }
];
