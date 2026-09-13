package tasks;

import models.Room;
import utils.Conn;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

public class CaptureRoomHistoryJob {

    public static void run(Supplier<Room[]> roomsSupplier) {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        long now = System.currentTimeMillis();
        long nextMidnight = LocalDate.now(ZoneId.systemDefault()).plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
        long delay = nextMidnight - now;

        scheduler.scheduleAtFixedRate(() -> {
            try {
                captureRoomHistory(roomsSupplier.get());
            }

            catch (Throwable t) {
                t.printStackTrace();
            }
        }, delay, 24, TimeUnit.HOURS);
    }

    private static void captureRoomHistory(Room[] rooms) throws SQLException, IOException {
        System.out.println("Running capture room history job...");

        Connection connection = Conn.getConnection();
        assert connection != null;

        Timestamp midnight = Timestamp.valueOf(LocalDate.now(ZoneOffset.UTC).atStartOfDay());

        try (PreparedStatement ps = connection.prepareStatement("insert into roomhistory (roomid, status_date, status) values (?, ?, ?)")) {

            for (Room room : rooms) {
                ps.setLong(1, room.getRoomid());
                ps.setTimestamp(2, midnight);
                ps.setString(3, room.getRoomstatus().name().toLowerCase());
                ps.addBatch();
            }

            ps.executeBatch();
        }

        System.out.println("Captured " + rooms.length + " rooms.");
    }
}