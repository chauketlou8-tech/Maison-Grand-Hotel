package models;

import types.Address;
import java.util.Date;

public class Guest {

    private final long id;
    private final String guestid;
    private final String firstname;
    private final String lastname;
    private final String email;
    private final String phonenumber;
    private final Address address;
    private final String idnumber;
    private final Date dateofbirth;
    private final String nationality;
    private final Date registrationdate;

    public Guest(long id, String guestid, String firstname, String lastname, String email, String phonenumber, Address address, String idnumber, Date dateofbirth, String nationality, Date registrationdate) {
        this.id = id;
        this.guestid = guestid;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.phonenumber = phonenumber;
        this.address = address;
        this.idnumber = idnumber;
        this.dateofbirth = dateofbirth;
        this.nationality = nationality;
        this.registrationdate = registrationdate;
    }

    public long getId() {
        return this.id;
    }

    public String getGuestid() {
        return this.guestid;
    }

    public String getFirstname() {
        return this.firstname;
    }

    public String getLastname() {
        return this.lastname;
    }

    public String getEmail() {
        return this.email;
    }

    public String getPhonenumber() {
        return this.phonenumber;
    }

    public Address getAddress() {
        return this.address;
    }

    public String getIdnumber() {
        return this.idnumber;
    }

    public Date getDateofbirth() {
        return this.dateofbirth;
    }

    public String getNationality() {
        return this.nationality;
    }

    public Date getRegistrationdate() {
        return this.registrationdate;
    }

    @Override
    public String toString() {
        return "{\n" +
                "  \"id\": " + id + ",\n" +
                "  \"guestid\": \"" + guestid + "\",\n" +
                "  \"firstname\": \"" + firstname + "\",\n" +
                "  \"lastname\": \"" + lastname + "\",\n" +
                "  \"email\": \"" + email + "\",\n" +
                "  \"phonenumber\": \"" + phonenumber + "\",\n" +
                "  \"address\": " + address + ",\n" +
                "  \"idnumber\": \"" + idnumber + "\",\n" +
                "  \"dateofbirth\": \"" + dateofbirth + "\",\n" +
                "  \"nationality\": \"" + nationality + "\",\n" +
                "  \"registrationdate\": \"" + registrationdate + "\"\n" +
                "}";
    }
}