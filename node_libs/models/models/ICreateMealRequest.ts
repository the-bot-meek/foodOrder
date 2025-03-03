import { MealConfig } from "./MealConfig"

export interface ICreateMealRequest {
    name: string,
    dateOfMeal: number,
    location: string,
    menuName: string,
    mealConfig: MealConfig
}

export interface IDeleteMealRequest {
    uid: string;
    mealDate: number;
    id: string
}
