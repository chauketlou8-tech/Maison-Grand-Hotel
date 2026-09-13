import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertEquals;

public class PaymentRecordTest {
    @Test
    public void testPaymentModel() {
        Payment payment = new Payment(1, 1500.00f, new Date(2026, 7, 10), "Credit Card", "SUCCESS", "TXN123456789");

        assertEquals(1, payment.id());
        assertEquals(1500.00f, payment.amount(), 0.01);
        assertEquals(new Date(2026, 7, 10), payment.paymentDate());
        assertEquals("Credit Card", payment.paymentMethod());
        assertEquals("SUCCESS", payment.paymentStatus());
        assertEquals("TXN123456789", payment.transactionReference());
    }
}