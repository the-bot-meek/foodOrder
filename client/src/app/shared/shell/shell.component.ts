import { Component } from '@angular/core';
import { BreakpointObserver, Breakpoints } from '@angular/cdk/layout';
import {Observable} from 'rxjs';
import { map, shareReplay } from 'rxjs/operators';
import { MatButtonModule } from '@angular/material/button';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatIconModule } from '@angular/material/icon';
import { LayoutModule } from '@angular/cdk/layout';
import { MatSidenavModule } from '@angular/material/sidenav';
import { MatListModule } from '@angular/material/list';
import { MatMenuModule } from '@angular/material/menu';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import {AsyncPipe, NgIf} from "@angular/common";
import {AddMealDialogComponent} from "../../components/meal/add-meal-dialog/add-meal-dialog.component";
import {MatDialog} from "@angular/material/dialog";
import {ComponentType} from "@angular/cdk/overlay";
import {AddMenuDialogComponent} from "../../components/menu/add-menu-dialog/add-menu-dialog.component";
import {AuthService} from "../services/auth/auth.service";
import {IUser} from "../../../models/IUser";


@Component({
  selector: 'app-shell',
  imports: [
    MatButtonModule,
    MatToolbarModule,
    MatIconModule,
    LayoutModule,
    MatSidenavModule,
    MatListModule,
    MatMenuModule,
    MatIconModule,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatSnackBarModule,
    AsyncPipe,
    NgIf
  ],
  templateUrl: './shell.component.html',
  standalone: true,
  styleUrl: './shell.component.scss'
})
export class ShellComponent {
  isHandset$: Observable<boolean> = this.breakpointObserver.observe([Breakpoints.Handset])
    .pipe(
      map(result => result.matches),
      shareReplay()
    );

  constructor(private breakpointObserver: BreakpointObserver, public dialog: MatDialog, private authService: AuthService) {}

  get userDetails(): IUser {
    return this.authService.userDetails;
  }

  openDialogComponent<T>(componentType: ComponentType<T>): void {
    this.dialog.open(componentType, {
      position: {
        top: "8vh"
      },
      maxWidth: "100%",
      minWidth: "80%",
      minHeight: "60vh",
      maxHeight: "100vh",
      autoFocus: false
    })
  }

  protected readonly AddMealDialogComponent = AddMealDialogComponent;
  protected readonly AddMenuDialogComponent = AddMenuDialogComponent;
}
