import {Component, Input} from '@angular/core';
import {Observable} from "rxjs";
import {AsyncPipe, TitleCasePipe} from "@angular/common";
import {MatTableModule} from "@angular/material/table";
import {MatButton} from "@angular/material/button";
import {environment} from "../../../../environments/environment";
import {CdkCopyToClipboard} from "@angular/cdk/clipboard";
import {IOrder} from "../../../../models/order";

@Component({
  selector: 'app-order-list',
  imports: [
    AsyncPipe,
    MatTableModule,
    MatButton,
    CdkCopyToClipboard,
    TitleCasePipe
  ],
  templateUrl: './order-list.component.html',
  standalone: true,
  styleUrl: './order-list.component.scss'
})
export class OrderListComponent {
  @Input()
  orders: Observable<IOrder[]>
  readonly placeOrderBasePath: string = environment.defaultClientBasePath + "/order/"

  columnsToDisplay = ['participantsName', "copyInviteLink"]
}
