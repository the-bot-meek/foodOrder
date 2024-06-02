import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, map } from 'rxjs';
import { environment } from '../../environments/environment';

import { ICreateMealRequest } from '../../../models/ICreateMealRequest';
import { IMeal } from '../../models/meal';

@Injectable({
  providedIn: 'root'
})
export class MealService {
  serverUrl: string = environment.serverUrl;
  constructor(private httpClient: HttpClient) {

  }

  public getMeal(sortKey: string): Observable<IMeal> {
    return this.httpClient.get<IMeal>(this.serverUrl + `meal/${encodeURIComponent(sortKey)}`, {withCredentials: true});
  }

  public getDraftMeal(sortKey: string): Observable<IMeal> {
    return this.httpClient.get<IMeal>( `${this.serverUrl}/meal/draft/${sortKey}`, {withCredentials: true})
  }

  public listDraftMeal(): Observable<IMeal[]> {
    return this.httpClient.get<IMeal[]>(`${this.serverUrl}/meal/draft`, {withCredentials: true})
  }

  public listMeal(): Observable<IMeal[]> {
    return this.httpClient.get<IMeal[]>(`${this.serverUrl}/meal`, {withCredentials: true})
  }

  public addMeal(createMealRequest: ICreateMealRequest): Observable<IMeal> {
    return this.httpClient.put<IMeal>(`${this.serverUrl}/meal`, createMealRequest, {withCredentials: true})
  }
}
