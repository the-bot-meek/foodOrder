import {IMenuItems} from "./menuItems";

export interface IConvertMenuItemTask {
    taskId: string;
    userId: string;
    status: "ERROR" | "SUCCESS" | "IN_PROGRESS";
    dateCreated: number;
    results: IMenuItems[];
}