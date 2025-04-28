import {Component, inject} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {FormsModule} from "@angular/forms";
import {MatFormField, MatInput, MatLabel} from "@angular/material/input";
import {MatButton} from "@angular/material/button";
import {OrderService} from "../../../shared/api/order.service";
import {IOrder} from "../../../../models/order";
import {IMenuItems} from "../../../../models/menuItems";

@Component({
  selector: 'app-confirm-anonomus-order-deatils',
  imports: [
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
  readonly data = inject<{order: IOrder, selectedItems: IMenuItems[]}>(MAT_DIALOG_DATA);
  name: string = '';
  constructor(
    private dialogRef: MatDialogRef<ConfirmAnonomusOrderDetailsModalComponent>,
    private orderService: OrderService

  ) {

  }

  confirmNameAndSubmitOrder(order: IOrder, name: string): void {
    order.orderParticipant.name = name;
    order.menuItems = this.data.selectedItems
    this.orderService.updateAnonymousOrder(order).subscribe()
    this.dialogRef.close()
  }

}
