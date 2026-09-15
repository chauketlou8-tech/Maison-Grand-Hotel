package utils;

import models.Booking;
import models.Guest;
import models.Reservation;
import models.Room;

import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class BookingData {
    private final Booking booking;
    private final Guest[] guests;
    private final Room[] rooms;
    private final Reservation[] reservations;

    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public BookingData(Booking booking, Room[] rooms, Guest[] guests, Reservation[]  reservations) {
        this.booking = booking;
        this.rooms = rooms;
        this.guests = guests;
        this.reservations = reservations;
    }

    public String getBookingId() {
        return booking.getBooking_id();
    }

    public String getGuestName() {
        for (Guest g : guests) {
            if (g.getId() == booking.getGuest_id()) {
                return g.getFirstname() + " " + g.getLastname();
            }
        }
        return "Unknown";
    }

    public String getRoomNumber() {
        for (Room r : rooms) {
            if (r.getRoomid() == booking.getRoom_id()) {
                return String.valueOf(r.getRoomnumber());
            }
        }
        return "—";
    }

    public String getRoomType() {
        for (Room r : rooms) {
            if (r.getRoomid() == booking.getRoom_id()) {
                return r.getRoomtype();
            }
        }
        return "—";
    }

    public String getRoomRate() {
        for (Room r : rooms) {
            if (r.getRoomid() == booking.getRoom_id()) {
                return "R " + String.format("%,.2f", r.getPrice());
            }
        }
        return "R 0.00";
    }

    public String getCheckIn() {
        if (booking.getCheck_in_date() == null) return "—";
        return booking.getCheck_in_date().toLocalDate().format(DateTimeFormatter.ofPattern("dd/MM/yyy"));
    }

    public String getCheckOut() {
        if (booking.getCheck_out_date() == null) return "—";
        return booking.getCheck_out_date().toLocalDate().format(DateTimeFormatter.ofPattern("dd/MM/yyy"));
    }

    public long getNights() {
        if (booking.getCheck_in_date() == null || booking.getCheck_out_date() == null) return 0;
        return ChronoUnit.DAYS.between(booking.getCheck_in_date().toLocalDate(), booking.getCheck_out_date().toLocalDate());
    }

    public int getGuests() {
        return (int) booking.getNumber_of_guests();
    }

    public String getReference() {
        return booking.getBooking_reference() == null ? "—" : booking.getBooking_reference().toString();
    }

    public String getTotal() {
        return "R " + String.format("%,.2f", booking.getTotal_cost());
    }

    public String getPackages() {
        String[] pkgs = booking.getSelected_packages();
        if (pkgs == null || pkgs.length == 0) return "N/A";
        return String.join(", ", pkgs);
    }

    public String getRequests() {
        String req = booking.getRequests();
        if (req == null || req.isBlank()) return "N/A";
        return req;
    }

    public String getStatus() {
        return booking.getBooking_status().name();
    }

    public Reservation getReservation() {
        for (Reservation reservation : reservations) {
            if (reservation.getReservationid() != null && reservation.getReservationid().equals(booking.getReservation_id())) {
                return reservation;
            }
        }

        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BookingData)) return false;
        BookingData b = (BookingData) o;
        return java.util.Objects.equals(booking.getBooking_id(), b.booking.getBooking_id())
                && java.util.Objects.equals(booking.getBooking_status(), b.booking.getBooking_status())
                && booking.getTotal_cost() == b.booking.getTotal_cost()
                && booking.getGuest_id() == b.booking.getGuest_id()
                && booking.getRoom_id() == b.booking.getRoom_id();
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(
                booking.getBooking_id(),
                booking.getBooking_status(),
                booking.getTotal_cost(),
                booking.getGuest_id(),
                booking.getRoom_id()
        );
    }
}