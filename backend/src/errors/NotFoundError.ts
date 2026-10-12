import AppError from "./AppError";

export class NotFoundError extends AppError {
    constructor(message: string, statusCode: number = 404) {
        super(message, statusCode);
        this.name = "NotFoundError";
    }
}