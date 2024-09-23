import {Component, OnInit} from '@angular/core';

@Component({
  selector: 'app-login-success',
  standalone: true,
  imports: [],
  templateUrl: './login-success.component.html',
  styleUrl: './login-success.component.scss'
})
export class LoginSuccessComponent implements OnInit {
  constructor() {
  }

  ngOnInit(): void {
    const authPostRedirectUrl: string = sessionStorage.getItem('authPostRedirectUrl')
    if (authPostRedirectUrl) {
      sessionStorage.removeItem(authPostRedirectUrl)
      window.document.location = authPostRedirectUrl
    }
  }
}
