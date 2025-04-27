import {Component, EventEmitter, Output} from '@angular/core';
import {MatButton} from "@angular/material/button";
import {MenuParserService} from "../../../shared/api/menu-parser.service";
import {IConvertMenuItemTask, ConvertMenuItemTaskStatus} from "@the-bot-meek/food-orders-models/models/IConvertMenuItemTask";
import {
  filter,
  mergeMap,
  Observable,
  switchMap,
  take,
  tap,
  timer
} from "rxjs";
import {AsyncPipe} from "@angular/common";
import {IMenuItems} from "@the-bot-meek/food-orders-models/models/menuItems";
import {MatSnackBar} from "@angular/material/snack-bar";

@Component({
  selector: 'app-menu-upload',
  imports: [
    MatButton,
    AsyncPipe
  ],
  templateUrl: './menu-upload.component.html',
  standalone: true,
  styleUrl: './menu-upload.component.scss'
})
export class MenuUploadComponent {
  convertMenuItemTask: Observable<IConvertMenuItemTask>
  uploadingStatus: ConvertMenuItemTaskStatus
  fileName: string
  @Output('menuItems')
  menuItemsEventEmitter: EventEmitter<IMenuItems[]> = new EventEmitter<IMenuItems[]>()

  constructor(private menuParserService: MenuParserService, private snackBar: MatSnackBar) {

  }


  public menuFileChange(fileInputEvent: any) {
    const file: File = fileInputEvent.target.files[0]
    this.fileName = file.name
    this.uploadingStatus = ConvertMenuItemTaskStatus.SUBMITTED
    this.convertMenuItemTask = this.menuParserService.startMenuConvertTask(file).pipe(
      mergeMap(convertMenuItemTask => this.pollMenuConvertTask(convertMenuItemTask).pipe(
        tap(convertMenuTask => this.uploadingStatus = convertMenuTask.status))
      )
    );

    this.convertMenuItemTask.pipe(
      tap((convertMenuItemTask: IConvertMenuItemTask) => {
        if (convertMenuItemTask.status == ConvertMenuItemTaskStatus.SUCCESS) {
          this.menuItemsEventEmitter.emit(convertMenuItemTask.results)
        }
        this.uploadingStatus = convertMenuItemTask.status;
      }),
      tap((convertMenuItemTask: IConvertMenuItemTask) => {
        const msg = convertMenuItemTask.status == ConvertMenuItemTaskStatus.ERROR ? 'Failed to Parse Menu Items' : 'Parsed Menu Items';
        this.snackBar.open(msg, null, {
          horizontalPosition: 'center', verticalPosition: 'top', duration: 7500
        });
      })).subscribe()
  }

  public pollMenuConvertTask(
    convertMenuItemTask: IConvertMenuItemTask,
  ): Observable<IConvertMenuItemTask> {
    return timer(0, 3500).pipe(
      // each tick, fetch the task
      switchMap(() =>
        this.menuParserService.getMenuConvertTask(convertMenuItemTask.taskId)
      ),
      // only let through those responses with at least one menuItem
      filter(resp => !this.isProcessing(resp.status)),
      // take the first matching response and complete
      take(1)
    );
  }

  isProcessing(convertMenuItemTaskStatus: ConvertMenuItemTaskStatus): boolean {
    return [ConvertMenuItemTaskStatus.SUBMITTED.toString(), ConvertMenuItemTaskStatus.IN_PROGRESS.toString()].includes(convertMenuItemTaskStatus)
  }
}
