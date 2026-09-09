package types;

/**
 * Represents a physical address with support for international address formats.
 * Some fields are optional to accommodate different address conventions.
 *
 * Fields:
 * - street: Street name and number
 * - city: City or town name
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
     * @param street     Street name and number
     * @param city       City or town name
     * @param province   State, province, or region
     * @param country    Country name
     * @param postalCode Postal or ZIP code (optional)
     */
    public Address(String street, String city, String province, String country, String postalCode) {
        this.street = street;
        this.city = city;
        this.province = province;
        this.country = country;
        this.postalCode = postalCode;
    }

    public String getStreet() {
        return street;
    }

    public String getCity() {
        return city;
    }

    public String getProvince() {
        return province;
    }

    public String getCountry() {
        return country;
    }

    public String getPostalCode() {
        return postalCode;
    }

    @Override
    public String toString() {
        return "{\n" +
                "  \"street\": \"" + street + "\",\n" +
                "  \"city\": \"" + city + "\",\n" +
                "  \"province\": \"" + province + "\",\n" +
                "  \"country\": \"" + country + "\",\n" +
                "  \"postalCode\": \"" + postalCode + "\"\n" +
                "}";
    }
}