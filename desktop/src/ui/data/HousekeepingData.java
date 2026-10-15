package ui.data;

import models.Service;
import models.ServiceTask;
import records.Settings;
import utils.DateFormats;

import java.sql.Timestamp;
import java.util.Map;
import java.util.Objects;

public class HousekeepingData {

    public static String latestCleaningDate(long roomId, ServiceTask[] serviceTasks, Map<Integer, Service> servicesById, Settings settings) {
        Timestamp latest = null;

        for (ServiceTask st : serviceTasks) {
            if (st.getRoom_id() != roomId) {
                continue;
            }

            if (!"completed".equalsIgnoreCase(st.getStatus())) {
                continue;
            }

            Service s = servicesById.get(st.getService_id());

            if (s == null || !"Cleaning".equalsIgnoreCase(s.getName())) {
                continue;
            }

            Timestamp t = st.getCompleted_at();
            if (t == null) {
                continue;
            }

            if (latest == null || t.after(latest)) {
                latest = t;
            }
        }

        if (latest == null) {
            return "N/A";
        }

        return latest.toLocalDateTime().toLocalDate().format(DateFormats.of(settings));
    }

    public record CleaningRoom(int taskId, int roomNumber, String roomType, int floor, String description, String lastCleaned) {

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }

            if (!(o instanceof CleaningRoom(
                    int taskId1, int number, String type, int floor1, String description1, String cleaned
            ))) {
                return false;
            }

            return Objects.equals(roomType, type) && Objects.equals(description, description1) && Objects.equals(lastCleaned, cleaned) && taskId == taskId1 && roomNumber == number && floor == floor1;
        }

    }

    public record DailyTask(int id, boolean done, String task, String assignedTo, String notes, Timestamp scheduled_for) {

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }

            if (!(o instanceof DailyTask(int id1, boolean done1, String task1, String assignedTo1, String notes1, Timestamp scheduled_for1))) {
                return false;
            }

            return Objects.equals(task, task1) && Objects.equals(assignedTo, assignedTo1) && Objects.equals(notes, notes1) && id == id1 && done == done1 && Objects.equals(scheduled_for, scheduled_for1);
        }

    }

    public record MaintenanceRoom(int roomNumber, String roomType, int floor, String description, String lastCleaned) {

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }

            if (!(o instanceof MaintenanceRoom(
                    int number, String type, int floor1, String description1, String cleaned
            ))) {
                return false;
            }

            return Objects.equals(roomType, type) && Objects.equals(description, description1) && Objects.equals(lastCleaned, cleaned) && roomNumber == number && floor == floor1;
        }

    }
}