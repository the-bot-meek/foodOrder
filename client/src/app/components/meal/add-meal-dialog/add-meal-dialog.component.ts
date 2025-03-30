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
import {IMenu} from "@the-bot-meek/food-orders-models/models/menu";
import {MenuService} from "../../../shared/api/menu.service";
import {MealService} from "../../../shared/api/meal.service";
import {ICreateMealRequest} from "@the-bot-meek/food-orders-models/models/ICreateMealRequest";
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
      mealConfig: {
        draft: false,
        privateMealConfig: null
      },
      name: this.addMealFormGroup.value.name as string,
      menuName: this.addMealFormGroup.value.menuName as string
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
        this.orderService.addAnonymousOrders(meal.sortKey).subscribe()
        this.mealService.listMeal().subscribe()
      }
    )
    this.addMealFormGroup.reset()
    this.dialogRef.close()
  }
}
