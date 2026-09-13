package models;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.UUID;

import enums.BookingStatus;

public class Booking implements Serializable {

    private final String booking_id;
    private final UUID reservation_id;
    private final long guest_id;
    private final long room_id;
    private final Date booking_date;
    private final Date check_in_date;
    private final Date check_out_date;
    private final long number_of_guests;
    private final BookingStatus booking_status;
    private final double total_cost;
    private final String[] selected_packages;
    private final String requests;
    private final Timestamp created_at;
    private final Timestamp updated_at;

    public Booking(
            String booking_id,
            UUID reservation_id,
            long guest_id,
            long room_id,
            Date booking_date,
            Date check_in_date,
            Date check_out_date,
            long number_of_guests,
            BookingStatus booking_status,
            double total_cost,
            String[] selected_packages,
            String requests,
            Timestamp created_at,
            Timestamp updated_at
    ) {
        this.booking_id = booking_id;
        this.reservation_id = reservation_id;
        this.guest_id = guest_id;
        this.room_id = room_id;
        this.booking_date = booking_date;
        this.check_in_date = check_in_date;
        this.check_out_date = check_out_date;
        this.number_of_guests = number_of_guests;
        this.booking_status = booking_status;
        this.total_cost = total_cost;
        this.selected_packages = selected_packages;
        this.requests = requests;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public String getBooking_id() {
        return this.booking_id;
    }

    public UUID getReservation_id() {
        return this.reservation_id;
    }

    public long getGuest_id() {
        return this.guest_id;
    }

    public long getRoom_id() {
        return this.room_id;
    }

    public Date getBooking_date() {
        return this.booking_date;
    }

    public Date getCheck_in_date() {
        return this.check_in_date;
    }

    public Date getCheck_out_date() {
        return this.check_out_date;
    }

    public long getNumber_of_guests() {
        return this.number_of_guests;
    }

    public BookingStatus getBooking_status() {
        return this.booking_status;
    }

    public double getTotal_cost() {
        return this.total_cost;
    }

    public String[] getSelected_packages() {
        return this.selected_packages;
    }

    public String getRequests() {
        return this.requests;
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
                "  \"booking_id\": \"" + booking_id + "\",\n" +
                "  \"reservation_id\": \"" + reservation_id + "\",\n" +
                "  \"guest_id\": " + guest_id + ",\n" +
                "  \"room_id\": " + room_id + ",\n" +
                "  \"booking_date\": \"" + booking_date + "\",\n" +
                "  \"check_in_date\": \"" + check_in_date + "\",\n" +
                "  \"check_out_date\": \"" + check_out_date + "\",\n" +
                "  \"number_of_guests\": " + number_of_guests + ",\n" +
                "  \"booking_status\": \"" + booking_status + "\",\n" +
                "  \"total_cost\": " + total_cost + "\n" +
                "}";
    }
}