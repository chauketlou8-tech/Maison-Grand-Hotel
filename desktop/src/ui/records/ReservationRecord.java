package ui.records;

import config.ConnectJedis;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcons;
import enums.NotificationType;
import enums.ReservationStatus;
import redis.clients.jedis.RedisClient;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.print.PrinterJob;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import models.Reservation;
import records.Settings;
import records.Payment;
import services.EmailService;
import utils.Conn;
import ui.data.ReservationsData;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Properties;

public class ReservationRecord extends VBox {

    private final ReservationsData data;
    private final Settings settings;

    public ReservationRecord(ReservationsData data) {
        this.data = data;
        this.settings = data.getSettings();
        getStyleClass().add("booking-record");

        boolean paymentComplete = isPaymentComplete();
        boolean alreadyConfirmed = "CONFIRMED".equalsIgnoreCase(data.getStatus());
        boolean alreadyCancelled = "CANCELLED".equalsIgnoreCase(data.getStatus());
        boolean alreadyRejected = "REJECTED".equalsIgnoreCase(data.getStatus());
        boolean alreadyExpired = "EXPIRED".equalsIgnoreCase(data.getStatus());

        boolean canConfirm = paymentComplete && !alreadyConfirmed && !alreadyCancelled && !alreadyRejected && !alreadyExpired;
        boolean canCancel = !paymentComplete && !alreadyConfirmed && !alreadyCancelled && !alreadyRejected && !alreadyExpired && withinCancellationWindow();

        HBox header = new HBox();
        header.getStyleClass().add("booking-record-header");
        header.setAlignment(Pos.CENTER_LEFT);

        Label title = new Label("Reservation — " + data.getReservationId());
        title.getStyleClass().add("booking-record-title");
        HBox.setHgrow(title, Priority.ALWAYS);
        title.setMaxWidth(Double.MAX_VALUE);

        Button closeX = new Button("✕");
        closeX.getStyleClass().add("booking-record-close");
        closeX.setOnAction(_ -> onClose());

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

        print.setOnAction(_ -> print());

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button close = new Button("Close");
        close.getStyleClass().add("registry-button-primary");
        close.setOnAction(_ -> onClose());

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

            confirm.setOnAction(_ -> {
                try {
                    this.confirmReservation();
                }

                catch (SQLException | IOException ex) {
                    throw new RuntimeException(ex);
                }
            });

            reject.setOnAction(_ -> {
                try {
                    this.rejectReservation();
                }

                catch (SQLException | IOException ex) {
                    throw new RuntimeException(ex);
                }
            });

            footer.getChildren().addAll(reject, print, spacer, confirm, close);
        }

        else if (canCancel) {
            Button cancel = new Button("Cancel");
            cancel.getStyleClass().add("reservation-reject");
            cancel.setOnAction(_ -> {
                try {
                    this.cancelReservation();
                }

                catch (SQLException | IOException ex) {
                    throw new RuntimeException(ex);
                }
            });

            footer.getChildren().addAll(cancel, print, spacer, close);
        }

        else {
            footer.getChildren().addAll(print, spacer, close);
        }

