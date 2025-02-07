import {Injectable} from '@angular/core';
import {UserService} from "../../api/user.service";
import {BehaviorSubject, catchError, Observable, tap} from "rxjs";
import {IUser} from "@the-bot-meek/food-orders-models/models/IUser";
import {AuthenticatorService} from "./authenticator.service";

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private _userReplaySubject: BehaviorSubject<IUser> = new BehaviorSubject<IUser>(null);
  constructor(private userService: UserService, private authenticatorService: AuthenticatorService) { }

  checkAuth(): Observable<IUser> {
    if (this._userReplaySubject.value && this._userReplaySubject.value.exp * 1000 > Date.now()) {
      return this._userReplaySubject
    }
    return this.userService.getUserInfo().pipe(
      catchError((err) => {
        this.authenticatorService.auth()
        throw Error(err)
        }
      ), tap(user => this._userReplaySubject.next(user))
    )
  }

  public get userDetails() {
    return this._userReplaySubject.value
  }

  public set userDetails(user: IUser) {
    this._userReplaySubject.next(user)
  }
}
