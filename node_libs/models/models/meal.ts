import {MealConfig} from "./MealConfig";

export interface IMeal {
    id: string;
    uid: string;
    name: string;
    mealDate: number;
    location: string;
    menuName: string;
    sortKey: string;
    primaryKey: string;
    mealConfig: MealConfig;
}
