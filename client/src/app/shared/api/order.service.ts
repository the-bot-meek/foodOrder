import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '../../../environments/environment';
import { Observable } from 'rxjs';
import {ICreateOrderRequest, IOrder} from "../../../models/order";

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
    return this.httpClient.post(`${this.serverUrl}/anonymousOrder/addBlankOrdersForMeal/${sortKey}`, null,{withCredentials: true})
  }

  public updateAnonymousOrder(order: IOrder): Observable<IOrder> {
    return this.httpClient.put<IOrder>(`${this.serverUrl}/anonymousOrder/${order.orderParticipant.userId}`, order, {withCredentials: true})
  }
}
