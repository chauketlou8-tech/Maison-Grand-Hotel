package models;

import java.io.Serializable;
import java.util.Arrays;
import java.util.UUID;

import enums.ReservationStatus;

public class Reservation implements Serializable {

    private final long id;
    private final UUID reservation_id;
    private final long guest_id;
    private final long room_id;
    private final String requestedroomtype;
    private final java.sql.Timestamp reservationdate;
    private final java.sql.Date checkindate;
    private final java.sql.Date checkoutdate;
    private final long numberofguests;
    private final ReservationStatus reservationstatus;
    private final String[] selectedpackages;
    private final String requests;
    private final java.sql.Timestamp created_at;
    private final java.sql.Timestamp updated_at;

    public Reservation(
            long id,
            UUID reservation_id,
            long guest_id,
            long room_id,
            String requestedroomtype,
            java.sql.Timestamp reservationdate,
            java.sql.Date checkindate,
            java.sql.Date checkoutdate,
            long numberofguests,
            ReservationStatus reservationstatus,
            String[] selectedpackages,
            String requests,
            java.sql.Timestamp created_at,
            java.sql.Timestamp updated_at
    ) {
        this.id = id;
        this.reservation_id = reservation_id;
        this.guest_id = guest_id;
        this.room_id = room_id;
        this.requestedroomtype = requestedroomtype;
        this.reservationdate = reservationdate;
        this.checkindate = checkindate;
        this.checkoutdate = checkoutdate;
        this.numberofguests = numberofguests;
        this.reservationstatus = reservationstatus;
        this.selectedpackages = selectedpackages;
        this.requests = requests;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public long getId() {
        return this.id;
    }

    public UUID getReservationid() {
        return this.reservation_id;
    }

    public long getGuest() {
        return this.guest_id;
    }

    public long getReservedroom() {
        return this.room_id;
    }

    public String getRequestedroomtype() {
        return this.requestedroomtype;
    }

    public java.sql.Timestamp getReservationdate() {
        return this.reservationdate;
    }

    public java.sql.Date getCheckindate() {
        return this.checkindate;
    }

    public java.sql.Date getCheckoutdate() {
        return this.checkoutdate;
    }

    public long getNumberofguests() {
        return this.numberofguests;
    }

    public ReservationStatus getReservationstatus() {
        return this.reservationstatus;
    }

    public String[] getSelectedpackages() {
        return this.selectedpackages;
    }

    public String getRequests() {
        return this.requests;
    }

    public java.sql.Timestamp getCreated_at() {
        return this.created_at;
    }

    public java.sql.Timestamp getUpdated_at() {
        return this.updated_at;
    }

    @Override
    public String toString() {
        return "{\n" +
                "  \"id\": " + id + ",\n" +
                "  \"reservationId\": \"" + reservation_id + "\",\n" +
                "  \"guest_id\": " + guest_id + ",\n" +
                "  \"room_id\": " + room_id + ",\n" +
                "  \"requestedroomtype\": \"" + requestedroomtype + "\",\n" +
                "  \"reservationdate\": \"" + reservationdate + "\",\n" +
                "  \"checkindate\": \"" + checkindate + "\",\n" +
                "  \"checkoutdate\": \"" + checkoutdate + "\",\n" +
                "  \"numberofguests\": " + numberofguests + ",\n" +
                "  \"reservationstatus\": \"" + reservationstatus + "\",\n" +
                "  \"selectedpackages\": " + Arrays.toString(selectedpackages) + ",\n" +
                "  \"requests\": \"" + requests + "\",\n" +
                "  \"created_at\": \"" + created_at + "\",\n" +
                "  \"updated_at\": \"" + updated_at + "\"\n" +
                "}";
    }
}