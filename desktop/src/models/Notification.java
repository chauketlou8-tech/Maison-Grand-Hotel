package models;

import java.io.Serializable;
import java.sql.Timestamp;

import enums.NotificationStatus;
import enums.NotificationType;

public class Notification implements Serializable {

    private final long notificationid;
    private final NotificationType type;
    private final String title;
    private final String message;
    private final Long payment_id;
    private final Long reservation_id;
    private final Long guest_id;
    private final NotificationStatus status;
    private final Timestamp created_at;
    private final Timestamp updated_at;

    public Notification(
            long notificationid,
            NotificationType type,
            String title,
            String message,
            Long payment_id,
            Long reservation_id,
            Long guest_id,
            NotificationStatus status,
            Timestamp created_at,
            Timestamp updated_at
    ) {
        this.notificationid = notificationid;
        this.type = type;
        this.title = title;
        this.message = message;
        this.payment_id = payment_id;
        this.reservation_id = reservation_id;
        this.guest_id = guest_id;
        this.status = status;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public long getNotificationid() {
        return this.notificationid;
    }

    public NotificationType getType() {
        return this.type;
    }

    public String getTitle() {
        return this.title;
    }

    public String getMessage() {
        return this.message;
    }

    public Long getPayment_id() {
        return this.payment_id;
    }

    public Long getReservation_id() {
        return this.reservation_id;
    }

    public Long getGuest_id() {
        return this.guest_id;
    }

    public NotificationStatus getStatus() {
        return this.status;
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
                "  \"notificationid\": " + notificationid + ",\n" +
                "  \"type\": \"" + type + "\",\n" +
                "  \"title\": \"" + title + "\",\n" +
                "  \"message\": \"" + message + "\",\n" +
                "  \"payment_id\": " + payment_id + ",\n" +
                "  \"reservation_id\": " + reservation_id + ",\n" +
                "  \"guest_id\": " + guest_id + ",\n" +
                "  \"status\": \"" + status + "\",\n" +
                "  \"created_at\": \"" + created_at + "\",\n" +
                "  \"updated_at\": \"" + updated_at + "\"\n" +
                "}";
    }
}