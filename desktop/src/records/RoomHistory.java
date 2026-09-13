package records;

import java.io.Serializable;

public record RoomHistory(
        long id,
        long roomid,
        java.sql.Timestamp status_date,
        String status,
        java.sql.Timestamp created_at,
        java.sql.Timestamp updated_at
) implements Serializable {
}