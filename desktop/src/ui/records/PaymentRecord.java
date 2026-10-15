package ui.records;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import records.Settings;
import records.Payment;
import ui.data.PaymentData;

import java.time.format.DateTimeFormatter;

public class PaymentRecord extends VBox {

    public PaymentRecord(PaymentData data, Runnable close, Settings settings) {

        String datePattern = javaPattern(settings == null ? null : settings.dateFormat());
        String timePattern = "12-hour".equalsIgnoreCase(settings == null ? null : settings.timeFormat()) ? "h:mm a" : "HH:mm";

        DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern(datePattern);
        DateTimeFormatter TIMESTAMP_FMT = DateTimeFormatter.ofPattern(datePattern + " " + timePattern);

        getStyleClass().add("booking-record");
        setSpacing(12);
        setPadding(new Insets(20, 24, 20, 24));
        setMaxWidth(Double.MAX_VALUE);

        Payment p = data.getPayment();

        HBox header = new HBox();
        header.getStyleClass().add("booking-record-header");
        header.setAlignment(Pos.CENTER_LEFT);

        Label title = new Label("Payment — " + data.getTxnId());
        title.getStyleClass().add("booking-record-title");
        HBox.setHgrow(title, Priority.ALWAYS);
        title.setMaxWidth(Double.MAX_VALUE);

        Button closeBtn = new Button("✕");
        closeBtn.getStyleClass().add("booking-record-close");
        closeBtn.setOnAction(_ -> close.run());

        header.getChildren().addAll(title, closeBtn);

        HBox body = new HBox();
        body.getStyleClass().add("booking-record-body");
        body.setMaxWidth(Double.MAX_VALUE);
        body.setSpacing(40);

        VBox guest = section("Guest");
        guest.getChildren().addAll(
                row("Guest",       data.getGuestName()),
                row("Guest ID",    String.valueOf(p.guest_id())),
                row("Reservation", p.reservation_id() == null ? "—" : p.reservation_id().toString())
        );

        VBox payment = section("Payment");
        payment.getChildren().addAll(
                row("Amount",         "R " + String.format("%,.2f", p.amount())),
                row("Status",         p.paymentstatus() == null ? "—" : p.paymentstatus()),
                row("Method",         p.paymentmethod() == null || p.paymentmethod().isBlank() ? "—" : p.paymentmethod()),
                row("Reference",      p.transactionreference() == null || p.transactionreference().isBlank() ? "—" : p.transactionreference()),
                row("Payment date",   p.paymentdate() == null ? "—" : DATE_FMT.format(p.paymentdate().toLocalDate())),
                row("Yoco checkout",  p.yoco_checkout_id() == null || p.yoco_checkout_id().isBlank() ? "—" : p.yoco_checkout_id())
        );

        VBox audit = section("Audit");
        audit.getChildren().addAll(
                row("Created", p.created_at() == null ? "—" : TIMESTAMP_FMT.format(p.created_at().toLocalDateTime())),
                row("Updated", p.updated_at() == null ? "—" : TIMESTAMP_FMT.format(p.updated_at().toLocalDateTime()))
        );

        grow(guest);
        grow(payment);
        grow(audit);

        body.getChildren().addAll(guest, payment, audit);

        getChildren().addAll(header, new Separator(), body);
    }

    private static String javaPattern(String pattern) {
        if (pattern == null || pattern.isBlank()) {
            return "dd/MM/yyyy";
        }

        String converted = pattern.replace("DD", "dd").replace("YYYY", "yyyy");

        try {
            DateTimeFormatter.ofPattern(converted);
            return converted;
        } catch (IllegalArgumentException e) {
            return "dd/MM/yyyy";
        }
    }

    private void grow(VBox section) {
        HBox.setHgrow(section, Priority.ALWAYS);
        section.setMaxWidth(Double.MAX_VALUE);
    }

    private VBox section(String title) {
        VBox v = new VBox();
        v.getStyleClass().add("booking-record-section");

        Label t = new Label(title.toUpperCase());
        t.getStyleClass().add("booking-record-section-title");

        v.getChildren().addAll(t, new Separator());
        return v;
    }

    private HBox row(String key, String value) {
        HBox r = new HBox();
        r.getStyleClass().add("booking-record-row");
        r.setMaxWidth(Double.MAX_VALUE);

        Label k = new Label(key);
        k.getStyleClass().add("booking-record-key");

        Label v = new Label(value);
        v.getStyleClass().add("booking-record-value");
        HBox.setHgrow(v, Priority.ALWAYS);
        v.setMaxWidth(Double.MAX_VALUE);

        r.getChildren().addAll(k, v);
        return r;
    }
}