import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ICreateVenueRequest, IVenue } from '../../../models/venue';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class VenueService {
  serverUrl: string = environment.serverUrl;
  constructor(private httpClient: HttpClient) { }

  public addVenue(createVenueRequest: ICreateVenueRequest): Observable<IVenue> {
   return  this.httpClient.put<IVenue>(`${this.serverUrl}/venue`, createVenueRequest, {withCredentials: true})
  }

  public listVenuesForLocation(location: string): Observable<IVenue[]> {
    return this.httpClient.get<IVenue[]>(`${this.serverUrl}/venue/${location}`, {withCredentials: true})
  }

  public fetchVenue(location: string, name: string): Observable<IVenue> {
    return this.httpClient.get<IVenue>(`${this.serverUrl}/venue/${location}/${name}`, {withCredentials: true})
  }
}
