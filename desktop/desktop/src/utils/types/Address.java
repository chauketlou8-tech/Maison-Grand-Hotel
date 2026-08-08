package utils.types;

/**
 * Represents a physical address with support for international address formats.
 * Some fields are optional to accommodate different address conventions.
 *
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

    private final String streetNumber;
    private final String streetName;
    private final String apartmentNumber;
    private final String city;
    private final String district;
    private final String province;
    private final String country;
    private final String postalCode;

    /**
     * Constructs a new Address object with all fields.
     *
     * @param streetNumber    Building or house number (optional in some countries)
     * @param streetName      Name of the street or road
     * @param apartmentNumber Apartment, suite, or unit number (optional)
     * @param city            City or town name
     * @param district        District, neighborhood, or suburb (optional)
     * @param province        State, province, or region
     * @param country         Country name
     * @param postalCode      Postal or ZIP code (optional)
     */
    public Address(String streetNumber, String streetName, String apartmentNumber, String city, String district, String province, String country, String postalCode) {
        this.streetNumber = streetNumber;
        this.streetName = streetName;
        this.apartmentNumber = apartmentNumber;
        this.city = city;
        this.district = district;
        this.province = province;
        this.country = country;
        this.postalCode = postalCode;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{\n");
        sb.append("  \"streetNumber\": \"").append(streetNumber).append("\",\n");
        sb.append("  \"streetName\": \"").append(streetName).append("\",\n");

        if (apartmentNumber != null && !apartmentNumber.isEmpty()) {
            sb.append("  \"apartmentNumber\": \"").append(apartmentNumber).append("\",\n");
        }

        sb.append("  \"city\": \"").append(city).append("\",\n");

        if (district != null && !district.isEmpty()) {
            sb.append("  \"district\": \"").append(district).append("\",\n");
        }

        sb.append("  \"province\": \"").append(province).append("\",\n");
        sb.append("  \"country\": \"").append(country).append("\",\n");

        if (postalCode != null && !postalCode.isEmpty()) {
            sb.append("  \"postalCode\": \"").append(postalCode).append("\"\n");
        } else {
            sb.append("  \"postalCode\": null\n");
        }

        sb.append("}");
        return sb.toString();
    }
}