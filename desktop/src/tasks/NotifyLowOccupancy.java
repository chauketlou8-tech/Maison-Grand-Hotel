package tasks;

import enums.NotificationType;
import records.Settings;
import utils.Conn;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class NotifyLowOccupancy {

    private static Connection conn;

    public static void run(Settings settings) throws IOException, SQLException {
        conn = Conn.getConnection();

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        scheduler.scheduleAtFixedRate(() -> {
            try {
                checkOccupancy(settings);
            }

            catch (SQLException e) {
                e.printStackTrace();
            }
        }, 6, 6, TimeUnit.HOURS);
    }

    private static void checkOccupancy(Settings settings) throws SQLException {
        if (!settings.notifyStaffLowOccupancy()) return;

        int occupied = 0;
        int total = 0;

        try (PreparedStatement ps = conn.prepareStatement("""
            select
                count(*) filter (where roomstatus = 'occupied') as occupied,
                count(*) filter (where roomstatus not in ('maintenance', 'closed')) as total
            from rooms
        """)) {

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    occupied = rs.getInt("occupied");
                    total = rs.getInt("total");
                }
            }
        }

        if (total == 0) return;

        BigDecimal threshold = settings.occupancyThreshold();

        if (threshold == null) return;

        double occupancy = (double) occupied / total;

        if (BigDecimal.valueOf(occupancy).compareTo(threshold) >= 0) return;

        int pct = (int) Math.round(occupancy * 100);

        try (PreparedStatement insert = conn.prepareStatement("insert into notifications (type, title, message) values (?::notificationtype, ?, ?)")) {
            insert.setString(1, NotificationType.low_occupancy.name());
            insert.setString(2, "Low Occupancy Alert");
            insert.setString(3, pct + "% occupancy (" + occupied + "/" + total + " rooms).");
            insert.execute();
        }
    }
}