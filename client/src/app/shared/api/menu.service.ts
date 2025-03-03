import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ICreateMenuRequest, IMenu } from '@the-bot-meek/food-orders-models/models/menu';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import {IMeal} from "@the-bot-meek/food-orders-models/models/meal";

@Injectable({
  providedIn: 'root'
})
export class MenuService {
  serverUrl: string = environment.serverUrl;
  constructor(private httpClient: HttpClient) { }

  public addMenu(createMenuRequest: ICreateMenuRequest): Observable<IMenu> {
   return  this.httpClient.post<IMenu>(`${this.serverUrl}/menu`, createMenuRequest, {withCredentials: true})
  }

  public listMenusForLocation(location: string): Observable<IMenu[]> {
    return this.httpClient.get<IMenu[]>(`${this.serverUrl}/menu/${location}`, {withCredentials: true})
  }

  public fetchMenu(location: string, name: string): Observable<IMenu> {
    return this.httpClient.get<IMenu>(`${this.serverUrl}/menu/${location}/${name}`, {withCredentials: true})
  }

  public getMealByMenuNameAndMealId(menuName: string, mealId: string): Observable<IMeal> {
    return this.httpClient.get<IMeal>(`${this.serverUrl}/menu/${menuName}/meal/${mealId}`)
  }
}
