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
import {MatSnackBar} from "@angular/material/snack-bar";
import {Router} from "@angular/router";
import {catchError} from "rxjs";
import {MatCheckbox} from "@angular/material/checkbox";
import {UUIDService} from "../../../shared/utils/uuid.service";


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
    MatIcon,
    MatCheckbox
  ],
  templateUrl: './add-meal-dialog.component.html',
  styleUrl: './add-meal-dialog.component.scss'
})
export class AddMealDialogComponent {
  venues: IVenue[] = []
  constructor(
    private venueService: VenueService,
    private mealService: MealService,
    public dialogRef: MatDialogRef<AddMealDialogComponent>,
    private snackBar: MatSnackBar,
    private router: Router,
    private uuid: UUIDService
  ) {
  }

  locations: string[] = ['Kirkwall', 'London']
  addMealFormGroup = new FormGroup({
    name: new FormControl<string>("", [Validators.required]),
    dateOfMeal: new FormControl<Date>(new Date(), [Validators.required]),
    location: new FormControl<string>('', [Validators.required]),
    venueName: new FormControl<string>('', [Validators.required]),
    privateMeal: new FormControl<boolean>(false),
    numberOfRecipients: new FormControl<number>(1)
  })

  getVenuesForLocation(locationName: string) {
    this.venueService.listVenuesForLocation(locationName).subscribe(venueLocations =>
      this.venues = venueLocations
    )
  }

  private openAddMealSnackBar(mealSortKey: string) {
    this.snackBar.open("Meal added", "Open Meal", {
      horizontalPosition: 'center', verticalPosition: 'top', duration: 75000
    }).onAction().subscribe(() => {
      this.router.navigate(['meal', mealSortKey])
    })
  }

  private failedToAddMeal() {
    this.snackBar.open("Failed to add meal", null, {
      horizontalPosition: 'end', verticalPosition: 'top', duration: 7500
    });
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
    if (this.addMealFormGroup.value.privateMeal) {
      const recipientIds: string[] = [];
      for (let i = 0; i < this.addMealFormGroup.value.numberOfRecipients; i++) {
        recipientIds.push(this.uuid.randomUUID())
      }
      createMealRequest.mealConfig.privateMealConfig = {
        recipientIds: recipientIds
      }
    }
    this.mealService.addMeal(createMealRequest)
      .pipe(catchError((it) => {
        this.failedToAddMeal();
        throw it;
      }))
      .subscribe(
      (meal) => {
        this.openAddMealSnackBar(meal.sortKey)
        this.mealService.listMeal().subscribe()
      }
    )
    this.addMealFormGroup.reset()
    this.dialogRef.close()
  }
}
