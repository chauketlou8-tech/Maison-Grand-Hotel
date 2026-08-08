package main.models;

import java.util.Date;

/**
 * Represents a record of a payment transaction made through Mason Hotel's online payment system.
 *
 * This class represents all payment transactions including:
 * - Successful payments
 * - Failed payments
 * - Pending payments
 * - Refunded payments
 * - Payments made for hotel bookings
 *
 * Features:
 * - Records payment information
 * - Records the transaction amount
 * - Tracks payment dates
 * - Records the payment method
 * - Tracks payment status
 * - Stores the payment provider's transaction reference
 * - Maintains a history of payment transactions
 */
public record Payment(long id, float amount, Date paymentDate, String paymentMethod, String paymentStatus, String transactionReference) {
}