import AppError from "./AppError";

export class CapacityError extends AppError {
    constructor(message: string, statusCode: number = 409) {
        super(message, statusCode);
        this.name = "CapacityError";
    }
}