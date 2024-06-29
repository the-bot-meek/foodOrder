import { Component } from '@angular/core';
import { BreakpointObserver, Breakpoints } from '@angular/cdk/layout';
import { Observable } from 'rxjs';
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

@Component({
  selector: 'app-shell',
  standalone: true,
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
  styleUrl: './shell.component.scss'
})
export class ShellComponent {
  isHandset$: Observable<boolean> = this.breakpointObserver.observe([Breakpoints.Handset])
    .pipe(
      map(result => result.matches),
      shareReplay()
    );

  constructor(private breakpointObserver: BreakpointObserver, public dialog: MatDialog) {}

  openDialogComponent<T>(componentType: ComponentType<T>): void {
    this.dialog.open(componentType, {
      position: {
        top: "8vh"
      }
    })
  }

  protected readonly AddMealDialogComponent = AddMealDialogComponent;
}
