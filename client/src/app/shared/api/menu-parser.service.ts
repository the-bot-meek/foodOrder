import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {environment} from "../../../environments/environment";
import {Observable} from "rxjs";
import {IConvertMenuItemTask} from '@the-bot-meek/food-orders-models/models/IConvertMenuItemTask'

@Injectable({
  providedIn: 'root'
})
export class MenuParserService {

  constructor(private http: HttpClient) { }

  public startMenuConvertTask(file: File): Observable<IConvertMenuItemTask> {
    const formData = new FormData()
    formData.append('file', file)
    return this.http.post<IConvertMenuItemTask>(`${environment.serverUrl}/menuParseTask`, formData, {withCredentials: true})
  }

  public getMenuConvertTask(taskId: string) {
    return this.http.get<IConvertMenuItemTask>(`${environment.serverUrl}/menuParseTask/${taskId}`, {withCredentials: true})
  }
}
