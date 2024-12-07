import { MealConfig } from "./MealConfig"
import {ICreateOrderRequest} from "./order";

export interface ICreateMealRequest {
    name: string,
    dateOfMeal: number,
    location: string,
    venueName: string,
    isDraft: boolean,
    mealType: 'Meal' | 'PrivateMeal'
}

export interface ICreatePrivateMealRequest extends ICreateMealRequest {
  numberOfOrders: number;
}

export interface IDeleteMealRequest {
    uid: string;
    mealDate: number;
    id: string
}
