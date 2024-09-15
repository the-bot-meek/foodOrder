import {ActivatedRouteSnapshot, CanActivateFn, GuardResult, MaybeAsync, RouterStateSnapshot} from "@angular/router";
import {inject} from "@angular/core";
import {mergeMap, Observable, of} from "rxjs";
import {AuthService} from "../services/auth/auth.service";

export const authGuardCanActivateFn: CanActivateFn = (route: ActivatedRouteSnapshot, state: RouterStateSnapshot) => {
  const authService: AuthService = inject(AuthService)
  console.log(document.cookie)
  return authService.checkAuth().pipe(
    mergeMap(() => {return of(true)})
  )
}
