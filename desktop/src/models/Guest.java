package models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;

public class Guest implements Serializable {

    @SerializedName("id")
    private final long id;

    @SerializedName("guestid")
    private final String guestid;

    @SerializedName("firstname")
    private final String firstname;

    @SerializedName("lastname")
    private final String lastname;

    @SerializedName("email")
    private final String email;

    @SerializedName("phonenumber")
    private final String phonenumber;

    @SerializedName("address_id")
    private final long address_id;

    @SerializedName("idnumber")
    private final String idnumber;

    @SerializedName("dateofbirth")
    private final Date dateofbirth;

    @SerializedName("nationality")
    private final String nationality;

    @SerializedName("registrationdate")
    private final Date registrationdate;

    @SerializedName("created_at")
    private final Timestamp created_at;

    @SerializedName("updated_at")
    private final Timestamp updated_at;

    public Guest(
            long id,
            String guestid,
            String firstname,
            String lastname,
            String email,
            String phonenumber,
            long address_id,
            String idnumber,
            Date dateofbirth,
            String nationality,
            Date registrationdate,
            Timestamp created_at,
            Timestamp updated_at
    ) {
        this.id = id;
        this.guestid = guestid;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.phonenumber = phonenumber;
        this.address_id = address_id;
        this.idnumber = idnumber;
        this.dateofbirth = dateofbirth;
        this.nationality = nationality;
        this.registrationdate = registrationdate;
        this.created_at = created_at;
        this.updated_at = updated_at;
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

    public long getAddress_id() {
        return this.address_id;
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

    public Timestamp getCreated_at() {
        return this.created_at;
    }

    public Timestamp getUpdated_at() {
        return this.updated_at;
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
                "  \"address_id\": " + address_id + ",\n" +
                "  \"idnumber\": \"" + idnumber + "\",\n" +
                "  \"dateofbirth\": \"" + dateofbirth + "\",\n" +
                "  \"nationality\": \"" + nationality + "\",\n" +
                "  \"registrationdate\": \"" + registrationdate + "\",\n" +
                "  \"created_at\": \"" + created_at + "\",\n" +
                "  \"updated_at\": \"" + updated_at + "\"\n" +
                "}";
    }
}