import {IMeal} from "./meal";
import {IMenuItems} from "./menuItems";

export interface IOrderParticipant {
  name: string,
  userId: string
}

export interface IOrder {
  id: string
  meal: IMeal
  uid: string
  menuItems: IMenuItems[],
  orderParticipant: IOrderParticipant
}

export interface ICreateOrderRequest {
  dateOfMeal: number
  mealId: string
  menuItems: IMenuItems[]
  organizerUid: string
}
