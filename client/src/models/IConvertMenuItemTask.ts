import {IMenuItems} from "./menuItems";

export enum ConvertMenuItemTaskStatus {
    ERROR="ERROR",
    SUCCESS="SUCCESS",
    IN_PROGRESS="IN_PROGRESS",
    SUBMITTED="SUBMITTED"
}

export interface IConvertMenuItemTask {
    taskId: string;
    userId: string;
    status: ConvertMenuItemTaskStatus;
    dateCreated: number;
    results: IMenuItems[];
}