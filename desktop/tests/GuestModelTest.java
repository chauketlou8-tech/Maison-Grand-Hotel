import models.Guest;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;
import types.Address;

public class GuestModelTest {
    @Test
    public void testGuestModel() {
        Address address = new Address("08", "Woolsack Drive", null, "Cape Town", null, "Western Cape", "South Africa", "7700");
        Guest guest = new Guest(1, "G01-ST-001", "Tlou Elvis", "Chauke", "chauketlou8@gmail.com", "0790433094", address, "0706235542083", new Date(2007, Calendar.JUNE, 23), "South African", new Date());

        assertEquals("Tlou Elvis", guest.getFirstName());
        assertEquals("Chauke", guest.getLastName());
    }
}
