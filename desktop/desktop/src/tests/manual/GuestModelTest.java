package tests.manual;

import main.models.Guest;
import utils.types.Address;

import java.util.Calendar;
import java.util.Date;

public class GuestModelTest {
    public static void main(String[] args) {
        Address address = new Address("08", "Woolsack Drive", null, "Cape Town", null, "Western Cape", "South Africa", "7700");
        Guest guest = new Guest(1, "G01-ST-001", "Tlou Elvis", "Chauke", "chauketlou8@gmail.com", "0790433094", address, "0706235542083", new Date(2007, Calendar.JUNE, 23), "South African", new Date());
        System.out.println(guest);
    }
}
