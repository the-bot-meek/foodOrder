import {Routes} from '@angular/router';
import {LandingPageComponent} from "./components/landing-page/landing-page.component";
import {MealOverviewComponent} from "./components/meal/meal-overview/meal-overview.component";
import {isAdmin} from "./shared/authGuards/authGuard";
import {
  AccreditedOrderComponent
} from "./components/order/accredited-order-component/accredited-order.component";
import {LoginSuccessComponent} from "./components/login-success/login-success.component";

export const routes: Routes = [
  {
    path: "",
    component: LandingPageComponent,
    canActivate: [isAdmin]
  },
  {
    path: "meal/:sk",
    component: MealOverviewComponent,
    canActivate: [isAdmin]
  },
  {
    path: "order/:menuName/:mealId",
    component: AccreditedOrderComponent,
    canActivate: [isAdmin]
  },
  {
    path: 'login-success',
    component: LoginSuccessComponent
  }
];
