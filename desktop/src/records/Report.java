package records;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;

public record Report(
        int id,
        LocalDate reportDate,
        int totalRooms,
        int occupiedRooms,
        int availableRooms,
        int maintenanceRooms,
        int arrivals,
        int departures,
        int inHouse,
        BigDecimal revenue,
        int tasksCreated,
        int tasksCompleted,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt
) {}