package main.models;

import utils.types.Address;

import java.util.Date;

/**
 * Represents customers who stay at Mason Hotel and stores the personal information
 * needed to manage their stays and customer records.
 *
 * This class represents all hotel guests including:
 * - New hotel guests
 * - Returning guests
 * - Individual customers
 * - Customers making reservations
 *
 * Features:
 * - Stores guest personal information
 * - Maintains customer records
 * - Stores guest identification details
 * - Supports guest registration
 * - Supports guest management
 * - Maintains guest history
 */
public class Guest {

    private final long id;
    private final String guestId;
    private final String firstName;
    private final String lastName;
    private final String email;
    private final String phoneNumber;
    private final Address address;
    private final String idNumber;
    private final Date dateOfBirth;
    private final String nationality;
    private final Date registrationDate;

    /**
     * Constructs a new Guest object.
     *
     * @param id              Guest's unique database stored id
     * @param guestId         Guest's unique identifier
     * @param firstName       Guest's first name
     * @param lastName        Guest's last name
     * @param email           Guest's email address
     * @param phoneNumber     Guest's contact phone number
     * @param address         Guest's residential address
     * @param idNumber        Guest's ID or passport number
     * @param dateOfBirth     Guest's date of birth
     * @param nationality     Guest's nationality
     * @param registrationDate Date guest was registered
     */
    public Guest(long id, String guestId, String firstName, String lastName, String email, String phoneNumber, Address address, String idNumber, Date dateOfBirth, String nationality, Date registrationDate) {
        this.id = id;
        this.guestId = guestId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.idNumber = idNumber;
        this.dateOfBirth = dateOfBirth;
        this.nationality = nationality;
        this.registrationDate = registrationDate;
    }

    public long getId() {
        return this.id;
    }

    public String getGuestId() {
        return this.guestId;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public String getEmail() {
        return this.email;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public Address getAddress() {
        return this.address;
    }

    public String getIdNumber() {
        return this.idNumber;
    }

    public Date getDateOfBirth() {
        return this.dateOfBirth;
    }

    public String getNationality() {
        return this.nationality;
    }

    public Date getRegistrationDate() {
        return this.registrationDate;
    }

    @Override
    public String toString() {
        return "{\n" +
                "  \"id\": " + id + ",\n" +
                "  \"guestId\": \"" + guestId + "\",\n" +
                "  \"firstName\": \"" + firstName + "\",\n" +
                "  \"lastName\": \"" + lastName + "\",\n" +
                "  \"email\": \"" + email + "\",\n" +
                "  \"phoneNumber\": \"" + phoneNumber + "\",\n" +
                "  \"address\": " + address + ",\n" +
                "  \"idNumber\": \"" + idNumber + "\",\n" +
                "  \"dateOfBirth\": \"" + dateOfBirth + "\",\n" +
                "  \"nationality\": \"" + nationality + "\",\n" +
                "  \"registrationDate\": \"" + registrationDate + "\"\n" +
                "}";
    }
}