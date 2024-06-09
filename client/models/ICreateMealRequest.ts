import { MealConfig } from "./MealConfig"

export interface ICreateMealRequest {
    name: string,
    dateOfMeal: number,
    location: string,
    venueName: string,
    draft: boolean
    mealConfig: MealConfig
}

export interface IDeleteMealRequest {
    uid: string;
    mealDate: number;
    id: string 
}
