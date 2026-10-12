import type { res, req, next } from "../types/express";
import { ValidationError, PaymentError } from "../errors";
import type { Settings } from "../types/settings";
import HelperFunctions from "../utils/helpers";
import getSettings from "../sockets/settingsClient";

export default async function validateBooking(req: req, res: res, next: next) {
    const { firstName, lastName, email, phone, guests, idNumber, nationality, address, total } = req.body;
    let { checkIn, checkOut, selectedRoom, dateOfBirth } = req.body;

    const settings: Settings | null = getSettings();

    if (!firstName || !lastName || !email || !phone || !guests || !idNumber || !nationality || !selectedRoom || !total) {
        return next(new ValidationError("Missing credentials"))
    }

    if (!HelperFunctions.validateEmail(email)) {
        return next(new ValidationError("Invalid email address"));
    }

    if (!HelperFunctions.isValidDate(checkIn) || !HelperFunctions.isValidDate(checkOut) || !HelperFunctions.isValidDate(dateOfBirth)) {
        return next(new ValidationError("Invalid date"));
    }

    if (!address.street || !address.city || !address.province || !address.country || !address.postalCode) {
        return next(new ValidationError("Missing credentials"));
    }

    const totalNum = Number(total);
    if (!Number.isFinite(totalNum) || totalNum <= 0) {
        return next(new PaymentError("Illegal amount for total"));
    }

    if (settings) {
        const inDate = new Date(checkIn.split("T")[0]);
        const outDate = new Date(checkOut.split("T")[0]);
        const today = new Date();
        today.setHours(0, 0, 0, 0);

        const nights = Math.round((outDate.getTime() - inDate.getTime()) / 86_400_000);

        if (nights < settings.min_nights) {
            return next(new ValidationError(`Minimum stay is ${settings.min_nights} night(s)`));
        }

        if (nights > settings.max_nights) {
            return next(new ValidationError(`Maximum stay is ${settings.max_nights} night(s)`));
        }

        const daysAhead = Math.round((inDate.getTime() - today.getTime()) / 86_400_000);

        if (daysAhead > settings.advance_booking_limit_days) {
            return next(new ValidationError(`Bookings can only be made up to ${settings.advance_booking_limit_days} days in advance`));
        }
    }

    next();
}