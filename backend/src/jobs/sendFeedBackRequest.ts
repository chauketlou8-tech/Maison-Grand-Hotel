import cron from "node-cron";
import connection from "../config/connection";
import type { Settings } from "../types/settings";
import { logger } from "../config/logger";
import { feedback_url } from "../config/env"
import sendEmail from "../services/email.service";
import getSettings from "../sockets/settingsClient";

cron.schedule("*/15 * * * *", async () => {
    try {
        const settings: Settings | null = getSettings();

        if (!settings) {
            logger.info("feedback request skipped: settings not loaded");
            return;
        }

        if (!settings.notify_feedback_request) {
            return;
        }

        logger.info("running send feedback request...");

        const result = await connection.query(`
            select b.booking_id, r.id as reservation_db_id, g.id as guest_id, g.email, g.firstname, g.lastname
            from bookings b
                     join guests g on b.guest_id = g.id
                     join reservations r on b.reservation_id = r.reservationid
            where b.booking_status = 'CHECKED_OUT'
              and b.updated_at <= now() - interval '2 HOURS'
              and not exists (
                select 1 from notifications n
                where n.reservationid = r.id
                  and n.type = 'feedback_request'
            )
        `);

        for (const r of result.rows) {
            const name = `${r.firstname} ${r.lastname}`;

            const subject = `${settings.hotel_name} — How was your stay?`;
            const message = `Dear ${name},

Thank you for staying with us at ${settings.hotel_name}. We hope you enjoyed your time.

If you have a moment, we would love to hear your thoughts — it helps us do better for every guest who follows.

Leave a review: ${feedback_url}

Kind regards,
${settings.hotel_name}
${settings.hotel_email}
${settings.hotel_phone}`;

            await connection.query(`
                insert into notifications (type, title, message, reservationid, guestid)
                values ($1::notificationtype, $2, $3, $4, $5)
            `, ["feedback_request", `Feedback Request — ${name}`, `Feedback request sent to ${name}.`, r.reservation_db_id, r.guest_id]);

            await sendEmail(r.email, subject, message, name);
        }
    }

    catch (e) {
        logger.error("feedback request failed: ", { e });
    }
});