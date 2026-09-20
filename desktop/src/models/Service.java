package models;

import java.io.Serializable;
import java.sql.Timestamp;

public class Service implements Serializable {

    private final long id;
    private final String name;
    private final String description;
    private final boolean recurring;
    private final Timestamp created_at;
    private final Timestamp updated_at;

    public Service(long id, String name, String description, boolean recurring, Timestamp created_at, Timestamp updated_at) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.recurring = recurring;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    public boolean isRecurring() {
        return this.recurring;
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
                "  \"name\": \"" + name + "\",\n" +
                "  \"description\": \"" + description + "\",\n" +
                "  \"recurring\": " + recurring + ",\n" +
                "  \"created_at\": \"" + created_at + "\",\n" +
                "  \"updated_at\": \"" + updated_at + "\"\n" +
                "}";
    }
}