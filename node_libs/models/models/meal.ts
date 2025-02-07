import {MealConfig} from "./MealConfig";

export interface IMeal {
    id: string;
    uid: string;
    name: string;
    mealDate: number;
    location: string;
    venueName: string;
    sortKey: string;
    primaryKey: string;
    mealConfig: MealConfig;
}
