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
import {MenuService} from "../../../shared/api/menu.service";
import {MealService} from "../../../shared/api/meal.service";
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
import {OrderService} from "../../../shared/api/order.service";
import {IMenu} from "../../../../models/menu";
import {ICreateMealConfig, ICreateMealRequest} from "../../../../models/ICreateMealRequest";


@Component({
  selector: 'app-add-meal-dialog',
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
  standalone: true,
  styleUrl: './add-meal-dialog.component.scss'
})
export class AddMealDialogComponent {
  menus: IMenu[] = []
  constructor(
    private menuService: MenuService,
    private mealService: MealService,
    public dialogRef: MatDialogRef<AddMealDialogComponent>,
    private snackBar: MatSnackBar,
    private router: Router,
    private uuid: UUIDService,
    private orderService: OrderService
  ) {
  }

  locations: string[] = ['Kirkwall', 'London']
  addMealFormGroup = new FormGroup({
    name: new FormControl<string>("", [Validators.required]),
    dateOfMeal: new FormControl<Date>(new Date(), [Validators.required]),
    location: new FormControl<string>('', [Validators.required]),
    menuName: new FormControl<string>('', [Validators.required]),
    privateMeal: new FormControl<boolean>(false),
    numberOfRecipients: new FormControl<number>(1)
  })

  getMenusForLocation(locationName: string) {
    this.menuService.listMenusForLocation(locationName).subscribe(menuLocations =>
      this.menus = menuLocations
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
      horizontalPosition: 'center', verticalPosition: 'top', duration: 7500
    });
  }

  addMeal(): void {
    const createMealRequest: ICreateMealRequest = {
      dateOfMeal: this.addMealFormGroup.value.dateOfMeal?.getTime() as number,
      location: this.addMealFormGroup.value.location as string,
      createMealConfig: {
        draft: false
      } as ICreateMealConfig,
      name: this.addMealFormGroup.value.name as string,
      menuName: this.addMealFormGroup.value.menuName as string
    }
    if (this.addMealFormGroup.value.privateMeal) {
      createMealRequest.createMealConfig.createPrivateMealConfig = {
        numberOfRecipients: this.addMealFormGroup.value.numberOfRecipients
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
          this.orderService.addAnonymousOrders(meal.sortKey).subscribe()
          this.mealService.listMeal().subscribe()
        }
      )
    this.addMealFormGroup.reset()
    this.dialogRef.close()
  }
}
