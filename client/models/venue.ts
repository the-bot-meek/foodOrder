import {IMenuItems} from "./menuItems";

export interface IVenue {
  id: string
  name: string
  location: string
  description: string
  menuItems: IMenuItems[]
}

export interface ICreateVenueRequest {
  menuItems: IMenuItems[]
  location: string
  name: string
  description: string
}
