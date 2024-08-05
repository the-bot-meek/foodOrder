import { Injectable } from '@angular/core';
import {v4 as uuid} from 'uuid';

@Injectable({
  providedIn: 'root'
})
export class UUIDService {

  constructor() { }

  randomUUID(): string {
    return uuid()
  }
}
