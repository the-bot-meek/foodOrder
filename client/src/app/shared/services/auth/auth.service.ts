import {Inject, Injectable} from '@angular/core';
import {UserService} from "../../api/user.service";
import {catchError, Observable} from "rxjs";
import {IUser} from "../../../../../models/IUser";
import {AuthenticatorService} from "./authenticator.service";

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  constructor(private userService: UserService, private authenticatorService: AuthenticatorService) { }

  checkAuth(): Observable<IUser> {
    return this.userService.getUserInfo().pipe(catchError((err) => {
      this.authenticatorService.auth()
      throw Error(err)
    }))
  }
}
