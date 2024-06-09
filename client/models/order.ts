import {IMeal} from "./meal";
import {IMenuItems} from "./menuItems";

export interface IOrder {
  id: string
  meal: IMeal
  uid: string
  participantsName: string
  menuItems: IMenuItems[]
}

export interface ICreateOrderRequest {
  dateOfMeal: number
  mealId: string
  menuItems: IMenuItems[]
  organizerUid: string
}
