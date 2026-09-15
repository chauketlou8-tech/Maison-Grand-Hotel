package views.dashboard;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcons;
import enums.NotificationType;
import enums.ReservationStatus;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import models.Reservation;
import records.Payment;
import tasks.Emailer;
import utils.Conn;
import utils.ReservationsData;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class ReservationRecord extends VBox {

    Connection connection = Conn.getConnection();

    private final ReservationsData data;
    private final Runnable onClose;

    public ReservationRecord(ReservationsData data, Runnable onClose) throws IOException {
        this.data = data;
        this.onClose = onClose;
        getStyleClass().add("booking-record");

        boolean paymentComplete = "completed".equalsIgnoreCase(data.getPaymentStatus());
        boolean alreadyConfirmed = "CONFIRMED".equalsIgnoreCase(data.getStatus());
        boolean alreadyCancelled = "CANCELLED".equalsIgnoreCase(data.getStatus());
        boolean alreadyRejected = "REJECTED".equalsIgnoreCase(data.getStatus());

        boolean canConfirm = paymentComplete && !alreadyConfirmed && !alreadyCancelled && !alreadyRejected;
        boolean canCancel = !paymentComplete && !alreadyConfirmed && !alreadyCancelled && !alreadyRejected;

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
        stats.getChildren().addAll(stat("GUEST", data.getGuestName()), stat("ROOM", data.getRoomNumber() + " · " + data.getRoomType()), stat("NIGHTS", String.valueOf(data.getNights())), stat("STATUS", data.getStatus()));

        HBox body = new HBox();
        body.getStyleClass().add("booking-record-body");

        VBox stay = new VBox();
        stay.getStyleClass().add("booking-record-section");
        HBox.setHgrow(stay, Priority.ALWAYS);
        Label stayTitle = new Label("STAY DETAILS");
        stayTitle.getStyleClass().add("booking-record-section-title");
        stay.getChildren().addAll(stayTitle, row("Reservation ID", data.getReservationId()), row("Arrival", data.getArrival()), row("Departure", data.getDeparture()), row("Nights", String.valueOf(data.getNights())), row("Guests", String.valueOf(data.getGuests())), row("Booked On", data.getBookedOn()));

        VBox pricing = new VBox();
        pricing.getStyleClass().add("booking-record-section");
        HBox.setHgrow(pricing, Priority.ALWAYS);
        Label pricingTitle = new Label("PAYMENT");
        pricingTitle.getStyleClass().add("booking-record-section-title");
        pricing.getChildren().addAll(pricingTitle, row("Room Rate", data.getRoomRate()), row("Nights", String.valueOf(data.getNights())), row("Total", data.getTotal()), row("Payment Status", data.getPaymentStatus()));

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

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button close = new Button("Close");
        close.getStyleClass().add("registry-button-primary");
        close.setOnAction(e -> onClose.run());

        if (canConfirm) {
            Button reject = new Button("Reject");
            reject.getStyleClass().add("reservation-reject");

            Button confirm = new Button("Confirm Reservation");
            confirm.getStyleClass().add("reservation-confirm");

            FontAwesomeIcon confirmIcon = new FontAwesomeIcon();
            confirmIcon.setIcon(FontAwesomeIcons.CHECK);
            confirmIcon.setSize("11");
            confirmIcon.setFill(Color.web("#ffffff"));
            confirm.setGraphic(confirmIcon);

            confirm.setOnAction(e -> {
                try {
                    this.confirmReservation();
                }

                catch (SQLException | IOException ex) {
                    throw new RuntimeException(ex);
                }
            });

            reject.setOnAction(e -> {
                try {
                    this.rejectReservation();
                }

                catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            });

            footer.getChildren().addAll(reject, print, spacer, confirm, close);
        }

        else if (canCancel) {
            Button cancel = new Button("Cancel");
            cancel.getStyleClass().add("reservation-reject");
            cancel.setOnAction(e -> this.cancelReservation());

            footer.getChildren().addAll(cancel, print, spacer, close);
        }

        else {
            footer.getChildren().addAll(print, spacer, close);
        }

        getChildren().addAll(header, new Separator(), stats, new Separator(), body, new Separator(), footer);
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

    private Properties loadProperties() {
        Properties properties = new Properties();

        try (InputStream inputStream = Emailer.class.getClassLoader().getResourceAsStream("config/application.properties")) {

            if (inputStream == null) {
                throw new RuntimeException("config/application.properties not found");
            }

            properties.load(inputStream);
        }

        catch (IOException e) {
            e.printStackTrace();
        }

        return properties;
    }

    private void confirmReservation() throws SQLException, IOException {
        Reservation reservation = data.getReservation();

        assert connection != null;

        try(PreparedStatement statement = connection.prepareStatement("insert into bookings (booking_id, reservation_id, guest_id, room_id, booking_date, check_in_date, check_out_date, number_of_guests, booking_status, total_cost, selected_packages, requests) values(?,?,?,?,?,?,?,?,?::bookingstatus,?,?,?)")) {
            statement.setString(1, "BK-" + reservation.getId());
            statement.setObject(2, reservation.getReservationid());
            statement.setLong(3, reservation.getGuest());
            statement.setLong(4, reservation.getReservedroom());
            statement.setTimestamp(5, reservation.getReservationdate());
            statement.setDate(6, reservation.getCheckindate());
            statement.setDate(7, reservation.getCheckoutdate());
            statement.setLong(8, reservation.getNumberofguests());
            statement.setString(9, "PENDING");
            statement.setFloat(10, reservation.getTotal());
            statement.setArray(11, connection.createArrayOf("text", reservation.getSelectedpackages() == null ? new String[0] : reservation.getSelectedpackages()));
            statement.setString(12, reservation.getRequests());

            statement.execute();
        }

        try(PreparedStatement statement = connection.prepareStatement("update reservations set reservationstatus = ?::reservationstatus where id = ?;")) {
            statement.setString(1, "CONFIRMED");
            statement.setLong(2, reservation.getId());

            statement.execute();
        }

        String name = data.getGuestName();
        String checkIn = data.getArrival();
        String checkOut = data.getDeparture();
        long guests = data.getGuests();
        String roomName = data.getRoomNumber() + " · " + data.getRoomType();
        Payment payment = data.getPayment();

        Properties properties = loadProperties();

        try (PreparedStatement statement = connection.prepareStatement("insert into notifications (type, title, message, reservationid, guestid, payment_id) values (?, ?, ?, ?, ?, ?)")) {

            statement.setObject(1, NotificationType.reservation_confirmation.name(), java.sql.Types.OTHER);
            statement.setString(2, "Reservation Confirmation");
            statement.setString(3, "Reservation " + reservation.getReservationid() + " has been confirmed.");
            statement.setLong(4, reservation.getId());
            statement.setLong(5, data.getGuestId());
            statement.setLong(6, data.getPaymentId());

            statement.execute();
        }

        String base_url = properties.getProperty("base.url");
        String guest_email = data.getGuestEmail();

        String emailSubject = "Mason Hotel — Reservation Confirmation";

        String emailMessage = "Dear " + name + ",\n\n" +
                "We are delighted to confirm your reservation at Mason Hotel.\n\n" +
                "Your payment has been received and your booking is now fully confirmed. We look forward to welcoming you.\n\n" +
                "RESERVATION DETAILS\n" +
                "─────────────────\n" +
                "Reservation ID: " + reservation.getReservationid() + "\n" +
                "Check-in: " + checkIn + "\n" +
                "Check-out: " + checkOut + "\n" +
                "Guests: " + guests + "\n" +
                "Room: " + roomName + "\n\n" +
                "PAYMENT\n" +
                "─────────────────\n" +
                "Amount Paid: R" + payment.amount() + "\n" +
                "Payment Status: Confirmed\n" +
                "Transaction Reference: " + payment.transactionreference() + "\n\n" +
                "VIEW YOUR BOOKING\n" +
                "─────────────────\n" +
                "To view or manage your booking at any time, visit:\n" +
                base_url + "/booking/" + reservation.getReservationid() + "\n\n" +
                "ARRIVAL INFORMATION\n" +
                "─────────────────\n" +
                "Check-in begins at 14:00. Please present your Reservation ID and a valid form of identification at reception.\n\n" +
                "If your travel plans change or you have any special requests, please contact us ahead of your arrival and we will do our best to accommodate you.\n\n" +
                "We look forward to hosting you at Mason Hotel.\n\n" +
                "Kind regards,\n\n" +
                "Mason Hotel\n" +
                "Guest Services\n" +
                "stay@maisongrandhotel.co.za\n" +
                "+27 21 987 6543\n" +
                "08 Greenwood Street, Gardens, Cape Town, 7700\n" +
                "www.maisongrandhotel.co.za";

        new Thread(() -> {
            try {
                Emailer.sendEmail(guest_email, emailSubject, emailMessage, name);
            }

            catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

        onClose.run();
    }

    private void cancelReservation() {
        Reservation reservation = data.getReservation();
        long room_id = data.getRoomId();

        assert connection != null;

        try(PreparedStatement statement = connection.prepareStatement("update reservations set reservationstatus = 'CANCELLED' where id = ?")) {
            statement.setLong(1, reservation.getId());
            statement.execute();
        }

        catch (SQLException e) {
            throw new RuntimeException(e);
        }

        try(PreparedStatement statement = connection.prepareStatement("update rooms set roomstatus = 'available' where roomid = ?")) {
            statement.setLong(1, room_id);
            statement.execute();
        }

        catch (SQLException e) {
            throw new RuntimeException(e);
        }

        try(PreparedStatement statement = connection.prepareStatement("insert into notifications (type, title, message, reservationid, guestid, payment_id) values (?::notificationtype, ?, ?, ?, ?, ?)")) {

            statement.setObject(1, NotificationType.reservation_cancelled.name(), java.sql.Types.OTHER);
            statement.setString(2, "Reservation Cancellation notification");
            statement.setString(3, "Reservation " + reservation.getReservationid() + " cancelled.");
            statement.setLong(4, reservation.getId());
            statement.setLong(5, data.getGuestId());
            statement.setLong(6, data.getPaymentId());

            statement.execute();
        }

        catch (SQLException e) {
            throw new RuntimeException(e);
        }

        try(Statement statement = connection.createStatement()) {
            statement.execute("update payments set paymentstatus = 'cancelled' where reservation_id = " + reservation.getReservationid());
        }

        catch (SQLException e) {
            throw new RuntimeException(e);
        }

        Properties properties = loadProperties();

        String name = data.getGuestName();
        String checkIn = data.getArrival();
        String checkOut = data.getDeparture();
        String base_url = properties.getProperty("base.url");
        String guest_email = data.getGuestEmail();

        String emailSubject = "Mason Hotel — Reservation Cancelled";

        String emailMessage = "Dear " + name + "\n\n" +
                "We regret to inform you that your reservation at Mason Hotel has been cancelled because the required payment was not received within the allocated payment period.\n\n" +
                "RESERVATION DETAILS\n" +
                "─────────────────\n" +
                "Reservation ID: " + reservation.getReservationid() + "\n" +
                "Check-in: " + checkIn + "\n" +
                "Check-out: " + checkOut + "\n" +
                "Guests: " + reservation.getNumberofguests() + "\n" +
                "Room: " + reservation.getRequestedroomtype() + "\n\n" +
                "PAYMENT STATUS\n" +
                "─────────────────\n" +
                "Payment Status: Not Received\n" +
                "Reservation Status: Cancelled\n\n" +
                "As payment was not completed within the allocated period, the reservation has been automatically cancelled and the room has been released for other guests.\n\n" +
                "WHAT TO DO NEXT\n" +
                "─────────────────\n" +
                "If you believe this cancellation was made in error or you would still like to stay with us, you may make a new reservation through our booking system:\n" +
                base_url + "/booking\n\n" +
                "If you have any questions regarding this cancellation, please contact us and provide your Reservation ID.\n\n" +
                "We hope to welcome you to Mason Hotel in the future.\n\n" +
                "Kind regards,\n\n" +
                "Mason Hotel\n" +
                "Guest Services\n" +
                "stay@maisongrandhotel.co.za\n" +
                "+27 21 987 6543\n" +
                "08 Greenwood Street, Gardens, Cape Town, 7700\n" +
                "www.maisongrandhotel.co.za";

        new Thread(() -> {
            try {
                Emailer.sendEmail(guest_email, emailSubject, emailMessage, name);
            }

            catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

        onClose.run();
    }

    private void rejectReservation() throws SQLException {
        Reservation reservation = data.getReservation();
        long room_id = data.getRoomId();

        assert connection != null;

        try(PreparedStatement statement = connection.prepareStatement("update reservations set reservationstatus = ? where id = ?")) {
            statement.setObject(1, ReservationStatus.REJECTED.name(), java.sql.Types.OTHER);
            statement.setLong(2, reservation.getId());

            statement.execute();
        }

        try(PreparedStatement statement = connection.prepareStatement("update rooms set roomstatus = 'available' where roomid = ?")) {
            statement.setLong(1, room_id);
            statement.execute();
        }

        onClose.run();
    }
}