        getChildren().addAll(header, new Separator(), stats, new Separator(), body, new Separator(), footer);
    }

    private boolean isPaymentComplete() {
        return "completed".equalsIgnoreCase(data.getPaymentStatus());
    }

    private boolean withinCancellationWindow() {
        if (settings == null || settings.cancellationWindowHours() <= 0) {
            return true;
        }

        Reservation reservation = data.reservation();
        if (reservation == null || reservation.getCheckindate() == null) {
            return true;
        }

        java.time.LocalDateTime checkIn = reservation.getCheckindate().toLocalDate().atStartOfDay();
        java.time.LocalDateTime cutoff = checkIn.minusHours(settings.cancellationWindowHours());

        return java.time.LocalDateTime.now().isBefore(cutoff);
    }

    private void print() {
        VBox slip = buildPrintSlip();

        Scene scene = new Scene(slip);
        scene.getStylesheets().addAll(this.getScene().getStylesheets());

        slip.applyCss();
        slip.resize(595, slip.prefHeight(595));
        slip.applyCss();
        slip.layout();

        PrinterJob job = PrinterJob.createPrinterJob();

        if (job == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "No printer available.", ButtonType.OK);
            alert.setHeaderText(null);
            alert.initOwner(this.getScene().getWindow());
            alert.showAndWait();
            return;
        }

        boolean proceed = job.showPrintDialog(this.getScene().getWindow());

        if (!proceed) {
            job.endJob();
            return;
        }

        boolean printed = job.printPage(slip);

        if (printed) {
            job.endJob();
        }
    }

    private VBox buildPrintSlip() {
        VBox slip = new VBox(12);
        slip.setPadding(new Insets(40));
        slip.setStyle("-fx-background-color: #ffffff;");
        slip.setPrefWidth(595);
        slip.setMinWidth(595);
        slip.setMaxWidth(595);

        Label hotel = new Label(settings == null ? "" : settings.hotelName());
        hotel.setFont(Font.font("Georgia", FontWeight.BOLD, 20));
        hotel.setTextFill(Color.web("#1a1814"));

        Label address = new Label(settings == null ? "" : settings.hotelStreet() + ", " + settings.hotelCity() + ", " + settings.hotelProvince() + ", " + settings.hotelPostalCode());
        address.setFont(Font.font("Georgia", 11));
        address.setTextFill(Color.web("#444444"));

        Label contact = new Label(settings == null ? "" : settings.hotelPhone() + " · " + settings.hotelEmail());
        contact.setFont(Font.font("Georgia", 11));
        contact.setTextFill(Color.web("#444444"));

        Separator top = new Separator();

        Label title = new Label("RESERVATION SLIP");
        title.setFont(Font.font("Georgia", FontWeight.BOLD, 14));
        title.setTextFill(Color.web("#1a1814"));

        GridPane grid = new GridPane();
        grid.setHgap(24);
        grid.setVgap(8);
        grid.setMaxWidth(Double.MAX_VALUE);

        ColumnConstraints keyCol = new ColumnConstraints();
        keyCol.setMinWidth(140);
        keyCol.setPrefWidth(140);

        ColumnConstraints valueCol = new ColumnConstraints();
        valueCol.setHgrow(Priority.ALWAYS);
        valueCol.setFillWidth(true);

        grid.getColumnConstraints().addAll(keyCol, valueCol);

        int row = 0;

        row = addPrintRow(grid, row, "Reservation ID", data.getReservationId());
        row = addPrintRow(grid, row, "Guest",          data.getGuestName());
        row = addPrintRow(grid, row, "Room",           data.getRoomNumber() + " · " + data.getRoomType());
        row = addPrintRow(grid, row, "Arrival",        data.getArrival());
        row = addPrintRow(grid, row, "Departure",      data.getDeparture());
        row = addPrintRow(grid, row, "Nights",         String.valueOf(data.getNights()));
        row = addPrintRow(grid, row, "Guests",         String.valueOf(data.getGuests()));
        row = addPrintRow(grid, row, "Booked On",      data.getBookedOn());
        row = addPrintRow(grid, row, "Total",          data.getTotal());
        addPrintRow(grid, row, "Payment Status",       data.getPaymentStatus());

        Separator bottom = new Separator();

        Label footer = new Label(settings == null ? "" : settings.hotelName() + " · " + settings.hotelPhone() + " · " + settings.hotelEmail());
        footer.setFont(Font.font("Georgia", 9));
        footer.setTextFill(Color.web("#666666"));

        slip.getChildren().addAll(hotel, address, contact, top, title, grid, bottom, footer);

        return slip;
    }

    private int addPrintRow(GridPane grid, int row, String key, String value) {
        Label k = new Label(key.toUpperCase());
        k.setFont(Font.font("Georgia", 10));
        k.setTextFill(Color.web("#666666"));

        Label v = new Label(value == null ? "—" : value);
        v.setFont(Font.font("Georgia", 12));
        v.setTextFill(Color.web("#1a1814"));
        v.setWrapText(true);
        v.setMaxWidth(Double.MAX_VALUE);

        GridPane.setHgrow(v, Priority.ALWAYS);

        grid.add(k, 0, row);
        grid.add(v, 1, row);

        return row + 1;
    }

    private void onClose() {
        ((Stage) getScene().getWindow()).close();
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

    private String hotelFooter() {
        String street = settings.hotelStreet();
        String city = settings.hotelCity();
        String province = settings.hotelProvince();
        String postalCode = settings.hotelPostalCode();

        StringBuilder address = new StringBuilder();

        if (street != null && !street.isBlank()) {
            address.append(street);
        }

        if (city != null && !city.isBlank()) {
            if (!address.isEmpty()) address.append(", ");
            address.append(city);
        }

        if (province != null && !province.isBlank()) {
            if (!address.isEmpty()) address.append(", ");
            address.append(province);
        }

        if (postalCode != null && !postalCode.isBlank()) {
            if (!address.isEmpty()) address.append(", ");
            address.append(postalCode);
        }

        return settings.hotelName() + "\n" +
                "Guest Services\n" +
                settings.hotelEmail() + "\n" +
                settings.hotelPhone() + "\n" +
                address;
    }

    private Properties loadProperties() throws IOException {
        Properties properties = new Properties();

        try (InputStream inputStream = EmailService.class.getClassLoader().getResourceAsStream("config/application.properties")) {

            if (inputStream == null) {
                throw new IOException("config/application.properties not found");
            }

            properties.load(inputStream);
        }

        return properties;
    }

    private void confirmReservation() throws SQLException, IOException {
        Reservation reservation = data.reservation();

        String name = data.getGuestName();
        String checkIn = data.getArrival();
        String checkOut = data.getDeparture();
        long guests = data.getGuests();
        String roomName = data.getRoomNumber() + " · " + data.getRoomType();
        Payment payment = data.getPayment();

        Properties properties = loadProperties();
        String base_url = properties.getProperty("base.url");
        String guest_email = data.getGuestEmail();

        Connection connection = Conn.getConnection();

        connection.setAutoCommit(false);

        try {
            try (PreparedStatement statement = connection.prepareStatement("insert into bookings (booking_id, reservation_id, guest_id, room_id, booking_date, check_in_date, check_out_date, number_of_guests, booking_status, total_cost, selected_packages, requests) values(?,?,?,?,?,?,?,?,?::bookingstatus,?,?,?)")) {
                String bookingId = String.format("BK-%s-%04d", LocalDate.now().format(java.time.format.DateTimeFormatter.ofPattern("yyMM")), reservation.getId() % 10000);
                statement.setString(1, bookingId);
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

            try (PreparedStatement statement = connection.prepareStatement("update reservations set reservationstatus = ?::reservationstatus where id = ?")) {
                statement.setString(1, "CONFIRMED");
                statement.setLong(2, reservation.getId());

                statement.execute();
            }

            try (PreparedStatement statement = connection.prepareStatement("insert into notifications (type, title, message, reservationid, guestid, payment_id) values (?::notificationtype, ?, ?, ?, ?, ?)")) {
                statement.setString(1, NotificationType.reservation_confirmation.name());
                statement.setString(2, "Reservation Confirmation");
                statement.setString(3, "Reservation " + reservation.getReservationid() + " has been confirmed.");
                statement.setLong(4, reservation.getId());
                statement.setLong(5, data.getGuestId());
                statement.setLong(6, data.getPaymentId());

                statement.execute();
            }

            connection.commit();
        }

        catch (SQLException e) {
            connection.rollback();
            throw e;
        }

        finally {
            connection.setAutoCommit(true);
            connection.close();
        }

        if (settings == null || settings.notifyPaymentConfirmation()) {
            assert settings != null;
            String emailSubject = settings.hotelName() + " — Reservation Confirmation";

            assert payment != null;
            String emailMessage = "Dear " + name + ",\n\n" +
                    "We are delighted to confirm your reservation at " + settings.hotelName() + ".\n\n" +
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
                    "Check-in begins at " + settings.checkInTime() + ". Please present your Reservation ID and a valid form of identification at reception.\n\n" +
                    "If your travel plans change or you have any special requests, please contact us ahead of your arrival and we will do our best to accommodate you.\n\n" +
                    "We look forward to hosting you at " + settings.hotelName() + ".\n\n" +
                    "Kind regards,\n\n" +
                    hotelFooter();

            new Thread(() -> {
                try {
                    EmailService.sendEmail(guest_email, emailSubject, emailMessage, name);
                }

                catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        }

        onClose();
    }

    private void cancelReservation() throws SQLException, IOException {
        Reservation reservation = data.reservation();

        String name = data.getGuestName();
        String checkIn = data.getArrival();
        String checkOut = data.getDeparture();

        Properties properties = loadProperties();
        String base_url = properties.getProperty("base.url");
        String guest_email = data.getGuestEmail();

        Connection connection = Conn.getConnection();

        connection.setAutoCommit(false);

        try {
            try (PreparedStatement statement = connection.prepareStatement("update reservations set reservationstatus = 'CANCELLED' where id = ?")) {
                statement.setLong(1, reservation.getId());
                statement.execute();
            }

            try (PreparedStatement statement = connection.prepareStatement("update rooms set roomstatus = 'available' where roomid = ?")) {
                statement.setLong(1, data.getRoomId());
                statement.execute();
            }

            try (PreparedStatement statement = connection.prepareStatement("update roomhistory set status = 'available', updated_at = now() where roomid = ? and status_date = date_trunc('day', now() at time zone 'Africa/Johannesburg') at time zone 'UTC'")){
                statement.setLong(1, data.getRoomId());
                statement.execute();
            }

            try (PreparedStatement statement = connection.prepareStatement("insert into notifications (type, title, message, reservationid, guestid, payment_id) values (?::notificationtype, ?, ?, ?, ?, ?)")) {
                statement.setString(1, NotificationType.reservation_cancelled.name());
                statement.setString(2, "Reservation Cancellation notification");
                statement.setString(3, "Reservation " + reservation.getReservationid() + " cancelled.");
                statement.setLong(4, reservation.getId());
                statement.setLong(5, data.getGuestId());
                statement.setLong(6, data.getPaymentId());

                statement.execute();
            }

            try (PreparedStatement statement = connection.prepareStatement("update payments set paymentstatus = 'cancelled' where reservation_id = ?")) {
                statement.setObject(1, reservation.getReservationid());
                statement.execute();
            }

            connection.commit();
        }

        catch (SQLException e) {
            connection.rollback();
            throw e;
        }

        finally {
            connection.setAutoCommit(true);
            connection.close();
        }

        RedisClient client = ConnectJedis.connect();
        client.del("rooms");
        client.del("availableRooms");

        if (settings == null || settings.notifyReservationCancelled()) {
            assert settings != null;
            String emailSubject = settings.hotelName() + " — Reservation Cancelled";

            String emailMessage = "Dear " + name + "\n\n" +
                    "We regret to inform you that your reservation at " + settings.hotelName() + " has been cancelled because the required payment was not received within the allocated payment period.\n\n" +
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
                    "We hope to welcome you to " + settings.hotelName() + " in the future.\n\n" +
                    "Kind regards,\n\n" +
                    hotelFooter();

            new Thread(() -> {
                try {
                    EmailService.sendEmail(guest_email, emailSubject, emailMessage, name);
                }

                catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        }

        onClose();
    }

    private void rejectReservation() throws SQLException, IOException {
        Reservation reservation = data.reservation();

        Connection connection = Conn.getConnection();

        connection.setAutoCommit(false);

        try {
            try (PreparedStatement statement = connection.prepareStatement("update reservations set reservationstatus = ?::reservationstatus where id = ?")) {
                statement.setString(1, ReservationStatus.REJECTED.name());
                statement.setLong(2, reservation.getId());

                statement.execute();
            }

            try (PreparedStatement statement = connection.prepareStatement("update rooms set roomstatus = 'available' where roomid = ?")) {
                statement.setLong(1, data.getRoomId());
                statement.execute();
            }

            connection.commit();
        }

        catch (SQLException e) {
            connection.rollback();
            throw e;
        }

        finally {
            connection.setAutoCommit(true);
            connection.close();
        }

        RedisClient client = ConnectJedis.connect();
        client.del("rooms");
        client.del("availableRooms");

        onClose();
    }
}