import {Component, EventEmitter, Output} from '@angular/core';
import {MatButton} from "@angular/material/button";
import {MenuParserService} from "../../../shared/api/menu-parser.service";
import {IConvertMenuItemTask} from "@the-bot-meek/food-orders-models/models/IConvertMenuItemTask";
import {
  catchError,
  delay,
  EMPTY,
  filter,
  flatMap, mergeMap,
  Observable,
  retry,
  share,
  switchMap,
  take,
  takeUntil,
  tap,
  timer
} from "rxjs";
import {map} from "rxjs/operators";
import {AsyncPipe} from "@angular/common";
import {IMenuItems} from "@the-bot-meek/food-orders-models/models/menuItems";

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
  uploading: boolean = false
  fileName: string
  @Output('menuItems')
  menuItemsEventEmitter: EventEmitter<IMenuItems[]> = new EventEmitter<IMenuItems[]>()

  constructor(private menuParserService: MenuParserService) {

  }


  public menuFileChange(fileInputEvent: any) {
    const file: File = fileInputEvent.target.files[0]
    this.fileName = file.name
    this.uploading = true
    this.convertMenuItemTask = this.menuParserService.startMenuConvertTask(file).pipe(
      mergeMap(convertMenuItemTask => this.pollMenuConvertTask(convertMenuItemTask).pipe(
        tap(() => this.uploading = false))
      )
    );

    this.convertMenuItemTask.pipe(
      tap((convertMenuItemTask: IConvertMenuItemTask) => {
        this.menuItemsEventEmitter.emit(convertMenuItemTask.results)
      }
    )).subscribe()
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
      filter(resp => Array.isArray(resp.results) && resp.results.length > 0),
      // take the first matching response and complete
      take(1)
    );
  }
}
