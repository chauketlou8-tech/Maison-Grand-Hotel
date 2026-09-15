package utils;

import enums.BookingStatus;
import models.Booking;
import models.Guest;
import records.Payment;
import records.Address;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class GuestRegistryData {
    public final Guest guest;
    private final Address address;
    private final Payment[] payments;
    private final Booking[] bookings;

    public GuestRegistryData(Guest guest, Address address, Payment[] payments, Booking[] bookings) {
        this.guest = guest;
        this.address = address;
        this.payments = payments;
        this.bookings = bookings;
    }

    public String getId() {
        return guest.getGuestid();
    }

    public String getFirstname() {
        return guest.getFirstname();
    }

    public String getLastname() {
        return guest.getLastname();
    }

    public String getEmail() {
        return guest.getEmail();
    }

    public String getPhoneNumber() {
        return guest.getPhonenumber();
    }

    public String getCountry() {
        return address == null ? "" : address.country();
    }

    public String getCity() {
        return address == null ? "" : address.city();
    }

    public String getIdNumber() {
        return guest.getIdnumber();
    }

    public int getNumStays() {
        int numStays = 0;

        for (Booking booking : bookings) {
            if (booking.getBooking_status() == BookingStatus.CANCELLED) continue;
            if (booking.getGuest_id() == guest.getId()) {
                numStays++;
            }
        }

        return numStays;
    }

    public float getSpent() {
        float spent = 0;

        for (Payment payment : payments) {
            if (!payment.paymentstatus().equals("completed")) continue;
            if (payment.guest_id() == guest.getId()) {
                spent += (float) payment.amount();
            }
        }

        return spent;
    }

    public String getLastStay() {
        List<Booking> booking = new ArrayList<Booking>();

        for (Booking b : bookings) {
            if (b.getBooking_status() != BookingStatus.CHECKED_OUT) continue;
            if (b.getGuest_id() != guest.getId()) continue;

            booking.add(b);
        }

        if (booking.isEmpty()) {
            return "New";
        }

        booking.sort(Comparator.comparing(Booking::getUpdated_at).reversed());
        Booking last = booking.getFirst();
        Timestamp last_date = last.getUpdated_at();

        return last_date.toLocalDateTime().toLocalDate().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GuestRegistryData g)) return false;
        return java.util.Objects.equals(getId(), g.getId());
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hashCode(getId());
    }
}