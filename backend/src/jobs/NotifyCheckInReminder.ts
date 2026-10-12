import cron from "node-cron";
import connection from "../config/connection";
import type { Settings } from "../types/settings";
import { logger } from "../config/logger";
import sendEmail from "../services/email.service";
import getSettings from "../sockets/settingsClient";

cron.schedule("*/2 * * * *", async () => {
    try {
        const settings: Settings | null = getSettings();

        if (!settings) {
            logger.info("check-in reminder skipped: settings not loaded");
            return;
        }

        if (!settings.notify_checkin_reminder) {
            return;
        }

        const result = await connection.query(`
            select r.id, r.reservationid, r.requestedroomtype, r.numberofguests, (r.checkoutdate - r.checkindate) as nights,
            g.id as guest_id, g.email, g.firstname, g.lastname from reservations r join guests g on r.guest_id = g.id
            where r.reservationstatus = 'CONFIRMED' and (r.checkindate + $1::time) between now() and now() + interval '12 hours'
            and not exists (select 1 from notifications n where n.reservationid = r.id and n.type = 'checkin_reminder')
        `, [settings.check_in_time]);

        for (const r of result.rows) {
            const name = `${r.firstname} ${r.lastname}`;

            const subject = `${settings.hotel_name} — Check-in Reminder`;
            const message = `Dear ${name},

Your stay at ${settings.hotel_name} begins soon. Check-in is from ${settings.check_in_time}.

We look forward to welcoming you.

Kind regards,
${settings.hotel_name}`;

            await sendEmail(r.email, subject, message, name);

            await connection.query(`
                insert into notifications (type, title, message, reservationid, guestid)
                values ($1::notificationtype, $2, $3, $4, $5)
            `, ["checkin_reminder", `Arrival — ${name}`, `${name} checks in today. Room ${r.requestedroomtype} · ${r.nights} night(s) · ${r.numberofguests} guest(s).`, r.id, r.guest_id]);
        }
    }

    catch (e) {
        logger.error("check-in reminder failed: ", { e });
    }
});