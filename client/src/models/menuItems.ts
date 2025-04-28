export interface IMenuItems {
  name: string
  description: string
  price: number,
  menuItemCategory: "STARTER" | "MAIN" | "DESSERT" | "DRINK" | "SIDE"
}
