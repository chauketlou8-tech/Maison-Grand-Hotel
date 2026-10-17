import asyncHandler from "../middleware/asyncHandler";

import connection from "../config/connection";
import { yoco_secret, yoco_webhook_secret, base_url, success_url, payment_url } from "../config/env";

import sendEmail from "../services/email.service";
import { PaymentError } from "../errors";

import Response from "../utils/response";
import HelperFunctions from "../utils/helpers";

import type { req, res, next } from "../types/express";
import type { Notification } from "../types/notification";
import type { Payment } from "../types/payment";
import type { Reservation } from "../types/reservation";
import type { Settings } from "../types/settings";

import getSettings from "../sockets/settingsClient";

const pay = asyncHandler(async (req: req, res: res) => {
    const { amount, guest_id, reservation_id } = req.body;

    const response = await fetch(`${payment_url}`, {
        method: "POST",
        headers: {
            "Authorization": `Bearer ${yoco_secret}`,
            "Content-Type": "application/json"
        },
        body: JSON.stringify({
            "amount": Math.round(amount * 100),
            "currency": "ZAR",
            successUrl: `${success_url}`,
        }),
    });

    const body = await response.json();

    await connection.query(`
        insert into payments(amount, paymentstatus, transactionreference, guest_id, reservation_id, yoco_checkout_id)
        values($1, $2, $3, $4, $5, $6)
    `, [amount, "started", null, guest_id, reservation_id, body.id]);

    return Response.success(res, {
        message: "payment request",
        data: {
            redirectUrl: body.redirectUrl
        }
    });
});

const yocoWebhook = asyncHandler(async (req: req, res: res, next: next) => {
    //@ts-ignore
    const rawBody = req.rawBody;
    const settings: Settings | null = getSettings();

    if (!HelperFunctions.isValidSignature(rawBody, req.headers, yoco_webhook_secret!)) {
        return next(new PaymentError("Invalid Signature", 401));
    }

    const event = JSON.parse(rawBody.toString("utf8"));

    if (event.type !== "payment.succeeded") {
        return Response.success(res, {
            message: "Event ignored"
        });
    }

    const checkoutId = event.payload.metadata.checkoutId;
    const paymentMethod = event.payload.paymentMethodDetails.type;

    if (!checkoutId) {
        return Response.success(res, {
            data: {
                received: true
            }
        });
    }

    const paymentResult = await connection.query(`
        select id, amount, paymentstatus, guest_id, transactionreference from payments
        where yoco_checkout_id = $1
    `, [checkoutId]);

    if (paymentResult.rows.length === 0) {
        return next(new PaymentError("Payment not found", 404))
    }

    const payment: Payment = paymentResult.rows[0];

    if (payment.paymentstatus === "completed" || payment.paymentstatus === "succeeded") {
        return Response.success(res, {
            message: "Payment already processed"
        });
    }

    const results = await connection.query(`
        update payments set paymentstatus = $1, transactionreference = $2, paymentdate = now(), paymentmethod = $3, updated_at = now() where id = $4
        returning *
    `, ["completed", event.id, paymentMethod, payment.id]);

    if (results.rowCount == 0) {
        return Response.success(res, {
            data: {
                received: true
            }
        });
    }

    const newPayment: Payment = results.rows[0];

    const guestResult = await connection.query(`
        select id, firstname, lastname, email from guests where id = $1
    `, [payment.guest_id]);

    const guest = guestResult.rows[0];

    const name = `${guest.firstname} ${guest.lastname}`;

    const reservationResult = await connection.query(`
        update reservations set reservationstatus = 'PROCESSING' where guest_id = $1
        returning reservationid, checkindate, checkoutdate, numberofguests, requestedroomtype
    `, [payment.guest_id]);

    const reservation: Reservation = reservationResult.rows[0];

    const notification: Notification = {
        type: "payment_confirmation",
        title: "New Payment notification",
        message: `${name} has made a payment.`,
        payment_id: payment.id,
        guest_id: payment.guest_id,
        status: "unread",
        created_at: new Date(),
        updated_at: new Date()
    }

    if (settings?.notify_staff_new_payment) {
        await connection.query(`
            insert into notifications(type, title, message, payment_id, guestid)
            values($1, $2, $3, $4, $5)
        `, [notification.type, notification.title, notification.message, notification.payment_id, notification.guest_id]);
    }

    if (settings && settings.notify_payment_confirmation) {
        const checkIn = new Date(reservation.checkindate).toLocaleString("en-ZA", { day: "2-digit", month: "long", year: "numeric" });
        const checkOut = new Date(reservation.checkoutdate).toLocaleString("en-ZA", { day: "2-digit", month: "long", year: "numeric" });

        const guests = reservation.numberofguests;
        const roomName = reservation.requestedroomtype;
        const email = guest.email;

        const address = [
            settings.hotel_street,
            settings.hotel_city,
            settings.hotel_province,
            settings.hotel_postal_code
        ].filter(Boolean).join(", ");

        const emailSubject = `${settings.hotel_name} — Payment Confirmation`;
        const paymentDate = new Date(newPayment.paymentdate).toLocaleString("en-ZA", { day: "2-digit", month: "long", year: "numeric" });
        const emailMessage = `Dear ${name},

Thank you for your payment to ${settings.hotel_name}.

We are pleased to confirm that your payment has been successfully received and processed. Your reservation is now confirmed.

PAYMENT DETAILS
─────────────────
Amount Paid: R${newPayment.amount}
Payment Status: Confirmed
Transaction Reference: ${newPayment.transactionreference}
Payment Date: ${paymentDate}

RESERVATION DETAILS
─────────────────
Reservation ID: ${reservation.reservationid}
Check-in: ${checkIn}
Check-out: ${checkOut}
Guests: ${guests}
Room: ${roomName}

VIEW YOUR BOOKING
─────────────────
To view or manage your booking at any time, visit:
${base_url}/booking/${reservation.reservationid}

Your payment has been successfully recorded against your reservation. Please keep this email as proof of payment.

We look forward to welcoming you to ${settings.hotel_name}.

Kind regards,

${settings.hotel_name}
Guest Services
${settings.hotel_email}
${settings.hotel_phone}
${address}
${base_url}`;

        await sendEmail(email, emailSubject, emailMessage, name);
    }

    return Response.success(res, {
        message: "Payment successful",
        data: {
            success: true,
        }
    });
});

export {
    pay,
    yocoWebhook
}