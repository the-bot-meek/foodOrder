import {Component, Input, OnInit} from '@angular/core';
import {Observable} from "rxjs";
import {IMeal} from "../../../../../models/meal";
import {AsyncPipe, DatePipe, NgIf} from "@angular/common";
import {MatButton} from "@angular/material/button";
import {map} from "rxjs/operators";
import {AuthService} from "../../../shared/services/auth/auth.service";

@Component({
  selector: 'app-meal-details',
  imports: [
    AsyncPipe,
    DatePipe,
    MatButton,
    NgIf
  ],
  templateUrl: './meal-details.component.html',
  standalone: true,
  styleUrl: './meal-details.component.scss'
})
export class MealDetailsComponent implements OnInit {
  @Input()
  meal: Observable<IMeal>
  isOwner: Observable<boolean>

  constructor(private authService: AuthService) {
  }

  ngOnInit(): void {
    this.isOwner = this.meal.pipe(
      map(meal =>
        meal.uid == this.authService.userDetails.sub
      )
    )
  }
}
