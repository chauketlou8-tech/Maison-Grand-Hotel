package manual;

import models.Payment;

import java.util.Date;

public class PaymentRecordTest {

    public static void main(String[] args) {
        Payment payment = new Payment(1, 1500.00f, new Date(2026, 7, 10), "Credit Card", "SUCCESS", "TXN123456789");
        System.out.println(payment);
    }
}