import {Component, EventEmitter, Input, Output} from '@angular/core';
import {IMenuItems} from "@the-bot-meek/food-orders-models/models/menuItems";
import {MatList, MatListItem} from "@angular/material/list";
import {MatIcon} from "@angular/material/icon";
import {MatDivider} from "@angular/material/divider";

@Component({
    selector: 'app-menu-item-list',
    imports: [
        MatListItem,
        MatIcon,
        MatDivider,
        MatList
    ],
    templateUrl: './menu-item-list.component.html',
    styleUrl: './menu-item-list.component.scss'
})
export class MenuItemListComponent {
  @Input()
  menuItems: IMenuItems[] = []

  @Output('removeMenuItem')
  removeMenuItemEvent: EventEmitter<string> = new EventEmitter<string>()

  removeMenuItem(name: string): void {
    this.menuItems = this.menuItems.filter(menuItem => menuItem.name != name)
    this.removeMenuItemEvent.emit(name)
  }
}
