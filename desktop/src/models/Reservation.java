package models;

import java.util.Date;
import enums.ReservationStatus;

public class Reservation {

    private final long id;
    private final Guest guest;
    private final String requestedroomtype;
    private final Date reservationdate;
    private final Date checkindate;
    private final Date checkoutdate;
    private final long numberofguests;
    private final ReservationStatus reservationstatus;
    private final String[] selectedpackages;
    private final String requests;
    private final Room reservedroom;

    public Reservation(long id, Guest guest, String requestedroomtype, Date reservationdate, Date checkindate, Date checkoutdate, long numberofguests, ReservationStatus reservationstatus, String[] selectedpackages, String requests, Room reservedroom) {
        this.id = id;
        this.guest = guest;
        this.requestedroomtype = requestedroomtype;
        this.reservationdate = reservationdate;
        this.checkindate = checkindate;
        this.checkoutdate = checkoutdate;
        this.numberofguests = numberofguests;
        this.reservationstatus = reservationstatus;
        this.selectedpackages = selectedpackages;
        this.requests = requests;
        this.reservedroom = reservedroom;
    }

    public long getId() {
        return this.id;
    }

    public Guest getGuest() {
        return this.guest;
    }

    public String getRequestedroomtype() {
        return this.requestedroomtype;
    }

    public Date getReservationdate() {
        return this.reservationdate;
    }

    public Date getCheckindate() {
        return this.checkindate;
    }

    public Date getCheckoutdate() {
        return this.checkoutdate;
    }

    public long getNumberofguests() {
        return this.numberofguests;
    }

    public ReservationStatus getReservationstatus() {
        return this.reservationstatus;
    }

    public String[] getSelectedpackages() {
        return this.selectedpackages;
    }

    public String getRequests() {
        return this.requests;
    }

    public Room getReservedroom() {
        return this.reservedroom;
    }

    @Override
    public String toString() {
        return "{\n" +
                "  \"id\": " + id + ",\n" +
                "  \"guest\": " + guest + ",\n" +
                "  \"requestedroomtype\": \"" + requestedroomtype + "\",\n" +
                "  \"reservationdate\": \"" + reservationdate + "\",\n" +
                "  \"checkindate\": \"" + checkindate + "\",\n" +
                "  \"checkoutdate\": \"" + checkoutdate + "\",\n" +
                "  \"numberofguests\": " + numberofguests + ",\n" +
                "  \"reservationstatus\": \"" + reservationstatus + "\",\n" +
                "  \"selectedpackages\": " + java.util.Arrays.toString(selectedpackages) + ",\n" +
                "  \"requests\": \"" + requests + "\",\n" +
                "  \"reservedroom\": " + reservedroom + "\n" +
                "}";
    }
}