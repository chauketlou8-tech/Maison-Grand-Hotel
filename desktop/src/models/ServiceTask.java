package models;

import java.io.Serializable;
import java.sql.Timestamp;

public class ServiceTask implements Serializable {

    private final int id;
    private final int service_id;
    private final long room_id;
    private final long assigned_to;
    private final String status;
    private final Timestamp scheduled_for;
    private final Timestamp completed_at;
    private final String notes;
    private final Timestamp created_at;
    private final Timestamp updated_at;

    public ServiceTask(
            int id,
            int service_id,
            long room_id,
            long assigned_to,
            String status,
            Timestamp scheduled_for,
            Timestamp completed_at,
            String notes,
            Timestamp created_at,
            Timestamp updated_at
    ) {
        this.id = id;
        this.service_id = service_id;
        this.room_id = room_id;
        this.assigned_to = assigned_to;
        this.status = status;
        this.scheduled_for = scheduled_for;
        this.completed_at = completed_at;
        this.notes = notes;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public int getId() {
        return this.id;
    }

    public int getService_id() {
        return this.service_id;
    }

    public long getRoom_id() {
        return this.room_id;
    }

    public long getAssigned_to() {
        return this.assigned_to;
    }

    public String getStatus() {
        return this.status;
    }

    public Timestamp getScheduled_for() {
        return this.scheduled_for;
    }

    public Timestamp getCompleted_at() {
        return this.completed_at;
    }

    public String getNotes() {
        return this.notes;
    }

    public Timestamp getCreated_at() {
        return this.created_at;
    }

    public Timestamp getUpdated_at() {
        return this.updated_at;
    }

    @Override
    public String toString() {
        return "{\n" +
                "  \"id\": " + id + ",\n" +
                "  \"service_id\": " + service_id + ",\n" +
                "  \"room_id\": " + room_id + ",\n" +
                "  \"assigned_to\": " + assigned_to + ",\n" +
                "  \"status\": \"" + status + "\",\n" +
                "  \"scheduled_for\": \"" + scheduled_for + "\",\n" +
                "  \"completed_at\": \"" + completed_at + "\",\n" +
                "  \"notes\": \"" + notes + "\",\n" +
                "  \"created_at\": \"" + created_at + "\",\n" +
                "  \"updated_at\": \"" + updated_at + "\"\n" +
                "}";
    }
}