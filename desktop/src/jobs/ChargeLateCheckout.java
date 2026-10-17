package jobs;

import enums.NotificationType;
import records.Settings;
import utils.Conn;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ChargeLateCheckout {

    private static Connection conn;

    public static void run(Settings settings) {
        try {
            conn = Conn.getConnection();
        }

        catch (Exception e) {
            e.printStackTrace();
            return;
        }

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        scheduler.scheduleAtFixedRate(() -> {
            try {
                chargeLateCheckout(settings);
            }

            catch (SQLException e) {
                e.printStackTrace();
            }
        }, 0, 5, TimeUnit.MINUTES);
    }

    private static void chargeLateCheckout(Settings settings) throws SQLException {
        System.out.println("Charging Late Checkout...");
        LocalTime standardCheckout = settings.checkOutTime().toLocalTime();
        LocalTime lateCheckout = settings.lateCheckOutTime() == null ? null : settings.lateCheckOutTime().toLocalTime();

        LocalDateTime now = LocalDateTime.now(ZoneId.systemDefault());

        try (PreparedStatement ps = conn.prepareStatement("""
            select b.requests, b.check_out_date, r.reservationid as reservation_uuid, r.room_id, g.firstname, g.lastname, rm.roomnumber, rm.price from bookings b
            join reservations r on b.reservation_id = r.reservationid
            join guests g on b.guest_id = g.id
            join rooms rm on r.room_id = rm.roomid
            where b.booking_status = 'CHECKED_IN'
        """)) {

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    UUID reservationUuid = (UUID) rs.getObject("reservation_uuid");
                    String requests = rs.getString("requests") == null | rs.getString("requests").isEmpty() ? "" : rs.getString("requests").toLowerCase();
                    java.sql.Date checkOutDate = rs.getDate("check_out_date");
                    String name = rs.getString("firstname") + " " + rs.getString("lastname");
                    int roomNumber = rs.getInt("roomnumber");
                    BigDecimal price = rs.getBigDecimal("price");

                    if (checkOutDate == null) continue;

                    boolean requestedLate = requests.contains("late check-out") || requests.contains("late check out") || requests.contains("late checkout");

                    LocalTime baseline = (requestedLate && lateCheckout != null) ? lateCheckout : standardCheckout;

                    LocalDateTime dueAt = checkOutDate.toLocalDate().atTime(baseline);
                    LocalDateTime graceEnd = dueAt.plusMinutes(settings.graceMinutes());
                    LocalDateTime fullNightAt = dueAt.plusHours(settings.fullNightHours());
                    LocalDateTime escalateAt = dueAt.plusHours(settings.escalateHours());

                    if (now.isBefore(graceEnd)) continue;

                    BigDecimal half = price.divide(BigDecimal.valueOf(2), 2, RoundingMode.HALF_UP);
                    BigDecimal full = price.setScale(2, RoundingMode.HALF_UP);

                    BigDecimal currentDue = null;

                    try (PreparedStatement q = conn.prepareStatement("select due from payments where reservation_id = ?::uuid and paymentstatus = 'completed' limit 1")) {
                        q.setString(1, reservationUuid.toString());

                        try (ResultSet pr = q.executeQuery()) {
                            if (pr.next()) currentDue = pr.getBigDecimal("due");
                        }
                    }

                    if (currentDue == null) continue;

                    boolean isZero = currentDue.compareTo(BigDecimal.ZERO) == 0;
                    boolean isHalf = currentDue.compareTo(half) == 0;

                    if (now.isBefore(fullNightAt)) {
                        if (isZero) {
                            setDue(reservationUuid, half);
                        }
                    }

                    else if (now.isBefore(escalateAt)) {
                        if (isZero) {
                            setDue(reservationUuid, half);
                        }

                        else if (isHalf) {
                            setDue(reservationUuid, full);
                        }
                    }

                    else {
                        if (isZero) {
                            setDue(reservationUuid, full);
                        }

                        else if (isHalf) {
                            setDue(reservationUuid, full);
                        }

                        notifyOverdue(name, roomNumber, now, dueAt);
                    }
                }
            }
        }
    }

    private static void setDue(UUID reservationUuid, BigDecimal amount) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("update payments set due = ?, updated_at = now() where reservation_id = ?::uuid and paymentstatus = 'completed'")) {
            ps.setBigDecimal(1, amount);
            ps.setString(2, reservationUuid.toString());
            ps.executeUpdate();
        }
    }

    private static void notifyOverdue(String name, int roomNumber, LocalDateTime now, LocalDateTime dueAt) throws SQLException {
        long minutes = java.time.Duration.between(dueAt, now).toMinutes();
        long hours = minutes / 60;
        long mins = minutes % 60;

        String overdue = hours > 0 ? hours + "h " + mins + "m" : mins + "m";

        try (PreparedStatement ps = conn.prepareStatement("insert into notifications (type, title, message) values (?::notificationtype, ?, ?)")) {
            ps.setString(1, NotificationType.late_checkout.name());
            ps.setString(2, "Late Checkout — " + name);
            ps.setString(3, name + " in room " + roomNumber + " is " + overdue + " overdue.");
            ps.executeUpdate();
        }
    }
}