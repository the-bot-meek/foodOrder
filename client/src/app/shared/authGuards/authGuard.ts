import {ActivatedRouteSnapshot, CanActivateFn, RouterStateSnapshot} from "@angular/router";
import {inject} from "@angular/core";
import {AuthService} from "../services/auth/auth.service";
import {map} from "rxjs/operators";
import {IUser} from "../../../models/IUser";

export const isAdminCanActivateFn: CanActivateFn = (route: ActivatedRouteSnapshot, state: RouterStateSnapshot) => {
  const authService: AuthService = inject(AuthService)
  return authService.checkAuth().pipe(
    map((user: IUser) => {return user.foodorder_roles.includes("FoodOrderAdminUser")})
  )
}
