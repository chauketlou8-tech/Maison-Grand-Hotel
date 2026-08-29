import asyncHandler from "../middleware/asyncHandler";
import connection from "../config/connection";
import type { req, res } from "../types/express";
import { yoco_secret } from "../config/env";
import Response from "../utils/response";
import type { Payment } from "../types/payment";
import type { Notification } from "../types/notification";

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
            "amount": amount,
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

    return Response.success(res,
        {
        message: "payment request",
            data: {
            redirectUrl: body.redirectUrl
        }
    });
});

export default pay;