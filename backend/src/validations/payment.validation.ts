import type { req, res, next } from "../types/express";
import { PaymentError } from "../errors";

export default async function validatePayment(req: req, res: res, next: next) {
    const { amount, guest_id, reservation_id } = req.body;

    if (!amount) {
        return next(new PaymentError("Amount is required"));
    }

    if (!guest_id) {
        return next(new PaymentError("Guest ID is required"));
    }

    if (amount <= 2) {
        return next(new PaymentError("Invalid Amount"));
    }

    if (!reservation_id) {
        return next(new PaymentError("Reservation ID is required"));
    }

    next()
}