package tasks;

import models.Room;
import utils.Conn;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

public class CaptureRoomHistoryJob {

    private static final Map<Long, String> lastKnownStatus = new HashMap<>();
    private static long lastDayKey = 0;

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

        scheduler.scheduleAtFixedRate(() -> {
            try {
                captureRoomHistoryForToday(roomsSupplier.get());
            }

            catch (Throwable t) {
                t.printStackTrace();
            }
        }, 60, 60, TimeUnit.SECONDS);
    }

    private static void captureRoomHistory(Room[] rooms) throws SQLException, IOException {
        System.out.println("Running capture room history job...");

        Connection connection = Conn.getConnection();
        assert connection != null;

        OffsetDateTime midnight = OffsetDateTime.now(ZoneOffset.UTC).truncatedTo(ChronoUnit.DAYS);

        try (PreparedStatement ps = connection.prepareStatement("insert into roomhistory (roomid, status_date, status) values (?, ?, ?) on conflict (roomid, status_date) do update set status = excluded.status, updated_at = now()")) {

            for (Room room : rooms) {
                ps.setLong(1, room.getRoomid());
                ps.setObject(2, midnight);
                ps.setString(3, room.getRoomstatus().name().toLowerCase());
                ps.addBatch();
            }

            ps.executeBatch();
        }

        System.out.println("Captured " + rooms.length + " rooms.");
    }

    private static void captureRoomHistoryForToday(Room[] rooms) throws SQLException, IOException {
        System.out.println("running the frequent room history job...");
        Connection connection = Conn.getConnection();
        assert connection != null;

        OffsetDateTime todayKey = LocalDate.now(ZoneId.systemDefault()).atStartOfDay().atOffset(ZoneOffset.UTC);

        if (todayKey.toInstant().toEpochMilli() != lastDayKey) {
            lastKnownStatus.clear();
            lastDayKey = todayKey.toInstant().toEpochMilli();
        }

        try (PreparedStatement ps = connection.prepareStatement("insert into roomhistory (roomid, status_date, status) values (?, ?, ?) on conflict (roomid, status_date) do update set status = excluded.status, updated_at = now()")) {

            int changed = 0;

            for (Room room : rooms) {
                String status = room.getRoomstatus().name().toLowerCase();
                String prev = lastKnownStatus.get(room.getRoomid());

                if (status.equals(prev)) continue;

                lastKnownStatus.put(room.getRoomid(), status);

                ps.setLong(1, room.getRoomid());
                ps.setObject(2, todayKey);
                ps.setString(3, status);
                ps.addBatch();
                changed++;
            }

            if (changed > 0) {
                ps.executeBatch();
            }

            System.out.println("Captured " + changed + " of " + rooms.length + " rooms.");
        }
    }
}