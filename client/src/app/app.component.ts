import {Component} from '@angular/core';
import { RouterOutlet } from '@angular/router';
import {ShellComponent} from "./shared/shell/shell.component";

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, ShellComponent],
  templateUrl: './app.component.html',
  standalone: true,
  styleUrl: './app.component.scss'
})
export class AppComponent {
  title = 'foodOrder';
  constructor() {

  }
}
