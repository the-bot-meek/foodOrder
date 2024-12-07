import {MealConfig} from "./MealConfig";

export interface IMealDisplayValues {
  name: string;
  date: string;
  location: string;
  venue: string;
  private: boolean
}

export interface IMeal {
    id: string;
    uid: string;
    name: string;
    mealDate: number;
    location: string;
    venueName: string;
    sortKey: string;
    primaryKey: string;
    private: boolean
}
