import { Component } from '@angular/core';
import {
  MatDialogActions,
  MatDialogClose,
  MatDialogContent,
  MatDialogRef,
  MatDialogTitle
} from "@angular/material/dialog";
import {MatButton, MatButtonModule} from "@angular/material/button";
import {FormControl, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {IVenue} from "../../../../../models/venue";
import {VenueService} from "../../../shared/api/venue.service";
import {MealService} from "../../../shared/api/meal.service";
import {ICreateMealRequest} from "../../../../../models/ICreateMealRequest";
import {MatFormFieldModule} from "@angular/material/form-field";
import {MatInputModule} from "@angular/material/input";
import {MatDatepickerModule} from "@angular/material/datepicker";
import {MatNativeDateModule, MatOption} from "@angular/material/core";
import {MatSelect} from "@angular/material/select";
import {MatIcon} from "@angular/material/icon";

@Component({
  selector: 'app-add-meal-dialog',
  standalone: true,
  imports: [
    MatDialogTitle,
    MatDialogContent,
    MatDialogActions,
    MatButton,
    MatDialogClose,
    MatFormFieldModule,
    MatInputModule,
    MatDatepickerModule,
    MatButtonModule,
    ReactiveFormsModule,
    MatNativeDateModule,
    MatOption,
    MatSelect,
    MatIcon
  ],
  templateUrl: './add-meal-dialog.component.html',
  styleUrl: './add-meal-dialog.component.scss'
})
export class AddMealDialogComponent {
  venues: IVenue[] = []
  constructor(
    private venueService: VenueService,
    private mealService: MealService,
    public dialogRef: MatDialogRef<AddMealDialogComponent>
  ) {
  }

  locations: string[] = ['Kirkwall', 'London']
  addMealFormGroup = new FormGroup({
    name: new FormControl<string>("", [Validators.required]),
    dateOfMeal: new FormControl<Date>(new Date(), [Validators.required]),
    location: new FormControl<string>('', [Validators.required]),
    venueName: new FormControl<string>('', [Validators.required])
  })

  getVenuesForLocation(locationName: string) {
    this.venueService.listVenuesForLocation(locationName).subscribe(venueLocations =>
      this.venues = venueLocations
    )
  }

  addMeal(): void {
    const createMealRequest: ICreateMealRequest = {
      dateOfMeal: this.addMealFormGroup.value.dateOfMeal?.getTime() as number,
      location: this.addMealFormGroup.value.location as string,
      mealConfig: {
        draft: false,
        privateMealConfig: null
      },
      name: this.addMealFormGroup.value.name as string,
      venueName: this.addMealFormGroup.value.venueName as string
    }
    this.mealService.addMeal(createMealRequest).subscribe()
    this.addMealFormGroup.reset()
    this.dialogRef.close()
  }
}
