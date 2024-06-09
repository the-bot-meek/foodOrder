import 'reflect-metadata';
import { jsonObject, jsonMember, TypedJSON, JsonObjectMetadata } from 'typedjson';

export interface PrivateMealConfig {
    recipientIds: Set<string>
}

export interface MealConfig {
    draft: boolean
    privateMealConfig: PrivateMealConfig | undefined | null
}
