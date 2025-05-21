import {Component, EventEmitter, Input, Output} from '@angular/core';
import {MatList, MatListItem} from "@angular/material/list";
import {MatIcon} from "@angular/material/icon";
import {MatDivider} from "@angular/material/divider";
import {CurrencyPipe, NgForOf} from "@angular/common";
import {MatCard, MatCardActions} from "@angular/material/card";
import {MatButton, MatIconButton} from "@angular/material/button";
import {IMenuItems} from "../../../../models/menuItems";

@Component({
  selector: 'app-menu-item-list',
  imports: [
    MatListItem,
    MatIcon,
    MatDivider,
    MatList,
    CurrencyPipe,
    MatCard,
    NgForOf,
    MatCardActions,
    MatButton,
    MatIconButton
  ],
  templateUrl: './menu-item-list.component.html',
  standalone: true,
  styleUrl: './menu-item-list.component.scss'
})
export class MenuItemListComponent {
  @Input()
  menuItems: IMenuItems[] = []

  @Output('removeMenuItem')
  removeMenuItemEvent: EventEmitter<string> = new EventEmitter<string>()

  @Output('editMenuItem')
  editMenuItemEvent: EventEmitter<string> = new EventEmitter<string>()

  removeMenuItem(name: string): void {
    this.menuItems = this.menuItems.filter(menuItem => menuItem.name != name)
    this.removeMenuItemEvent.emit(name)
  }

  editMenuItem(name: string): void {
    this.menuItems = this.menuItems.filter(menuItem => menuItem.name != name)
    this.editMenuItemEvent.emit(name)
  }
}
