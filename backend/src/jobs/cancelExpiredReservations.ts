import cron from "node-cron";
import connection from "../config/connection";
import type { Notification } from "../types/notification";
import { logger } from "../config/logger";
import sendEmail from "../services/email.service";
import { base_url } from "../config/env"

cron.schedule("* * * * *", async () => {
    try {
        logger.info("cron schedule running...");

        const cancelledReservations = await connection.query(`
        update reservations set reservationstatus = 'CANCELLED'
        where reservationstatus = 'PENDING' and created_at <= NOW() - INTERVAL '30 minutes'
        returning id, guest_id, room_id, reservationid, checkindate, checkoutdate, numberofguests, requestedroomtype
    `);

        for (const reservation of cancelledReservations.rows) {

            await connection.query(`
            update rooms set roomstatus = 'available' where roomid = $1
        `, [reservation.room_id]);

            const guestResults = await connection.query(`
            select firstname, lastname, id, email from guests where id = $1
        `, [reservation.guest_id]);

            const guest = guestResults.rows[0];

            if (!guest) {
                continue;
            }

            const checkIn = new Date(reservation.checkindate).toLocaleString("en-ZA", { day: "2-digit",month: "long",year: "numeric" });
            const checkOut = new Date(reservation.checkoutdate).toLocaleString("en-ZA", { day: "2-digit",month: "long",year: "numeric" });

            const name = `${guest.firstname} ${guest.lastname}`;

            const notification: Notification = {
                type: "reservation_cancelled",
                title: "Reservation Cancellation notification",
                message: `Reservation for ${name} cancelled due to non payment`,
                guest_id: guest.id,
                status: "unread",
                created_at: new Date(),
                updated_at: new Date()
            };

            await connection.query(`
            insert into notifications (type, title, message, guestid)
            values ($1, $2, $3, $4)
        `, [notification.type, notification.title, notification.message, notification.guest_id]);

            const emailSubject = "Mason Hotel — Reservation Cancelled";

            const emailMessage = `Dear ${name}

We regret to inform you that your reservation at Mason Hotel has been cancelled because the required payment was not received within the allocated payment period.

RESERVATION DETAILS
─────────────────
Reservation ID: ${reservation.reservationid}
Check-in: ${checkIn}
Check-out: ${checkOut}
Guests: ${reservation.numberofguests}
Room: ${reservation.requestedroomtype}

PAYMENT STATUS
─────────────────
Payment Status: Not Received
Reservation Status: Cancelled

As payment was not completed within the allocated period, the reservation has been automatically cancelled and the room has been released for other guests.

WHAT TO DO NEXT
─────────────────
If you believe this cancellation was made in error or you would still like to stay with us, you may make a new reservation through our booking system:
${base_url}/booking

If you have any questions regarding this cancellation, please contact us and provide your Reservation ID.

We hope to welcome you to Mason Hotel in the future.

Kind regards,

Mason Hotel
Guest Services
stay@maisongrandhotel.co.za
+27 21 987 6543
08 Greenwood Street, Gardens, Cape Town, 7700
www.maisongrandhotel.co.za`;

            await sendEmail(guest.email, emailSubject, emailMessage, name);
        }
    }

    catch (e) {
        logger.error("error: ", { e });
    }
});