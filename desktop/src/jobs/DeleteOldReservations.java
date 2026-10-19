package jobs;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class DeleteOldReservations {

    public static void run() throws SQLException {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        scheduler.scheduleAtFixedRate(DeleteOldReservations::deleteOldReservations, 0, 24, TimeUnit.HOURS);
    }

    private static void deleteOldReservations() {
        System.out.println("Deleting old reservations...");

        try (Connection conn = utils.Conn.getConnection();
             Statement statement = conn.createStatement()) {
            statement.executeUpdate("delete from reservations where created_at < now() - interval '30 days'");
        }

        catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }
}