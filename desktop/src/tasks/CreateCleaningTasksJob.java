package tasks;

import models.Settings;
import utils.Conn;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class CreateCleaningTasksJob {

    private static Settings settings;

    private static Connection conn;

    public static void run(Settings settings) throws IOException, SQLException {
        CreateCleaningTasksJob.settings = settings;
        CreateCleaningTasksJob.conn = Conn.getConnection();

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        scheduler.schedule(() -> {
            try {
                createCleaningTasks();
            }

            catch (SQLException e) {
                e.printStackTrace();
            }
        }, 60, TimeUnit.SECONDS);

        LocalTime taskTime = settings.getHousekeepingTaskTime().toLocalTime();

        LocalDateTime next = LocalDateTime.of(LocalDate.now(), taskTime);

        if (next.isBefore(LocalDateTime.now())) {
            next = next.plusDays(1);
        }

        long delay = next.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() - System.currentTimeMillis();

        scheduler.scheduleAtFixedRate(() -> {
            try {
                createCleaningTasks();
            }

            catch (SQLException e) {
                e.printStackTrace();
            }
        }, delay, 24, TimeUnit.HOURS);
    }

    private static void createCleaningTasks() throws SQLException {
        System.out.println("Running create cleaning tasks job...");

        LocalTime taskTime = settings.getHousekeepingTaskTime().toLocalTime();
        String defaultStatus = settings.getHousekeepingDefaultStatus();
        String assignment = settings.getHousekeepingAssignment();

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime target = LocalDateTime.of(now.toLocalDate(), taskTime);

        if (target.isBefore(now)) {
            target = target.plusDays(1);
        }

        Timestamp scheduledFor = Timestamp.valueOf(target);

        Map<Long, Integer> roomFloors = new HashMap<>();
        List<Long> occupiedRoomIds = new ArrayList<>();

        try (PreparedStatement statement = conn.prepareStatement("select r.roomid, r.floornumber from Rooms r where r.roomstatus = 'occupied' and not exists (select 1 from ServiceTasks st where st.service_id = 1 and st.room_id = r.roomid and st.scheduled_for::date = ?::date)")) {

            statement.setTimestamp(1, scheduledFor);
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                long roomId = rs.getLong("roomid");
                occupiedRoomIds.add(roomId);
                roomFloors.put(roomId, rs.getInt("floornumber"));
            }
        }

        if (occupiedRoomIds.isEmpty()) {
            System.out.println("Created 0 cleaning tasks for " + target.toLocalDate() + ".");
            return;
        }

        long[] staffIds = getActiveHousekeepers();

        try (PreparedStatement statement = conn.prepareStatement("insert into ServiceTasks (service_id, room_id, assigned_to, status, scheduled_for) values (1, ?, ?, ?, ?)")) {

            for (int i = 0; i < occupiedRoomIds.size(); i++) {
                long roomId = occupiedRoomIds.get(i);
                Long assignedTo = pickAssignee(assignment, i, roomFloors.get(roomId), staffIds);

                statement.setLong(1, roomId);

                if (assignedTo == null) {
                    statement.setNull(2, Types.BIGINT);
                }

                else {
                    statement.setLong(2, assignedTo);
                }

                statement.setString(3, defaultStatus);
                statement.setTimestamp(4, scheduledFor);
                statement.addBatch();
            }

            statement.executeBatch();

            System.out.println("Created " + occupiedRoomIds.size() + " cleaning tasks for " + target.toLocalDate() + " using " + assignment + ".");
        }
    }

    private static Long pickAssignee(String assignment, int index, int floor, long[] staffIds) {
        if (staffIds.length == 0) {
            return null;
        }

        if ("By floor".equalsIgnoreCase(assignment)) {
            return staffIds[Math.floorMod(floor, staffIds.length)];
        }

        return staffIds[index % staffIds.length];
    }

    private static long[] getActiveHousekeepers() throws SQLException {
        List<Long> ids = new ArrayList<>();

        try (PreparedStatement statement = conn.prepareStatement("select id from employees where department = 'Housekeeping' and employmentstatus = 'ACTIVE' and jobposition in ('Room Attendant', 'Public Area Attendant', 'Temporary Housekeeper') order by id")) {

            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                ids.add(rs.getLong("id"));
            }
        }

        long[] out = new long[ids.size()];

        for (int i = 0; i < ids.size(); i++) {
            out[i] = ids.get(i);
        }

        return out;
    }
}