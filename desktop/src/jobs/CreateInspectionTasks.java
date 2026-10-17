package jobs;

import enums.BookingStatus;
import records.Settings;
import utils.Assignment;
import utils.Conn;

import java.io.IOException;
import java.sql.*;
import java.time.*;
import java.time.temporal.TemporalAdjusters;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class CreateInspectionTasks {

    private static final Connection connection;

    static {
        try {
            connection = Conn.getConnection();
        }

        catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void run(Settings settings) {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        LocalDateTime now = LocalDateTime.now();

        LocalDateTime nextSunday = now.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY)).withHour(8).withMinute(0).withSecond(0).withNano(0);

        if (nextSunday.isBefore(now)) {
            nextSunday = nextSunday.plusWeeks(1);
        }

        long weeklyDelay = nextSunday.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() - System.currentTimeMillis();

        LocalTime checkInTime = settings.checkInTime().toLocalTime();

        LocalDateTime nextBeforeCheckIn = LocalDateTime.of(LocalDate.now(), checkInTime).minusHours(2);

        if (nextBeforeCheckIn.isBefore(now)) {
            nextBeforeCheckIn = LocalDateTime.of(LocalDate.now().plusDays(1), checkInTime).minusHours(2);
        }

        long beforeCheckInDelay = nextBeforeCheckIn.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() - System.currentTimeMillis();

        scheduler.scheduleAtFixedRate(() -> {
            try {
                createWeeklyInspectionTasks();
            }

            catch (SQLException e) {
                e.printStackTrace();
            }
        }, weeklyDelay, 168, TimeUnit.HOURS);

        scheduler.scheduleAtFixedRate(() -> {
            try {
                createBeforeCheckInInspectionTasks();
            }

            catch (SQLException e) {
                e.printStackTrace();
            }
        }, beforeCheckInDelay, 24, TimeUnit.HOURS);
    }

    private static void createWeeklyInspectionTasks() throws SQLException {
        System.out.println("Creating weekly inspection jobs...");

        Timestamp scheduledFor = Timestamp.valueOf(LocalDateTime.now(ZoneOffset.UTC));

        try (PreparedStatement roomsStmt = connection.prepareStatement(
                "select r.roomid, r.floornumber from rooms r where r.roomstatus not in ('maintenance', 'closed') " +
                        "and not exists (select 1 from servicetasks st where st.service_id = 2 and st.room_id = r.roomid and st.scheduled_for::date = ?::date)")) {

            roomsStmt.setTimestamp(1, scheduledFor);

            ResultSet rs = roomsStmt.executeQuery();

            try (PreparedStatement insert = connection.prepareStatement("insert into servicetasks(service_id, room_id, assigned_to, scheduled_for) values (?, ?, ?, ?)")) {

                int index = 0;

                while (rs.next()) {
                    long roomId = rs.getLong("roomid");
                    index = addTaskToBatch(rs, insert, index, roomId);
                }

                insert.executeBatch();
            }
        }
    }

    private static void createBeforeCheckInInspectionTasks() throws SQLException {
        System.out.println("Checking rooms to inspect before check-in...");

        try (PreparedStatement reservationsStmt = connection.prepareStatement(
                "select r.room_id, rm.floornumber from reservations r join rooms rm on rm.roomid = r.room_id " +
                        "where r.checkindate = ? and not exists (select 1 from servicetasks st where st.service_id = 2 and st.room_id = r.room_id and st.scheduled_for::date = ?::date)" +
                        "and not exists (select 1 from bookings b where b.reservation_id = r.reservationid and b.booking_status = ?::bookingstatus)")) {

            reservationsStmt.setDate(1, java.sql.Date.valueOf(LocalDate.now()));
            reservationsStmt.setDate(2, java.sql.Date.valueOf(LocalDate.now()));
            reservationsStmt.setObject(3, BookingStatus.CHECKED_IN.name(), Types.OTHER);

            ResultSet rs = reservationsStmt.executeQuery();

            try (PreparedStatement insert = connection.prepareStatement("insert into servicetasks(service_id, room_id, assigned_to, scheduled_for) values (?, ?, ?, ?)")) {

                int index = 0;

                while (rs.next()) {
                    long roomId = rs.getLong("room_id");
                    index = addTaskToBatch(rs, insert, index, roomId);
                }

                insert.executeBatch();
            }
        }
    }

    private static int addTaskToBatch(ResultSet rs, PreparedStatement insert, int index, long roomId) throws SQLException {
        int floor = rs.getInt("floornumber");

        insert.setLong(1, 2);
        insert.setLong(2, roomId);
        insert.setObject(3, Assignment.pick("supervisors", index, floor), Types.BIGINT);
        insert.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now(ZoneOffset.UTC)));

        insert.addBatch();
        index++;
        return index;
    }
}