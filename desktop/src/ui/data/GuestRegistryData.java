package ui.data;

import enums.BookingStatus;
import models.Booking;
import models.Guest;
import records.Settings;
import records.Payment;
import records.Address;
import utils.DateFormats;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public record GuestRegistryData(Guest guest, Address address, Payment[] payments, Booking[] bookings, Settings settings) {

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

    public long getAddressId() {
        return address.id();
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
            if (booking.getBooking_status() == BookingStatus.CANCELLED) {
                continue;
            }

            if (booking.getGuest_id() == guest.getId()) {
                numStays++;
            }
        }

        return numStays;
    }

    public float getSpent() {
        float spent = 0;

        for (Payment payment : payments) {
            if (!payment.paymentstatus().equals("completed")) {
                continue;
            }

            if (payment.guest_id() == guest.getId()) {
                spent += (float) payment.amount();
            }
        }

        return spent;
    }

    public String getLastStay() {
        List<Booking> booking = new ArrayList<>();

        for (Booking b : bookings) {
            if (b.getBooking_status() != BookingStatus.CHECKED_OUT) {
                continue;
            }

            if (b.getGuest_id() != guest.getId()) {
                continue;
            }

            booking.add(b);
        }

        if (booking.isEmpty()) {
            return "New";
        }

        booking.sort(Comparator.comparing(Booking::getUpdated_at).reversed());
        Booking last = booking.getFirst();
        Timestamp last_date = last.getUpdated_at();

        return last_date.toLocalDateTime().toLocalDate().format(DateFormats.of(settings));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof GuestRegistryData g)) {
            return false;
        }

        return Objects.equals(getId(), g.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }
}