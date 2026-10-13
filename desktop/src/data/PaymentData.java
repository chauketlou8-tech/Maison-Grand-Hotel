package data;

import models.Guest;
import records.Settings;
import records.Payment;
import utils.DateFormats;

import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class PaymentData {

    private final Payment payment;

    private final String txnId;
    private final String guestName;
    private final String reference;
    private final String method;
    private final String amount;
    private final String date;
    private final String status;

    private final DateTimeFormatter dateFmt;

    private final Settings settings;

    public PaymentData(Payment payment, Guest[] guests, Settings settings) {
        this.payment = payment;
        this.dateFmt = DateFormats.of(settings);

        this.txnId = "PAY-" + String.format("%03d", payment.id());
        this.guestName = resolveGuestName(payment.guest_id(), guests);
        this.reference = payment.transactionreference() != null && !payment.transactionreference().isBlank() ? payment.transactionreference() : formatReservationId(payment.reservation_id());
        this.method = payment.paymentmethod() == null || payment.paymentmethod().isBlank() ? "—" : payment.paymentmethod();
        this.amount = "R " + String.format("%,.2f", payment.amount());
        this.date = formatDate(payment);
        this.status = payment.paymentstatus() == null ? "—" : payment.paymentstatus();

        this.settings = settings;
    }

    private String resolveGuestName(long guestId, Guest[] guests) {
        if (guests == null) {
            return "—";
        }

        for (Guest g : guests) {
            if (g.getId() == guestId) {
                return g.getFirstname() + " " + g.getLastname();
            }
        }

        return "—";
    }

    private String formatReservationId(UUID reservationId) {
        if (reservationId == null) {
            return "—";
        }

        String s = reservationId.toString();
        return s.length() >= 8 ? s.substring(0, 8) : s;
    }

    private String formatDate(Payment p) {
        if (p.paymentdate() != null) {
            return p.paymentdate().toLocalDate().format(dateFmt);
        }

        if (p.created_at() != null) {
            return p.created_at().toLocalDateTime().toLocalDate().format(dateFmt);
        }

        return "—";
    }

    public String getTxnId() {
        return txnId;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getReference() {
        return reference;
    }

    public String getMethod() {
        return method;
    }

    public String getAmount() {
        return amount;
    }

    public String getDate() {
        return date;
    }

    public String getStatus() {
        return status;
    }

    public Payment getPayment() {
        return payment;
    }

    public boolean isCollected() {
        return "completed".equalsIgnoreCase(status) || "succeeded".equalsIgnoreCase(status);
    }

    public boolean isInFlight() {
        return "created".equalsIgnoreCase(status) || "started".equalsIgnoreCase(status) || "processing".equalsIgnoreCase(status);
    }

    public boolean isFailed() {
        return "failed".equalsIgnoreCase(status) || "cancelled".equalsIgnoreCase(status);
    }

    public boolean isThisMonth() {
        java.sql.Date d = payment.paymentdate();
        if (d == null) {
            return false;
        }

        java.time.LocalDate local = d.toLocalDate();
        java.time.YearMonth now = java.time.YearMonth.now();

        return java.time.YearMonth.from(local).equals(now);
    }

    public Settings getSettings() {
        return settings;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof PaymentData p)) {
            return false;
        }

        return java.util.Objects.equals(txnId, p.txnId) && java.util.Objects.equals(guestName, p.guestName) && java.util.Objects.equals(reference, p.reference) && java.util.Objects.equals(method, p.method) && java.util.Objects.equals(amount, p.amount) && java.util.Objects.equals(date, p.date) && java.util.Objects.equals(status, p.status);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(txnId, guestName, reference, method, amount, date, status);
    }
}