import {Component, inject} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {IOrder} from "@the-bot-meek/food-orders-models/models/order";
import {AsyncPipe, JsonPipe} from "@angular/common";
import {Observable} from "rxjs";
import {MatIcon} from "@angular/material/icon";
import {FormsModule} from "@angular/forms";
import {MatFormField, MatInput, MatLabel} from "@angular/material/input";
import {MatButton} from "@angular/material/button";
import {OrderService} from "../../../shared/api/order.service";

@Component({
  selector: 'app-confirm-anonomus-order-deatils',
  imports: [
    JsonPipe,
    AsyncPipe,
    FormsModule,
    MatInput,
    MatFormField,
    MatLabel,
    MatButton,
  ],
  templateUrl: './confirm-anonomus-order-details-modal.component.html',
  standalone: true,
  styleUrl: './confirm-anonomus-order-details-modal.component.scss'
})
export class ConfirmAnonomusOrderDetailsModalComponent {
  readonly data = inject<{order: Observable<IOrder>}>(MAT_DIALOG_DATA);
  name: string = '';
  constructor(
    private dialogRef: MatDialogRef<ConfirmAnonomusOrderDetailsModalComponent>,
    private orderService: OrderService

  ) {

  }

  confirmNameAndSubmitOrder(order: IOrder, name: string): void {
    order.orderParticipant.name = name;
    this.orderService.updateAnonymousOrder(order).subscribe()
    this.dialogRef.close()
  }

}
