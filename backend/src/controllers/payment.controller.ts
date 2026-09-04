import asyncHandler from "../middleware/asyncHandler";
import connection from "../config/connection";
import type { req, res } from "../types/express";
import { yoco_secret } from "../config/env";
import Response from "../utils/response";
import type { Payment } from "../types/payment";
import type { Notification } from "../types/notification";
import sendEmail from "../services/email.service";
import { base_url } from "../config/env"

const pay = asyncHandler(async (req: req, res: res) => {
    const amount = req.body.amount;
    const idNumber = req.body.idNumber;

    const response = await fetch("https://payments.yoco.com/api/checkouts", {
        method: "POST",
        headers: {
            "Authorization": `Bearer ${yoco_secret}`,
            "Content-Type": "application/json"
        },
        body: JSON.stringify({
            "amount": Math.round(amount * 100),
            "currency": "ZAR",
            successUrl: "http://localhost:5173/booking/success",
        }),
    });

    const body = await response.json();

    const payment: Payment = {
        amount: Number(body.amount),
        paymentDate: new Date(),
        paymentStatus: body.status,
        transactionReference: body.id
    }

    const guestResult = await connection.query(`select id, firstname, lastname from guests where idnumber = $1`, [idNumber])
    const guest_id = guestResult.rows[0].id;
    const firstName = guestResult.rows[0].firstname;
    const lastName = guestResult.rows[0].lastname;
    const name = `${firstName} ${lastName}`;

    const reservationResults = await connection.query(`
    select r.reservationid, r.checkindate, r.checkoutdate, r.numberofguests, r.requestedroomtype, g.email
    from reservations r join guests g on r.guest_id = g.id where g.idnumber = $1
    `, [idNumber]);

    const reservation = reservationResults.rows[0];
    const reservationId = reservation.reservationid;

    const checkIn = new Date(reservation.checkindate).toLocaleString("en-ZA", { day: "2-digit", month: "long", year: "numeric" });
    const checkOut = new Date(reservation.checkoutdate).toLocaleString("en-ZA", { day: "2-digit", month: "long", year: "numeric" });

    const guests = reservation.numberofguests;
    const roomName = reservation.requestedroomtype;
    const email = reservation.email;

    const pymt = await connection.query(`
        insert into payments(amount, paymentdate, paymentstatus, transactionreference, guest_id) 
        values($1, $2, $3, $4, $5)
        returning id
    `, [payment.amount, payment.paymentDate, payment.paymentStatus, payment.transactionReference, guest_id])

    const paymentID = pymt.rows[0].id;

    const notification: Notification = {
        type: "payment_confirmation",
        title: "New Payment notification",
        message: `${name} has made a payment.`,
        payment_id: paymentID,
        guest_id: guest_id,
        status: "unread",
        createdAt: new Date()
    }

    await connection.query(`
        insert into notifications(type, title, message, payment_id, guestid) 
        values($1, $2, $3, $4, $5)
    `, [notification.type, notification.title, notification.message, notification.payment_id, notification.guest_id]);

    const emailSubject = "Mason Hotel — Payment Confirmation";
    const paymentDate = new Date(payment.paymentDate).toLocaleString("en-ZA", { day: "2-digit", month: "long", year: "numeric", hour: "2-digit", minute: "2-digit", hour12: false });
    const emailMessage = `Dear ${name},

Thank you for your payment to Mason Hotel.

We are pleased to confirm that your payment has been successfully received and processed. Your reservation is now confirmed.

PAYMENT DETAILS
─────────────────
Amount Paid: R${amount}
Payment Status: Confirmed
Transaction Reference: ${payment.transactionReference}
Payment Date: ${paymentDate}

RESERVATION DETAILS
─────────────────
Reservation ID: ${reservationId}
Check-in: ${checkIn}
Check-out: ${checkOut}
Guests: ${guests}
Room: ${roomName}

VIEW YOUR BOOKING
─────────────────
To view or manage your booking at any time, visit:
${base_url}/booking/${reservationId}

Your payment has been successfully recorded against your reservation. Please keep this email as proof of payment.

We look forward to welcoming you to Mason Hotel.

Kind regards,

Mason Hotel
Guest Services
stay@maisongrandhotel.co.za
+27 21 987 6543
08 Greenwood Street, Gardens, Cape Town, 7700
www.maisongrandhotel.co.za`;

    await sendEmail(email, emailSubject, emailMessage, name);
    return Response.success(res,
        {
        message: "payment request",
            data: {
            redirectUrl: body.redirectUrl
        }
    });
});

export default pay;