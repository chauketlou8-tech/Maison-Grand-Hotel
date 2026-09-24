package tasks;

import models.Room;
import models.Settings;
import utils.Conn;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.temporal.TemporalAdjusters;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

public class CaptureRoomHistoryJob {

    private static final Map<Long, String> lastKnownStatus = new HashMap<>();
    private static long lastPeriodKey = 0;

    private static Connection conn;

    public static void run(Supplier<Room[]> roomsSupplier, Settings settings) throws IOException, SQLException {
        conn = Conn.getConnection();

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        LocalTime auditTime = settings.getNightAuditTime().toLocalTime();

        long now = System.currentTimeMillis();

        long nextAudit = LocalDate.now(ZoneId.systemDefault()).atTime(auditTime).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();

        if (nextAudit <= now) {
            nextAudit = LocalDate.now(ZoneId.systemDefault()).plusDays(1).atTime(auditTime).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        }

        long delay = nextAudit - now;

        scheduler.scheduleAtFixedRate(() -> {
            try {
                captureRoomHistory(roomsSupplier.get(), settings);
            }

            catch (SQLException e) {
                e.printStackTrace();
            }
        }, delay, 24, TimeUnit.HOURS);

        scheduler.scheduleAtFixedRate(() -> {
            try {
                captureRoomHistoryForToday(roomsSupplier.get(), settings);
            }

            catch (SQLException e) {
                e.printStackTrace();
            }
        }, 60, 60, TimeUnit.MINUTES);
    }

    private static void captureRoomHistory(Room[] rooms, Settings settings) throws SQLException {
        System.out.println("Running capture room history job...");

        OffsetDateTime periodKey = computePeriodKey(settings.getNightAuditFrequency());

        try (PreparedStatement ps = conn.prepareStatement("insert into roomhistory (roomid, status_date, status) values (?, ?, ?) on conflict (roomid, status_date) do update set status = excluded.status, updated_at = now()")) {

            for (Room room : rooms) {
                ps.setLong(1, room.getRoomid());
                ps.setObject(2, periodKey);
                ps.setString(3, room.getRoomstatus().name().toLowerCase());
                ps.addBatch();
            }

            ps.executeBatch();
        }

        System.out.println("Captured " + rooms.length + " rooms.");
    }

    private static void captureRoomHistoryForToday(Room[] rooms, Settings settings) throws SQLException {
        System.out.println("running the frequent room history job...");

        OffsetDateTime periodKey = computePeriodKey(settings.getNightAuditFrequency());

        if (periodKey.toInstant().toEpochMilli() != lastPeriodKey) {
            lastKnownStatus.clear();
            lastPeriodKey = periodKey.toInstant().toEpochMilli();
        }

        try (PreparedStatement ps = conn.prepareStatement("insert into roomhistory (roomid, status_date, status) values (?, ?, ?) on conflict (roomid, status_date) do update set status = excluded.status, updated_at = now()")) {

            int changed = 0;

            for (Room room : rooms) {
                String status = room.getRoomstatus().name().toLowerCase();
                String prev = lastKnownStatus.get(room.getRoomid());

                if (status.equals(prev)) continue;

                lastKnownStatus.put(room.getRoomid(), status);

                ps.setLong(1, room.getRoomid());
                ps.setObject(2, periodKey);
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

    private static OffsetDateTime computePeriodKey(String frequency) {
        LocalDate today = LocalDate.now(ZoneId.systemDefault());

        LocalDate periodStart;

        if ("Weekly".equalsIgnoreCase(frequency)) {
            periodStart = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        }

        else if ("Monthly".equalsIgnoreCase(frequency)) {
            periodStart = today.withDayOfMonth(1);
        }

        else {
            periodStart = today;
        }

        return periodStart.atStartOfDay().atOffset(ZoneOffset.UTC);
    }
}