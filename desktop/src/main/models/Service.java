package main.models;

/**
 * Represents an additional service offered by Mason Hotel that a guest can request or use during their stay.
 *
 * This class represents all hotel services including:
 * - Room service
 * - Laundry
 * - Airport transfers
 * - Spa services
 * - Restaurant services
 * - Other hotel services
 *
 * Features:
 * - Stores service information
 * - Stores service pricing
 * - Categorizes hotel services
 * - Tracks service availability
 * - Allows the hotel to manage its available services
 * - Provides information about additional services offered by the hotel
 */
public class Service {

    private final long id;
    private final String serviceName;
    private final String description;
    private final String category;
    private final float price;
    private final boolean available;

    /**
     * Constructs a new Service object.
     *
     * @param id           Service's unique identifier
     * @param serviceName  Name of the service
     * @param description  Description of the service
     * @param category     Category the service belongs to
     * @param price        Price of the service
     * @param available    Availability status of the service
     */
    public Service(long id, String serviceName, String description, String category, float price, boolean available) {
        this.id = id;
        this.serviceName = serviceName;
        this.description = description;
        this.category = category;
        this.price = price;
        this.available = available;
    }

    public long getId() {
        return this.id;
    }

    public String getServiceName() {
        return this.serviceName;
    }

    public String getDescription() {
        return this.description;
    }

    public String getCategory() {
        return this.category;
    }

    public float getPrice() {
        return this.price;
    }

    public boolean isAvailable() {
        return this.available;
    }

    @Override
    public String toString() {
        return "{\n" +
                "  \"id\": " + id + ",\n" +
                "  \"serviceName\": \"" + serviceName + "\",\n" +
                "  \"description\": \"" + description + "\",\n" +
                "  \"category\": \"" + category + "\",\n" +
                "  \"price\": " + price + ",\n" +
                "  \"available\": " + available + "\n" +
                "}";
    }
}