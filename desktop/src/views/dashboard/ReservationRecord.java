package views.dashboard;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcons;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import utils.ReservationsData;

public class ReservationRecord extends VBox {

    public ReservationRecord(
            ReservationsData data,
            Runnable onClose,
            Runnable onConfirm,
            Runnable onReject,
            Runnable onCancel,
            Runnable onPrint
    ) {
        getStyleClass().add("booking-record");

        boolean paymentComplete = "completed".equalsIgnoreCase(data.getPaymentStatus());

        HBox header = new HBox();
        header.getStyleClass().add("booking-record-header");
        header.setAlignment(Pos.CENTER_LEFT);

        Label title = new Label("Reservation — " + data.getReservationId());
        title.getStyleClass().add("booking-record-title");
        HBox.setHgrow(title, Priority.ALWAYS);
        title.setMaxWidth(Double.MAX_VALUE);

        Button closeX = new Button("✕");
        closeX.getStyleClass().add("booking-record-close");
        closeX.setOnAction(e -> onClose.run());

        header.getChildren().addAll(title, closeX);

        HBox stats = new HBox();
        stats.getStyleClass().add("booking-record-stats");
        stats.getChildren().addAll(
                stat("GUEST", data.getGuestName()),
                stat("ROOM", data.getRoomNumber() + " · " + data.getRoomType()),
                stat("NIGHTS", String.valueOf(data.getNights())),
                stat("STATUS", data.getStatus())
        );

        HBox body = new HBox();
        body.getStyleClass().add("booking-record-body");

        VBox stay = new VBox();
        stay.getStyleClass().add("booking-record-section");
        HBox.setHgrow(stay, Priority.ALWAYS);
        Label stayTitle = new Label("STAY DETAILS");
        stayTitle.getStyleClass().add("booking-record-section-title");
        stay.getChildren().addAll(
                stayTitle,
                row("Reservation ID", data.getReservationId()),
                row("Arrival", data.getArrival()),
                row("Departure", data.getDeparture()),
                row("Nights", String.valueOf(data.getNights())),
                row("Guests", String.valueOf(data.getGuests())),
                row("Booked On", data.getBookedOn())
        );

        VBox pricing = new VBox();
        pricing.getStyleClass().add("booking-record-section");
        HBox.setHgrow(pricing, Priority.ALWAYS);
        Label pricingTitle = new Label("PAYMENT");
        pricingTitle.getStyleClass().add("booking-record-section-title");
        pricing.getChildren().addAll(
                pricingTitle,
                row("Room Rate", data.getRoomRate()),
                row("Nights", String.valueOf(data.getNights())),
                row("Total", data.getTotal()),
                row("Payment Status", data.getPaymentStatus())
        );

        body.getChildren().addAll(stay, pricing);

        HBox footer = new HBox();
        footer.getStyleClass().add("booking-record-footer");
        footer.setAlignment(Pos.CENTER_LEFT);

        Button print = new Button("Print");
        print.getStyleClass().add("registry-button");

        FontAwesomeIcon printerIcon = new FontAwesomeIcon();
        printerIcon.setIcon(FontAwesomeIcons.PRINT);
        printerIcon.setSize("11");
        printerIcon.setFill(Color.web("#9a9080"));
        print.setGraphic(printerIcon);

        print.setOnAction(e -> onPrint.run());

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button close = new Button("Close");
        close.getStyleClass().add("registry-button-primary");
        close.setOnAction(e -> onClose.run());

        if (paymentComplete) {
            Button reject = new Button("Reject");
            reject.getStyleClass().add("reservation-reject");
            reject.setOnAction(e -> onReject.run());

            Button confirm = new Button("Confirm Reservation");
            confirm.getStyleClass().add("reservation-confirm");

            FontAwesomeIcon confirmIcon = new FontAwesomeIcon();
            confirmIcon.setIcon(FontAwesomeIcons.CHECK);
            confirmIcon.setSize("11");
            confirmIcon.setFill(Color.web("#ffffff"));
            confirm.setGraphic(confirmIcon);

            confirm.setOnAction(e -> onConfirm.run());

            footer.getChildren().addAll(reject, print, spacer, confirm, close);
        }

        else {
            Button cancel = new Button("Cancel");
            cancel.getStyleClass().add("reservation-reject");
            cancel.setOnAction(e -> onCancel.run());

            footer.getChildren().addAll(cancel, print, spacer, close);
        }

        getChildren().addAll(
                header,
                new Separator(),
                stats,
                new Separator(),
                body,
                new Separator(),
                footer
        );
    }

    private VBox stat(String label, String value) {
        Label k = new Label(label);
        k.getStyleClass().add("booking-record-stat-key");
        Label v = new Label(value);
        v.getStyleClass().add("booking-record-stat-value");
        VBox box = new VBox(2, k, v);
        box.getStyleClass().add("booking-record-stat");
        HBox.setHgrow(box, Priority.ALWAYS);
        box.setMaxWidth(Double.MAX_VALUE);
        return box;
    }

    private HBox row(String key, String value) {
        HBox r = new HBox();
        r.getStyleClass().add("booking-record-row");

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