package utils;

import models.Guest;
import models.Reservation;
import models.Room;
import records.Payment;

import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class ReservationsData {
    private final Reservation reservation;
    private final Guest[] guests;
    private final Room[] rooms;
    private final Payment[] payments;

    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public ReservationsData(Reservation reservation, Room[] rooms, Guest[] guests, Payment[] payments) {
        this.reservation = reservation;
        this.rooms = rooms;
        this.guests = guests;
        this.payments = payments;
    }

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
        if (reservation.getReservedroom() <= 0) return "—";
        for (Room r : rooms) {
            if (r.getRoomid() == reservation.getReservedroom()) {
                return String.valueOf(r.getRoomnumber());
            }
        }
        return "—";
    }

    public String getRoomType() {
        if (reservation.getReservedroom() <= 0) return reservation.getRequestedroomtype() == null ? "—" : reservation.getRequestedroomtype();
        for (Room r : rooms) {
            if (r.getRoomid() == reservation.getReservedroom()) {
                return r.getRoomtype();
            }
        }
        return reservation.getRequestedroomtype() == null ? "—" : reservation.getRequestedroomtype();
    }

    public String getRoomRate() {
        if (reservation.getReservedroom() <= 0) return "R 0.00";
        for (Room r : rooms) {
            if (r.getRoomid() == reservation.getReservedroom()) {
                return "R " + String.format("%,.2f", r.getPrice());
            }
        }
        return "R 0.00";
    }

    public String getArrival() {
        if (reservation.getCheckindate() == null) return "—";
        return reservation.getCheckindate().toLocalDate().format(DATE_FMT);
    }

    public String getDeparture() {
        if (reservation.getCheckoutdate() == null) return "—";
        return reservation.getCheckoutdate().toLocalDate().format(DATE_FMT);
    }

    public long getNights() {
        if (reservation.getCheckindate() == null || reservation.getCheckoutdate() == null) return 0;
        return ChronoUnit.DAYS.between(
                reservation.getCheckindate().toLocalDate(),
                reservation.getCheckoutdate().toLocalDate()
        );
    }

    public String getTotal() {
        double total = 0;
        for (Payment p : payments) {
            if (p.reservation_id() == null) continue;
            if (!p.paymentstatus().equals("completed")) continue;

            if (p.reservation_id().equals(reservation.getReservationid())) {
                total += p.amount();
            }
        }
        return "R " + String.format("%,.2f", total);
    }

    public int getGuests() {
        return (int) reservation.getNumberofguests();
    }

    public String getBookedOn() {
        if (reservation.getCreated_at() == null) return "—";
        return reservation.getCreated_at().toLocalDateTime().toLocalDate().format(DATE_FMT);
    }

    public String getStatus() {
        return reservation.getReservationstatus().name();
    }

    public String getPaymentStatus() {
        for (Payment p : payments) {
            if (p.reservation_id() == null) continue;
            if (p.reservation_id().equals(reservation.getReservationid())) {
                return p.paymentstatus();
            }
        }
        return "pending";
    }

    public long getPaymentId() {
        for (Payment p : payments) {
            if (p.reservation_id() == null) continue;
            if (p.reservation_id().equals(reservation.getReservationid())) {
                return p.id();
            }
        }
        return 0;
    }

    public Reservation getReservation() {
        return reservation;
    }

    public Payment getPayment() {
        for (Payment p : payments) {
            if (p.reservation_id() == null) continue;
            if (p.reservation_id().equals(reservation.getReservationid())) return p;
        }
        return null;
    }

    public String getGuestEmail() {
        for (Guest g : guests) {
            if (g.getId() == reservation.getGuest()) return g.getEmail();
        }
        return null;
    }

    public long getGuestId() {
        return reservation.getGuest();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ReservationsData)) return false;
        ReservationsData r = (ReservationsData) o;
        return java.util.Objects.equals(reservation.getReservationid(), r.reservation.getReservationid())
                && java.util.Objects.equals(reservation.getReservationstatus(), r.reservation.getReservationstatus())
                && java.util.Objects.equals(getPaymentStatus(), r.getPaymentStatus())
                && java.util.Objects.equals(reservation.getCheckindate(), r.reservation.getCheckindate())
                && java.util.Objects.equals(reservation.getCheckoutdate(), r.reservation.getCheckoutdate());
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(reservation.getReservationid(), reservation.getReservationstatus(), getPaymentStatus(), reservation.getCheckindate(), reservation.getCheckoutdate());
    }
}