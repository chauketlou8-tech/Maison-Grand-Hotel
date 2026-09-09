import org.junit.Test;
import models.Booking;
import models.Guest;
import models.Room;
import enums.BookingStatus;
import enums.RoomStatus;
import types.Address;

import java.util.Date;

import static org.junit.Assert.assertEquals;

public class BookingModelTest {
    @Test
    public void testBookingModel() {
        Address address = new Address("08", "Woolsack Drive", null, "Cape Town", null, "Western Cape", "South Africa", "7700");
        Guest guest = new Guest(1, "G001", "Tlou Elvis", "Chauke", "chauketlou8@gmail.com", "0790433094", address, "123456789", new Date(2007, 5, 23), "South African", new Date(2026, 7, 7));
        Room room = new Room(1, 101, 1, "Standard", 1500.00f, 2, RoomStatus.AVAILABLE, "Standard room with queen bed and sea view");
        Booking booking = new Booking("B001", guest, room, new Date(2026, 7, 1), new Date(2026, 7, 10), new Date(2026, 7, 15), 2, BookingStatus.CONFIRMED, 7500.00f, new String[]{""}, "");

        assertEquals("B001", booking.getBookingId());
        assertEquals(guest, booking.getGuest());
        assertEquals(room, booking.getRoom());
        assertEquals(2, booking.getNumberOfGuests());
        assertEquals(BookingStatus.CONFIRMED, booking.getBookingStatus());
        assertEquals(7500.00f, booking.getTotalCost(), 0.01);
    }
}