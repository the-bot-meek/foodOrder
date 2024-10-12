import { Component } from '@angular/core';
import {MatDialogClose, MatDialogContent, MatDialogRef, MatDialogTitle} from "@angular/material/dialog";
import {FormControl, FormGroup, ReactiveFormsModule, ValidationErrors, ValidatorFn, Validators} from "@angular/forms";
import {ICreateVenueRequest} from "../../../../../models/venue";
import {VenueService} from "../../../shared/api/venue.service";
import {IMenuItems} from "../../../../../models/menuItems";
import {MatOption} from "@angular/material/autocomplete";
import {MatDateRangeInput} from "@angular/material/datepicker";
import {MatFormField, MatLabel} from "@angular/material/form-field";
import {MatInput} from "@angular/material/input";
import {MatSelect} from "@angular/material/select";
import {MatButton, MatIconButton} from "@angular/material/button";
import {JsonPipe} from "@angular/common";
import {MenuItemListComponent} from "../menu-item-list/menu-item-list.component";
import {MatIcon} from "@angular/material/icon";
import {MatSnackBar} from "@angular/material/snack-bar";
import {map} from "rxjs/operators";

@Component({
  selector: 'app-add-venue-dialog',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    MatDateRangeInput,
    MatFormField,
    MatInput,
    MatLabel,
    MatSelect,
    MatOption,
    MatButton,
    JsonPipe,
    MatDialogTitle,
    MatDialogContent,
    MenuItemListComponent,
    MatIcon,
    MatIconButton,
    MatDialogClose
  ],
  templateUrl: './add-venue-dialog.component.html',
  styleUrl: './add-venue-dialog.component.scss'
})
export class AddVenueDialogComponent {
  locations: string[] = ['London', 'Kirkwall']
  menuItems: IMenuItems[] = []
  venueFormGroup = new FormGroup({
    name: new FormControl<string>('', [Validators.required]),
    location: new FormControl<string>('', [Validators.required]),
    description: new FormControl<string>('', [Validators.required])
  });

  menuItemValidator: ValidatorFn = (it): ValidationErrors => {
    return  this.menuItems.map(it => it.name).includes(it.value) ? {"nonUniqueItem": "Can't add two menu items with same name"} : {}
  }
  addMenuItemForm = new FormGroup({
    name: new FormControl<string>('', [Validators.required, this.menuItemValidator]),
    description: new FormControl<string>('', [Validators.required]),
    price: new FormControl<number>(0, [Validators.required, Validators.min(0.01)])
  })

  constructor(
    private venueService:VenueService,
    public dialogRef: MatDialogRef<AddVenueDialogComponent>,
    private snackBar: MatSnackBar
  ) {
  }

  saveVenue(): void {
    const addVenueRequest: ICreateVenueRequest = {
      location: this.venueFormGroup.value.location as string,
      description: this.venueFormGroup.value.description as string,
      name: this.venueFormGroup.value.name as string,
      menuItems: this.menuItems
    }
    this.venueService.addVenue(addVenueRequest).pipe(map(() => {
      this.snackBar.open("Venue Added", null, {
        horizontalPosition: 'end', verticalPosition: 'top', duration: 7500
      })
    })).subscribe()
    this.addMenuItemForm.reset()
    this.venueFormGroup.reset()
    this.menuItems = [];
    this.dialogRef.close()
  }

  removeMenuItem(name: string) {
    this.menuItems = this.menuItems.filter(menuItem => menuItem.name != name)
  }

  addMenuItem(): void {
    this.menuItems.push(this.addMenuItemForm.value as IMenuItems)
    this.addMenuItemForm.reset()
  }

}
