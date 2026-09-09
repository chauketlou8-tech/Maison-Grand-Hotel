package models;

import java.util.Date;
import enums.BookingStatus;

/**
 * Represents a confirmed stay at Mason Hotel and stores the information needed
 * to manage the guest's booking from confirmation through completion.
 *
 * This class represents all hotel bookings including:
 * - Confirmed hotel stays
 * - Upcoming stays
 * - Current stays
 * - Completed stays
 *
 * Features:
 * - Stores booking information
 * - Records which guest made the booking
 * - Records which room was booked
 * - Tracks booking dates
 * - Tracks the number of guests
 * - Tracks booking status
 * - Records the total cost
 * - Supports check-in and check-out management
 */
public class Booking {

    private final String bookingId;
    private final Guest guest;
    private final Room room;
    private final Date bookingDate;
    private final Date checkInDate;
    private final Date checkOutDate;
    private final long numberOfGuests;
    private final BookingStatus bookingStatus;
    private final float totalCost;
    private final String[] selectedPackages;
    private final String requests;

    /**
     * Constructs a new Booking object.
     *
     * @param bookingId      Booking's unique identifier
     * @param guest          Guest who made the booking
     * @param room           Room that was booked
     * @param bookingDate    Date the booking was made
     * @param checkInDate    Date the guest will check in
     * @param checkOutDate   Date the guest will check out
     * @param numberOfGuests Number of guests staying
     * @param bookingStatus  Current status of the booking
     * @param totalCost      Total cost of the booking
     * @param selectedPackages Additional packages offered by the hotel
     * @param requests       Additional things guest has requested
     */
    public Booking(String bookingId, Guest guest, Room room, Date bookingDate, Date checkInDate, Date checkOutDate, long numberOfGuests, BookingStatus bookingStatus, float totalCost, String[] selectedPackages, String requests) {
        this.bookingId = bookingId;
        this.guest = guest;
        this.room = room;
        this.bookingDate = bookingDate;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.numberOfGuests = numberOfGuests;
        this.bookingStatus = bookingStatus;
        this.totalCost = totalCost;
        this.selectedPackages = selectedPackages;
        this.requests = requests;
    }

    public String getBookingId() {
        return this.bookingId;
    }

    public Guest getGuest() {
        return this.guest;
    }

    public Room getRoom() {
        return this.room;
    }

    public Date getBookingDate() {
        return this.bookingDate;
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

    public BookingStatus getBookingStatus() {
        return this.bookingStatus;
    }

    public float getTotalCost() {
        return this.totalCost;
    }

    public String[] getSelectedPackages() {
        return this.selectedPackages;
    }

    public String getRequests() {
        return this.requests;
    }

    @Override
    public String toString() {
        return "{\n" +
                "  \"bookingId\": \"" + bookingId + "\",\n" +
                "  \"guest\": " + guest + ",\n" +
                "  \"room\": " + room + ",\n" +
                "  \"bookingDate\": \"" + bookingDate + "\",\n" +
                "  \"checkInDate\": \"" + checkInDate + "\",\n" +
                "  \"checkOutDate\": \"" + checkOutDate + "\",\n" +
                "  \"numberOfGuests\": " + numberOfGuests + ",\n" +
                "  \"bookingStatus\": \"" + bookingStatus + "\",\n" +
                "  \"totalCost\": " + totalCost + "\n" +
                "}";
    }
}