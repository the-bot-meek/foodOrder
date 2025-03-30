import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import {Observable, BehaviorSubject, tap} from 'rxjs';
import { environment } from '../../../environments/environment';

import {IMeal} from '@the-bot-meek/food-orders-models/models/meal'
import {ICreateMealRequest} from '@the-bot-meek/food-orders-models/models/ICreateMealRequest';
import {IOrder} from "@the-bot-meek/food-orders-models/models/order";

@Injectable({
  providedIn: 'root'
})
export class MealService {
  private _serverUrl: string = environment.serverUrl;
  private _meals: BehaviorSubject<IMeal[]> = new BehaviorSubject<IMeal[]>([]);
  constructor(private httpClient: HttpClient) {

  }

  public getMeal(sortKey: string): Observable<IMeal> {
    return this.httpClient.get<IMeal>(this._serverUrl + `/meal/${encodeURIComponent(sortKey)}`, {withCredentials: true});
  }

  public getMealByMealDateAndId(mealDate: number, id: string): Observable<IMeal> {
    return this.httpClient.get<IMeal>(this._serverUrl + `/meal/${mealDate}/${id}`, {withCredentials: true});
  }

  public getDraftMeal(sortKey: string): Observable<IMeal> {
    return this.httpClient.get<IMeal>( `${this._serverUrl}/meal/draft/${sortKey}`, {withCredentials: true})
  }

  public listDraftMeal(): Observable<IMeal[]> {
    return this.httpClient.get<IMeal[]>(`${this._serverUrl}/meal/draft`, {withCredentials: true})
  }

  public listMeal(): BehaviorSubject<IMeal[]> {
    this.httpClient.get<IMeal[]>(`${this._serverUrl}/meal`, {withCredentials: true})
      .pipe(tap(it => this._meals.next(it))).subscribe()
    return this._meals;
  }

  public addMeal(createMealRequest: ICreateMealRequest): Observable<IMeal> {
    return this.httpClient.post<IMeal>(`${this._serverUrl}/meal`, createMealRequest, {withCredentials: true})
  }

  public listAllOrdersForMeal(mealId: string): Observable<IOrder[]> {
    return this.httpClient.get<IOrder[]>(`${this._serverUrl}/meal/${mealId}/orders`, {withCredentials: true})
  }
}
