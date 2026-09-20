package data;

import models.ServiceItem;

import java.math.BigDecimal;

public class ServiceItemData {

    private final ServiceItem item;

    private final long id;
    private final String name;
    private final String category;
    private final String description;
    private final String price;
    private final String duration;
    private final String status;
    private final boolean active;

    public ServiceItemData(ServiceItem item) {
        this.item = item;

        this.id = item.getId();
        this.name = item.getName();
        this.category = item.getCategory();

        this.description = item.getDescription() == null ? "—" : item.getDescription();
        this.price = formatPrice(item.getPrice());
        this.duration = item.getDuration() == null ? "Varies" : item.getDuration();

        this.status = item.isActive() ? "ACTIVE" : "INACTIVE";
        this.active = item.isActive();
    }

    private String formatPrice(BigDecimal p) {
        if (p == null) {
            return "—";
        }

        if (p.compareTo(BigDecimal.ZERO) == 0) {
            return "Complimentary";
        }

        return "R" + String.format("%,.2f", p);
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }

    public String getPrice() {
        return price;
    }

    public String getDuration() {
        return duration;
    }

    public String getStatus() {
        return status;
    }

    public boolean isActive() {
        return active;
    }

    public ServiceItem getItem() {
        return item;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof ServiceItemData s)) {
            return false;
        }

        return java.util.Objects.equals(name, s.name) && java.util.Objects.equals(category, s.category) && java.util.Objects.equals(description, s.description) && java.util.Objects.equals(price, s.price) && java.util.Objects.equals(duration, s.duration) && java.util.Objects.equals(status, s.status) && id == s.id && active == s.active;
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(name, category, description, price, duration, status, id, active);
    }
}