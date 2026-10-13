package data;

import models.Guest;
import models.Reservation;
import models.Room;
import records.Settings;
import records.Payment;
import utils.DateFormats;

import java.time.temporal.ChronoUnit;

public record ReservationsData(Reservation reservation, Room[] rooms, Guest[] guests, Payment[] payments, Settings settings) {

    public String getReservationId() {
        return reservation.getReservationid() == null ? "—" : reservation.getReservationid().toString();
    }

    public String getGuestName() {
        for (Guest g : guests) {
            if (g.getId() == reservation.getGuest()) {
                return g.getFirstname() + " " + g.getLastname();
            }
        }

        return "Unknown";
    }

    public long getRoomId() {
        for (Room r : rooms) {
            if (r.getRoomid() == reservation.getReservedroom()) {
                return r.getRoomid();
            }
        }

        return 0;
    }

    public String getRoomNumber() {
        for (Room r : rooms) {
            if (r.getRoomid() == reservation.getReservedroom()) {
                return String.valueOf(r.getRoomnumber());
            }
        }

        return "—";
    }

    public String getRoomType() {
        for (Room r : rooms) {
            if (r.getRoomid() == reservation.getReservedroom()) {
                return r.getRoomtype();
            }
        }

        return reservation.getRequestedroomtype() == null ? "—" : reservation.getRequestedroomtype();
    }

    public String getRoomRate() {
        for (Room r : rooms) {
            if (r.getRoomid() == reservation.getReservedroom()) {
                return "R " + String.format("%,.2f", r.getPrice());
            }
        }

        return "R 0.00";
    }

    public String getArrival() {
        if (reservation.getCheckindate() == null) {
            return "—";
        }

        return reservation.getCheckindate().toLocalDate().format(DateFormats.of(settings));
    }

    public String getDeparture() {
        if (reservation.getCheckoutdate() == null) {
            return "—";
        }

        return reservation.getCheckoutdate().toLocalDate().format(DateFormats.of(settings));
    }

    public long getNights() {
        if (reservation.getCheckindate() == null || reservation.getCheckoutdate() == null) {
            return 0;
        }

        return ChronoUnit.DAYS.between(reservation.getCheckindate().toLocalDate(), reservation.getCheckoutdate().toLocalDate());
    }

    public String getTotal() {
        double total = 0;

        for (Payment p : payments) {
            if (p.reservation_id() == null) {
                continue;
            }

            if (!p.paymentstatus().equals("completed")) {
                continue;
            }

            if (p.reservation_id().equals(reservation.getReservationid())) {
                total += p.amount();
            }
        }

        return "R " + String.format("%,.2f", total);
    }

    public long getGuests() {
        return reservation.getNumberofguests();
    }

    public String getBookedOn() {
        if (reservation.getCreated_at() == null) {
            return "—";
        }

        return reservation.getCreated_at().toLocalDateTime().toLocalDate().format(DateFormats.of(settings));
    }

    public String getStatus() {
        return reservation.getReservationstatus().name();
    }

    public String getPaymentStatus() {
        for (Payment p : payments) {
            if (p.reservation_id() == null) {
                continue;
            }

            if (p.reservation_id().equals(reservation.getReservationid())) {
                return p.paymentstatus();
            }
        }

        return "pending";
    }

    public long getPaymentId() {
        for (Payment p : payments) {
            if (p.reservation_id() == null) {
                continue;
            }

            if (p.reservation_id().equals(reservation.getReservationid())) {
                return p.id();
            }
        }

        return 0;
    }

    public Payment getPayment() {
        for (Payment p : payments) {
            if (p.reservation_id() == null) {
                continue;
            }

            if (p.reservation_id().equals(reservation.getReservationid())) {
                return p;
            }
        }

        return null;
    }

    public String getGuestEmail() {
        for (Guest g : guests) {
            if (g.getId() == reservation.getGuest()) {
                return g.getEmail();
            }
        }

        return null;
    }

    public long getGuestId() {
        return reservation.getGuest();
    }

    public Settings getSettings() {
        return settings;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof ReservationsData r)) {
            return false;
        }

        return java.util.Objects.equals(reservation.getReservationid(), r.reservation.getReservationid()) && java.util.Objects.equals(reservation.getReservationstatus(), r.reservation.getReservationstatus()) && java.util.Objects.equals(getPaymentStatus(), r.getPaymentStatus()) && java.util.Objects.equals(reservation.getCheckindate(), r.reservation.getCheckindate()) && java.util.Objects.equals(reservation.getCheckoutdate(), r.reservation.getCheckoutdate());
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(reservation.getReservationid(), reservation.getReservationstatus(), getPaymentStatus(), reservation.getCheckindate(), reservation.getCheckoutdate());
    }
}