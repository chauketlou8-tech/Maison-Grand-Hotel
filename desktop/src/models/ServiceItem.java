package models;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

public class ServiceItem implements Serializable {

    private final long id;
    private final int service_id;
    private final String name;
    private final String category;
    private final String description;
    private final BigDecimal price;
    private final String duration;
    private final boolean active;
    private final Timestamp created_at;
    private final Timestamp updated_at;

    public ServiceItem(
            long id,
            int service_id,
            String name,
            String category,
            String description,
            BigDecimal price,
            String duration,
            boolean active,
            Timestamp created_at,
            Timestamp updated_at
    ) {
        this.id = id;
        this.service_id = service_id;
        this.name = name;
        this.category = category;
        this.description = description;
        this.price = price;
        this.duration = duration;
        this.active = active;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public long getId() {
        return this.id;
    }

    public int getService_id() {
        return this.service_id;
    }

    public String getName() {
        return this.name;
    }

    public String getCategory() {
        return this.category;
    }

    public String getDescription() {
        return this.description;
    }

    public BigDecimal getPrice() {
        return this.price;
    }

    public String getDuration() {
        return this.duration;
    }

    public boolean isActive() {
        return this.active;
    }

    public Timestamp getCreated_at() {
        return this.created_at;
    }

    public Timestamp getUpdated_at() {
        return this.updated_at;
    }
}