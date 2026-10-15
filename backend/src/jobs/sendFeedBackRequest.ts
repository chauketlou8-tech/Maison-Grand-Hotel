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
            select b.booking_id, b.reservation_id, g.id as guest_id, g.email, g.firstname, g.lastname from bookings b join guests g on b.guest_id = g.id
            where b.booking_status = 'CHECKED_OUT' and b.updated_at <= now() - interval '2 hours'
              and not exists (
                select 1 from notifications n
                where n.reservationid = (
                    select r.id from reservations r where r.reservationid = b.reservation_id
                )
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

            await sendEmail(r.email, subject, message, name);

            await connection.query(`
                insert into notifications (type, title, message, reservationid, guestid)
                values ($1::notificationtype, $2, $3, $4, $5)
            `, ["feedback_request", `Feedback Request — ${name}`, `Feedback request sent to ${name}.`, r.reservation_id, r.guest_id]);
        }
    }

    catch (e) {
        logger.error("feedback request failed: ", { e });
    }
});