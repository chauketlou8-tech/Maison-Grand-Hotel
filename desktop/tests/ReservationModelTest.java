import org.junit.Test;
import main.models.Reservation;
import main.models.Guest;
import utils.enums.ReservationStatus;
import utils.types.Address;

import java.util.Date;

import static org.junit.Assert.assertEquals;

public class ReservationModelTest {
    @Test
    public void testReservationModel() {
        Address address = new Address("08", "Woolsack Drive", null, "Cape Town", null, "Western Cape", "South Africa", "7700");
        Guest guest = new Guest(1, "G001", "Tlou Elvis", "Chauke", "chauketlou8@gmail.com", "0790433094", address, "123456789", new Date(2007, 5, 23), "South African", new Date(2026, 7, 7));
        Reservation reservation = new Reservation(1, guest, "Standard", new Date(2026, 7, 1), new Date(2026, 7, 10), new Date(2026, 7, 15), 2, ReservationStatus.CONFIRMED, "", new String[]{""});

        assertEquals(1, reservation.getId());
        assertEquals(guest, reservation.getGuest());
        assertEquals("Standard", reservation.getRequestedRoomType());
        assertEquals(2, reservation.getNumberOfGuests());
        assertEquals(ReservationStatus.CONFIRMED, reservation.getReservationStatus());
    }
}