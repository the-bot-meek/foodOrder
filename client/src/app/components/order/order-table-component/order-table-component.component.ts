import {Component, Input, OnInit} from '@angular/core';
import {IOrder} from "../../../../../models/order";
import {JsonPipe} from "@angular/common";
import {MatTable, MatTableModule} from "@angular/material/table";

@Component({
  selector: 'app-order-table-component',
  imports: [
    JsonPipe,
    MatTableModule
  ],
  templateUrl: './order-table-component.component.html',
  standalone: true,
  styleUrl: './order-table-component.component.scss'
})
export class OrderTableComponentComponent implements OnInit{
  @Input()
  orders: IOrder[]

  displayedColumns: string[] = ['meal', 'participantsName', 'numberOfMenuItems'];

  ngOnInit(): void {
  }
}
