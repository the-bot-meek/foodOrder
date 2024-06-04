import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ICreateOrderRequest, IOrder } from '../../../models/order';
import { environment } from '../../environments/environment';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class OrderService {
  serverUrl: string = environment.serverUrl;
  constructor(private httpClient: HttpClient) { }

  public addOrder(createOrderRequest: ICreateOrderRequest): Observable<IOrder> {
    return this.httpClient.put<IOrder>(`${this.serverUrl}/order`, createOrderRequest, {withCredentials: true})
  }
}
