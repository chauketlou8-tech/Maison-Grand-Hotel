import type { req, res, next } from "../types/express";
import HelperFunctions from "../utils/helpers"

export default async function validateFeedback(req: req, res: res, next: next) {
    const { name, email, comments, rating } = req.body;

    if (!name || typeof name !== "string" || name.trim().length === 0) {
        return next(new Error("Name is required."));
    }

    if (!email || typeof email !== "string" || !HelperFunctions.validateEmail(email)) {
        return next(new Error("A valid email is required."));
    }

    if (!comments || typeof comments !== "string" || comments.trim().length === 0) {
        return next(new Error("Comments are required."));
    }

    if (typeof rating !== "number" || rating < 1 || rating > 5) {
        return next(new Error("Rating must be between 1 and 5."));
    }

    next();
}