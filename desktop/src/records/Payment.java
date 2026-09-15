package records;

import java.io.Serializable;

public record Payment(
        long id,
        double amount,
        java.sql.Date paymentdate,
        String paymentstatus,
        String transactionreference,
        long guest_id,
        java.util.UUID reservation_id,
        String yoco_checkout_id,
        String paymentmethod,
        java.sql.Timestamp created_at,
        java.sql.Timestamp updated_at
) implements Serializable {
}