import {Inject, Injectable} from '@angular/core';
import {DOCUMENT} from "@angular/common";
import {environment} from "../../../../environments/environment";

@Injectable({
  providedIn: 'root'
})
export class AuthenticatorService {
  authPath: string = environment.authPath;
  serverUrl: string = environment.serverUrl;
  constructor(@Inject(DOCUMENT) private document: Document) { }

  auth() {
    sessionStorage.setItem('authPostRedirectUrl', window.document.documentURI)
    this.document.location = `${this.serverUrl}/${this.authPath}`
  }
}
