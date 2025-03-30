import { Component } from '@angular/core';
import {MatDialogClose, MatDialogContent, MatDialogRef, MatDialogTitle} from "@angular/material/dialog";
import {FormControl, FormGroup, ReactiveFormsModule, ValidationErrors, ValidatorFn, Validators} from "@angular/forms";
import {ICreateMenuRequest} from "@the-bot-meek/food-orders-models/models/menu";
import {MenuService} from "../../../shared/api/menu.service";
import {IMenuItems} from "@the-bot-meek/food-orders-models/models/menuItems";
import {MatOption} from "@angular/material/autocomplete";
import {MatFormField, MatLabel} from "@angular/material/form-field";
import {MatInput} from "@angular/material/input";
import {MatSelect} from "@angular/material/select";
import {MatButton, MatIconButton} from "@angular/material/button";
import {MenuItemListComponent} from "../menu-item-list/menu-item-list.component";
import {MatIcon} from "@angular/material/icon";
import {MatSnackBar} from "@angular/material/snack-bar";
import {map} from "rxjs/operators";
import {catchError} from "rxjs";
import {TitleCasePipe} from "@angular/common";

@Component({
  selector: 'app-add-menu-dialog',
  imports: [
    ReactiveFormsModule,
    MatFormField,
    MatInput,
    MatLabel,
    MatSelect,
    MatOption,
    MatButton,
    MatDialogTitle,
    MatDialogContent,
    MenuItemListComponent,
    MatIcon,
    MatIconButton,
    MatDialogClose,
    TitleCasePipe
  ],
  templateUrl: './add-menu-dialog.component.html',
  standalone: true,
  styleUrl: './add-menu-dialog.component.scss'
})
export class AddMenuDialogComponent {
  locations: string[] = ['London', 'Kirkwall']
  menuItems: IMenuItems[] = []
  private menuItemCategory = ['STARTER', 'MAIN', 'DESSERT', 'DRINK', 'SIDE']
  menuFormGroup = new FormGroup({
    name: new FormControl<string>('', [Validators.required]),
    location: new FormControl<string>('', [Validators.required]),
    phoneNumber: new FormControl<string>('', [Validators.required]),
    description: new FormControl<string>('', [Validators.required])
  });

  menuItemValidator: ValidatorFn = (it): ValidationErrors => {
    return  this.menuItems.map(it => it.name).includes(it.value) ? {"nonUniqueItem": "Can't add two menu items with same name"} : {}
  }
  addMenuItemForm = new FormGroup({
    name: new FormControl<string>('', [Validators.required, this.menuItemValidator]),
    description: new FormControl<string>('', [Validators.required]),
    price: new FormControl<number>(0, [Validators.required, Validators.min(0.01)]),
    menuItemCategory: new FormControl<string>('', [Validators.required])
  })

  constructor(
    private menuService:MenuService,
    public dialogRef: MatDialogRef<AddMenuDialogComponent>,
    private snackBar: MatSnackBar
  ) {
  }

  saveMenu(): void {
    const addMenuRequest: ICreateMenuRequest = {
      location: this.menuFormGroup.value.location as string,
      description: this.menuFormGroup.value.description as string,
      phoneNumber: this.menuFormGroup.value.phoneNumber as string,
      name: this.menuFormGroup.value.name as string,
      menuItems: this.menuItems
    }
    this.menuService.addMenu(addMenuRequest).pipe(map(() => {
      this.snackBar.open("Menu Added", null, {
        horizontalPosition: 'center', verticalPosition: 'top', duration: 7500
      })
    }),
    catchError(err => {
      this.failedToAddMenu()
      throw err;
    })).subscribe()
    this.addMenuItemForm.reset()
    this.menuFormGroup.reset()
    this.menuItems = [];
    this.dialogRef.close()
  }

  removeMenuItem(name: string) {
    this.menuItems = this.menuItems.filter(menuItem => menuItem.name != name)
  }

  addMenuItem(): void {
    console.log(this.addMenuItemForm.value)
    this.menuItems.push(this.addMenuItemForm.value as IMenuItems)
    this.addMenuItemForm.reset()
  }



  private failedToAddMenu() {
    this.snackBar.open("Failed to add menu", null, {
      horizontalPosition: 'center', verticalPosition: 'top', duration: 7500
    });
  }

  get menuItemCatogorys() {
    return this.menuItemCategory
  }

  protected readonly Object = Object;
}
