import {Routes} from '@angular/router';
import {LandingPageComponent} from "./components/landing-page/landing-page.component";
import {MealOverviewComponent} from "./components/meal/meal-overview/meal-overview.component";
import {isAdminCanActivateFn} from "./shared/authGuards/authGuard";
import {LoginSuccessComponent} from "./components/login-success/login-success.component";
import {AnonymousOrderComponent} from "./components/order/anonymous-order/anonymous-order.component";

export const routes: Routes = [
  {
    path: "",
    component: LandingPageComponent,
    canActivate: [isAdminCanActivateFn]
  },
  {
    path: "meal/:sk",
    component: MealOverviewComponent,
    canActivate: [isAdminCanActivateFn]
  },
  {
    path: "order/:mealId/:userId",
    component: AnonymousOrderComponent,
  },
  {
    path: 'login-success',
    component: LoginSuccessComponent
  }
];
