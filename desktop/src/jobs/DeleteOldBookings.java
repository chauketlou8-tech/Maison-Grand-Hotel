package jobs;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class DeleteOldBookings {

    public static void run() throws SQLException {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        scheduler.scheduleAtFixedRate(DeleteOldBookings::deleteOldBookings, 0, 24, TimeUnit.HOURS);
    }

    private static void deleteOldBookings() {
        System.out.println("Deleting old bookings...");

        try (Connection conn = utils.Conn.getConnection();
             Statement statement = conn.createStatement()) {
            statement.executeUpdate("delete from bookings where created_at < now() - interval '30 days'");
        }

        catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }
}