import 'reflect-metadata';

export interface PrivateMealConfig {
    recipientIds: Set<string>
}

export interface MealConfig {
    draft: boolean
    privateMealConfig: PrivateMealConfig | undefined | null
}
