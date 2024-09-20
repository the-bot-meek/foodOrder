import {Routes} from '@angular/router';
import {LandingPageComponent} from "./components/landing-page/landing-page.component";
import {MealOverviewComponent} from "./components/meal/meal-overview/meal-overview.component";
import {authGuardCanActivateFn} from "./shared/authGuards/authGuard";
import {
  AccreditedOrderComponent
} from "./components/order/accredited-order-component/accredited-order.component";

export const routes: Routes = [
  {
    path: "",
    component: LandingPageComponent,
    canActivate: [authGuardCanActivateFn]
  },
  {
    path: "meal/:sk",
    component: MealOverviewComponent,
    canActivate: [authGuardCanActivateFn]
  },
  {
    path: "order/:venueName/:mealId",
    component: AccreditedOrderComponent,
    canActivate: [authGuardCanActivateFn]
  }
];
