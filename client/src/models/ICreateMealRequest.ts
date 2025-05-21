export interface ICreatePrivateMealConfig {
    numberOfRecipients: number
}

export interface ICreateMealConfig {
    createPrivateMealConfig: ICreatePrivateMealConfig | undefined | null
    draft: boolean
}

export interface ICreateMealRequest {
    name: string,
    dateOfMeal: number,
    location: string,
    menuName: string,
    createMealConfig: ICreateMealConfig
}

export interface IDeleteMealRequest {
    uid: string;
    mealDate: number;
    id: string
}
