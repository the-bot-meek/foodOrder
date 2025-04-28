import {IMenuItems} from "./menuItems";

export interface IMenu {
  id: string
  name: string
  location: string
  description: string
  menuItems: IMenuItems[]
}

export interface ICreateMenuRequest {
  menuItems: IMenuItems[]
  location: string
  name: string
  description: string,
  phoneNumber: string
}
