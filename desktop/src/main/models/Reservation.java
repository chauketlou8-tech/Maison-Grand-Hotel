package main.models;

import java.util.Date;
import utils.enums.ReservationStatus;

/**
 * Represents a guest's request to reserve accommodation at Mason Hotel before the stay takes place.
 *
 * This class represents all reservations including:
 * - Future room reservations
 * - Pending reservations
 * - Confirmed reservations
 * - Cancelled reservations
 *
 * Features:
 * - Records reservation information
 * - Records the requested room type
 * - Tracks reservation dates
 * - Tracks the number of guests
 * - Tracks reservation status
 * - Supports reservation confirmation
 * - Supports reservation cancellation
 * - Helps manage future accommodation availability
 */
public class Reservation {

    private final long id;
    private final Guest guest;
    private final String requestedRoomType;
    private final Date reservationDate;
    private final Date checkInDate;
    private final Date checkOutDate;
    private final long numberOfGuests;
    private final ReservationStatus reservationStatus;
    private final String requests;
    private final String[] selectedPackages;

    /**
     * Constructs a new Reservation object.
     *
     * @param id                 Reservation's unique identifier
     * @param guest              Guest making the reservation
     * @param requestedRoomType  Type of room requested
     * @param reservationDate    Date the reservation was made
     * @param checkInDate        Date the guest will check in
     * @param checkOutDate       Date the guest will check out
     * @param numberOfGuests     Number of guests staying
     * @param reservationStatus  Current status of the reservation
     * @param selectedPackages   Additional packages offered by the hotel
     * @param requests           Additional things guest has requested
     */
    public Reservation(long id, Guest guest, String requestedRoomType, Date reservationDate, Date checkInDate, Date checkOutDate, long numberOfGuests, ReservationStatus reservationStatus, String requests, String[] selectedPackages) {
        this.id = id;
        this.guest = guest;
        this.requestedRoomType = requestedRoomType;
        this.reservationDate = reservationDate;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.numberOfGuests = numberOfGuests;
        this.reservationStatus = reservationStatus;
        this.requests = requests;
        this.selectedPackages = selectedPackages;
    }

    public long getId() {
        return this.id;
    }

    public Guest getGuest() {
        return this.guest;
    }

    public String getRequestedRoomType() {
        return this.requestedRoomType;
    }

    public Date getReservationDate() {
        return this.reservationDate;
    }

    public Date getCheckInDate() {
        return this.checkInDate;
    }

    public Date getCheckOutDate() {
        return this.checkOutDate;
    }

    public long getNumberOfGuests() {
        return this.numberOfGuests;
    }

    public ReservationStatus getReservationStatus() {
        return this.reservationStatus;
    }

    @Override
    public String toString() {
        return "{\n" +
                "  \"id\": " + id + ",\n" +
                "  \"guest\": " + guest + ",\n" +
                "  \"requestedRoomType\": \"" + requestedRoomType + "\",\n" +
                "  \"reservationDate\": \"" + reservationDate + "\",\n" +
                "  \"checkInDate\": \"" + checkInDate + "\",\n" +
                "  \"checkOutDate\": \"" + checkOutDate + "\",\n" +
                "  \"numberOfGuests\": " + numberOfGuests + ",\n" +
                "  \"reservationStatus\": \"" + reservationStatus + "\"\n" +
                "}";
    }
}