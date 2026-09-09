package models;

import java.util.Date;

public class Payment {

    private final long id;
    private final float amount;
    private final Date paymentdate;
    private final String paymentmethod;
    private final String paymentstatus;
    private final String transactionreference;
    private final String yoco_checkout_id;

    public Payment(long id, float amount, Date paymentdate, String paymentmethod, String paymentstatus, String transactionreference, String yoco_checkout_id) {
        this.id = id;
        this.amount = amount;
        this.paymentdate = paymentdate;
        this.paymentmethod = paymentmethod;
        this.paymentstatus = paymentstatus;
        this.transactionreference = transactionreference;
        this.yoco_checkout_id = yoco_checkout_id;
    }

    public long getId() {
        return this.id;
    }

    public float getAmount() {
        return this.amount;
    }

    public Date getPaymentdate() {
        return this.paymentdate;
    }

    public String getPaymentmethod() {
        return this.paymentmethod;
    }

    public String getPaymentstatus() {
        return this.paymentstatus;
    }

    public String getTransactionreference() {
        return this.transactionreference;
    }

    public String getYoco_checkout_id() {
        return this.yoco_checkout_id;
    }

    @Override
    public String toString() {
        return "{\n" +
                "  \"id\": " + id + ",\n" +
                "  \"amount\": " + amount + ",\n" +
                "  \"paymentdate\": \"" + paymentdate + "\",\n" +
                "  \"paymentmethod\": \"" + paymentmethod + "\",\n" +
                "  \"paymentstatus\": \"" + paymentstatus + "\",\n" +
                "  \"transactionreference\": \"" + transactionreference + "\",\n" +
                "  \"yoco_checkout_id\": \"" + yoco_checkout_id + "\"\n" +
                "}";
    }
}