package ui.data;

import models.Booking;
import models.Guest;
import models.Reservation;
import models.Room;
import records.Settings;
import utils.DateFormats;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

public record BookingData(Booking booking, Room[] rooms, Guest[] guests, Reservation[] reservations, Settings settings) {

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

    public LocalDate getCheckInDate() {
        return booking.getCheck_in_date() == null ? null : booking.getCheck_in_date().toLocalDate();
    }

    public LocalDate getCheckOutDate() {
        return booking.getCheck_out_date() == null ? null : booking.getCheck_out_date().toLocalDate();
    }

    public String getCheckIn() {
        LocalDate d = getCheckInDate();

        return d == null ? "—" : d.format(DateFormats.of(settings));
    }

    public String getCheckOut() {
        LocalDate d = getCheckOutDate();

        return d == null ? "—" : d.format(DateFormats.of(settings));
    }

    public long getNights() {
        LocalDate in = getCheckInDate();
        LocalDate out = getCheckOutDate();

        if (in == null || out == null) {
            return 0;
        }

        return ChronoUnit.DAYS.between(in, out);
    }

    public long getGuests() {
        return booking.getNumber_of_guests();
    }

    public String getReference() {
        return booking.getBooking_reference() == null ? "—" : booking.getBooking_reference().toString();
    }

    public String getTotal() {
        return "R " + String.format("%,.2f", booking.getTotal_cost());
    }

    public String getPackages() {
        String[] pkgs = booking.getSelected_packages();

        if (pkgs == null || pkgs.length == 0) {
            return "N/A";
        }

        return String.join(", ", pkgs);
    }

    public String getRequests() {
        String req = booking.getRequests();

        if (req == null || req.isBlank()) {
            return "N/A";
        }

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

    public Room getRoom() {
        for (Room r : rooms) {
            if (r.getRoomid() == booking.getRoom_id()) {
                return r;
            }
        }

        return null;
    }

    public Settings getSettings() {
        return settings;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof BookingData b)) {
            return false;
        }

        return Objects.equals(booking.getBooking_id(), b.booking.getBooking_id()) && Objects.equals(booking.getBooking_status(), b.booking.getBooking_status()) && booking.getTotal_cost() == b.booking.getTotal_cost() && booking.getGuest_id() == b.booking.getGuest_id() && booking.getRoom_id() == b.booking.getRoom_id();
    }

    @Override
    public int hashCode() {
        return Objects.hash(booking.getBooking_id(), booking.getBooking_status(), booking.getTotal_cost(), booking.getGuest_id(), booking.getRoom_id());
    }
}