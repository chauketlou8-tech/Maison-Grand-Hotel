import asyncHandler from "../middleware/asyncHandler";
import connection from "../config/connection";
import type { req, res } from "../types/express";
import { yoco_secret } from "../config/env";
import Response from "../utils/response";
import type { Payment } from "../types/payment";
import type { Notification } from "../types/notification";
import sendEmail from "../services/email.service";

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

    const reservationResults = await connection.query(`
    select r.reservationid, r.checkindate, r.checkoutdate, r.numberofguests, r.requestedroomtype, g.email
    from reservations r join guests g on r.guest_id = g.id where g.idnumber = $1
    `, [idNumber]);

    const reservation = reservationResults.rows[0];
    const reservationId = reservation.reservationid;
    const checkIn = reservation.checkindate;
    const checkOut = reservation.checkoutdate;
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
        message: `${firstName} ${lastName} has made a payment.`,
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
    const emailMessage = `Dear ${firstName} ${lastName},

Thank you for your payment to Mason Hotel.

We are pleased to confirm that your payment has been successfully received and processed.

PAYMENT DETAILS

Amount Paid: R${amount}
Payment Status: Confirmed
Transaction Reference: ${payment.transactionReference}
Payment Date: ${payment.paymentDate}

RESERVATION DETAILS

Reservation ID: ${reservationId}
Check-in: ${checkIn}
Check-out: ${checkOut}
Guests: ${guests}
Room: ${roomName}

Your payment has been successfully recorded against your reservation.

Please keep this email as proof of payment and confirmation of your transaction.

We look forward to welcoming you to Mason Hotel.

Kind regards,

Mason Hotel
Guest Services
[masonGrand@lequor.co.za](mailto:masonGrand@lequor.co.za)
+223432324567`

    await sendEmail(email, emailSubject, emailMessage);
    return Response.success(res,
        {
        message: "payment request",
            data: {
            redirectUrl: body.redirectUrl
        }
    });
});

export default pay;