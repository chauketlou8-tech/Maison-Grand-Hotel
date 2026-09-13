package records;

import java.io.Serializable;

public record Address(long id, String street, String city, String province, String country, String postalCode, java.sql.Timestamp created_at, java.sql.Timestamp updated_at) implements Serializable {
}
