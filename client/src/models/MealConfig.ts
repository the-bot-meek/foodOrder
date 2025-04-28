import 'reflect-metadata';

export interface PrivateMealConfig {
    recipientIds: string[]
}

export interface MealConfig {
    draft: boolean
    privateMealConfig: PrivateMealConfig | undefined | null
}
