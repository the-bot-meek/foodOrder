import { Injectable } from '@angular/core';
import { HttpClient } from "@angular/common/http";
import {Observable, tap} from "rxjs";
import {IUser} from "../../../../models/IUser";
import {environment} from "../../../environments/environment";

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private _serverUrl: string = environment.serverUrl;
  private _isAuthenticated: boolean = false;
  constructor(private httpClient: HttpClient) { }

  public getUserInfo(): Observable<IUser> {
    return this.httpClient.get<IUser>(`${this._serverUrl}/user`, {withCredentials: true})
      .pipe(
        tap(() =>{this._isAuthenticated = true;})
      );
  }

  public isAuthenticated() {
    return this._isAuthenticated;
  }
}
