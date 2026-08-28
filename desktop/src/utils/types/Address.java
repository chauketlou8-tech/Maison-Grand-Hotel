package utils.types;

/**
 * Represents a physical address with support for international address formats.
 * Some fields are optional to accommodate different address conventions.
 *\
 * Fields:
 * - streetNumber: Building or house number (optional in some countries)
 * - streetName: Name of the street or road
 * - apartmentNumber: Apartment, suite, or unit number (optional)
 * - city: City or town name
 * - district: District, neighborhood, or suburb (optional)
 * - province: State, province, or region
 * - country: Country name
 * - postalCode: Postal or ZIP code (optional)
 */
public class Address {

    private final String street;
    private final String city;
    private final String province;
    private final String country;
    private final String postalCode;

    /**
     * Constructs a new Address object with all fields.
     *
     * @param street          Building or house number (optional in some countries)
     * @param city            City or town name
     * @param province        State, province, or region
     * @param country         Country name
     * @param postalCode      Postal or ZIP code (optional)
     */
    public Address(String street, String city, String province, String country, String postalCode) {
        this.street = street;
        this.city = city;
        this.province = province;
        this.country = country;
        this.postalCode = postalCode;
    }

    @Override
    public String toString() {
        return String.format("%s, %s, %s, %s, %s", street, city, province, country, postalCode);
    }
}