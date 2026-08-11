import AppError from "./AppError";

export class RoomError extends AppError {
    constructor(message: string, statusCode: number = 500) {
        super(message, statusCode);
        this.name = "RoomError";
    }
}