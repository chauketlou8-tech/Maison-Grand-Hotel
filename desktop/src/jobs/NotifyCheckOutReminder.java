package jobs;

import enums.NotificationType;
import records.Settings;
import utils.Conn;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class NotifyCheckOutReminder {

    private static Connection conn;

    public static void run(Settings settings) throws IOException, SQLException {
        conn = Conn.getConnection();

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        scheduler.scheduleAtFixedRate(() -> {
            try {
                notifyReminder(settings);
            }

            catch (SQLException e) {
                e.printStackTrace();
            }
        }, 0, 2, TimeUnit.MINUTES);
    }

    private static void notifyReminder(Settings settings) throws SQLException {
        if (!settings.notifyCheckoutReminder()) return;

        LocalTime checkOutTime = settings.checkOutTime().toLocalTime();
        LocalDateTime now = LocalDateTime.now(ZoneId.systemDefault());
        LocalDateTime windowEnd = now.plusHours(1);

        try (PreparedStatement ps = conn.prepareStatement("""
            select b.booking_id, b.reservation_id, b.guest_id
            from bookings b
            where b.booking_status = 'CHECKED_IN'
              and (b.check_out_date + ?::time) between ? and ?
              and not exists (
                  select 1 from notifications n
                  where n.reservationid = b.reservation_id
                    and n.type = 'checkout_reminder'
              )
        """)) {

            ps.setObject(1, checkOutTime);
            ps.setObject(2, now);
            ps.setObject(3, windowEnd);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    long guestId = rs.getLong("guest_id");
                    long reservationId = rs.getLong("reservation_id");

                    try (PreparedStatement insert = conn.prepareStatement("insert into notifications (type, title, message, reservationid, guestid) values (?::notificationtype, ?, ?, ?, ?)")) {
                        insert.setString(1, NotificationType.reservation_confirmation.name());
                        insert.setString(2, "Check-out Reminder");
                        insert.setString(3, "Check-out is approaching.");
                        insert.setLong(4, reservationId);
                        insert.setLong(5, guestId);
                        insert.execute();
                    }
                }
            }
        }
    }
}