import {Component, OnInit} from '@angular/core';
import { RouterOutlet } from '@angular/router';
import {AuthService} from "./shared/services/auth/auth.service";
import {ShellComponent} from "./shared/shell/shell.component";

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, ShellComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss'
})
export class AppComponent implements OnInit{
  title = 'foodOrder';
  constructor(private authService: AuthService) {

  }

  ngOnInit(): void {
    this.authService.checkAuth().subscribe()
  }
}
