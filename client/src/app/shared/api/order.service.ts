import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ICreateOrderRequest, IOrder } from '@the-bot-meek/food-orders-models/models/order';
import { environment } from '../../../environments/environment';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class OrderService {
  serverUrl: string = environment.serverUrl;
  constructor(private httpClient: HttpClient) { }

  public addOrder(createOrderRequest: ICreateOrderRequest): Observable<IOrder> {
    return this.httpClient.post<IOrder>(`${this.serverUrl}/order`, createOrderRequest, {withCredentials: true})
  }

  public getAnonymousOrder(userId: string, mealId: string): Observable<IOrder> {
    return this.httpClient.get<IOrder>(`${this.serverUrl}/anonymousOrder/${userId}/${mealId}`, {withCredentials: true})
  }

  public addAnonymousOrders(sortKey: string): Observable<Object> {
    return this.httpClient.post(`${this.serverUrl}/anonymousOrder/addBlankOrdersForMeal/${sortKey}`, {withCredentials: true})
  }
}
