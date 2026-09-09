import type { res, req, next } from "../types/express";
import { ValidationError } from "../errors";
import HelperFunctions from "../utils/helpers";

export default async function validateBooking(req: req, res: res, next: next) {
    const { firstName, lastName, email, phone, guests, idNumber, nationality, address } = req.body;
    let { checkIn, checkOut, selectedRoom, dateOfBirth } = req.body;

    if (!firstName || !lastName || !email || !phone || !guests || !idNumber || !nationality || !selectedRoom) {
        return next(new ValidationError("Missing credentials"))
    }

    if (!HelperFunctions.validateEmail(email)) {
        return next(new ValidationError("Invalid email address"));
    }

    if (!HelperFunctions.isValidDate(checkIn) || !HelperFunctions.isValidDate(checkOut) || !HelperFunctions.isValidDate(dateOfBirth)) {
        return next(new ValidationError("Invalid date"));
    }

    if (!address.street || !address.city || !address.province || !address.country || !address.postalCode) {
        return next(new ValidationError("Missing credentials"))
    }

    next();
}