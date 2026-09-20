package ui.records;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcons;
import enums.BookingStatus;
import enums.ReservationStatus;
import enums.RoomStatus;

import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import data.BookingData;
import models.Settings;
import utils.Conn;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

public class BookingRecord extends VBox {

    private final Connection connection = Conn.getConnection();
    private final BookingData data;
    private final Runnable onClose;

    public BookingRecord(BookingData data, Runnable onClose) throws IOException, SQLException {
        this.data = data;
        this.onClose = onClose;
        getStyleClass().add("booking-record");

        HBox header = new HBox();
        header.getStyleClass().add("booking-record-header");
        header.setAlignment(Pos.CENTER_LEFT);

        Label title = new Label("Booking — " + data.getBookingId());
        title.getStyleClass().add("booking-record-title");
        HBox.setHgrow(title, Priority.ALWAYS);
        title.setMaxWidth(Double.MAX_VALUE);

        Button closeX = new Button("✕");
        closeX.getStyleClass().add("booking-record-close");
        closeX.setOnAction(_ -> onClose.run());

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
        stay.getChildren().addAll(stayTitle, row("Booking ID", data.getBookingId()), row("Reference", data.getReference()), row("Check-In", data.getCheckIn()), row("Check-Out", data.getCheckOut()), row("Nights", String.valueOf(data.getNights())), row("Guests", String.valueOf(data.getGuests())));

        VBox pricing = new VBox();
        pricing.getStyleClass().add("booking-record-section");
        HBox.setHgrow(pricing, Priority.ALWAYS);
        Label pricingTitle = new Label("PRICING");
        pricingTitle.getStyleClass().add("booking-record-section-title");
        pricing.getChildren().addAll(pricingTitle, row("Room Rate", data.getRoomRate()), row("Nights", String.valueOf(data.getNights())), row("Total Cost", data.getTotal()));

        Label pkgTitle = new Label("PACKAGES");
        pkgTitle.getStyleClass().add("booking-record-section-title");
        Label pkg = new Label(data.getPackages());
        pkg.getStyleClass().add("booking-record-value");
        pkg.setWrapText(true);

        VBox pkgBox = new VBox(pkgTitle, pkg);
        pkgBox.getStyleClass().add("booking-record-block");
        pricing.getChildren().add(pkgBox);

        body.getChildren().addAll(stay, pricing);

        Label reqTitle = new Label("SPECIAL REQUESTS");
        reqTitle.getStyleClass().add("booking-record-section-title");
        Label req = new Label(data.getRequests());
        req.getStyleClass().add("booking-record-value");
        req.setWrapText(true);

        VBox reqBox = new VBox(reqTitle, req);
        reqBox.getStyleClass().add("booking-record-block");
        reqBox.setMaxWidth(Double.MAX_VALUE);

        HBox footer = new HBox();
        footer.getStyleClass().add("booking-record-footer");
        footer.setAlignment(Pos.CENTER_LEFT);

        String status = data.getStatus();

        Button printFolio = new Button("Print Folio");
        printFolio.getStyleClass().add("registry-button");

        FontAwesomeIcon printerIcon = new FontAwesomeIcon();
        printerIcon.setIcon(FontAwesomeIcons.PRINT);
        printerIcon.setSize("11");
        printerIcon.setFill(Color.web("#9a9080"));
        printFolio.setGraphic(printerIcon);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button close = new Button("Close");
        close.getStyleClass().add("registry-button-primary");
        close.setOnAction(_ -> onClose.run());

        if ("PENDING".equalsIgnoreCase(status)) {
            Button checkIn = new Button("Check In");
            checkIn.setStyle("-fx-background-color: blue; -fx-text-fill: #fff;");

            FontAwesomeIcon checkInIcon = new FontAwesomeIcon();
            checkInIcon.setIcon(FontAwesomeIcons.CHECK);
            checkInIcon.setSize("11");
            checkInIcon.setFill(Color.web("#fff"));
            checkIn.setGraphic(checkInIcon);

            footer.getChildren().addAll(printFolio, spacer, checkIn, close);
            checkIn.setOnAction(_ -> {
                try {
                    this.checkIn();
                }

                catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            });
        }

        else if ("CHECKED_IN".equalsIgnoreCase(status)) {
            Button checkOut = new Button("Check Out");
            checkOut.getStyleClass().add("registry-button");

            footer.getChildren().addAll(checkOut, printFolio, spacer, close);
            checkOut.setOnAction(_ -> {
                try {
                    this.checkOut();
                }

                catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            });
        }

        else {
            footer.getChildren().addAll(printFolio, spacer, close);
        }

        getChildren().addAll(header, new Separator(), stats, new Separator(), body, new Separator(), reqBox, new Separator(), footer);
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

    private boolean confirm(String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, message, ButtonType.OK, ButtonType.CANCEL);
        alert.setHeaderText(null);

        if (this.getScene() != null) {
            alert.initOwner(this.getScene().getWindow());
        }

        return alert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK;
    }

    private void checkIn() throws SQLException {
        LocalDate checkInDate = data.getCheckInDate();

        if (checkInDate != null && !checkInDate.equals(LocalDate.now())) {
            if (!confirm("Check-in date " + data.getCheckIn() + ", is not today. Proceed anyway?")) {
                return;
            }
        }

        Settings settings = data.getSettings();

        if (settings != null) {
            LocalTime now = LocalTime.now();
            LocalTime standardCheckIn = settings.getCheckInTime().toLocalTime();
            LocalTime earliestEarly = settings.getEarlyCheckInTime() == null ? null : settings.getEarlyCheckInTime().toLocalTime();

            if (now.isBefore(standardCheckIn)) {
                if (earliestEarly != null && now.isBefore(earliestEarly)) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Early check-in is not available before " + earliestEarly + ".", ButtonType.OK);
                    alert.setHeaderText(null);

                    if (this.getScene() != null) {
                        alert.initOwner(this.getScene().getWindow());
                    }

                    alert.showAndWait();
                    return;
                }

                String requests = data.getRequests();
                boolean requested = requests.toLowerCase().contains("early check-in") || requests.toLowerCase().contains("early check in") || requests.toLowerCase().contains("early checkin");

                if (!requested) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Check-in time is at " + standardCheckIn + ". Early check-in was not requested.", ButtonType.OK);
                    alert.setHeaderText(null);

                    if (this.getScene() != null) {
                        alert.initOwner(this.getScene().getWindow());
                    }

                    alert.showAndWait();
                    return;
                }

                if (!confirm("Early check-in was requested. Standard check-in is at " + standardCheckIn + ". Proceed with early check-in?")) {
                    return;
                }
            }
        }

        connection.setAutoCommit(false);

        try {
            try (PreparedStatement statement = connection.prepareStatement("update bookings set booking_status = ?::bookingstatus where booking_id = ?;")) {
                statement.setObject(1, BookingStatus.CHECKED_IN.name(), java.sql.Types.OTHER);
                statement.setString(2, data.getBookingId());

                statement.executeUpdate();
            }

            try (PreparedStatement statement = connection.prepareStatement("update rooms set roomstatus = ? where roomid = ?")) {
                statement.setString(1, RoomStatus.OCCUPIED.name().toLowerCase());
                statement.setLong(2, Objects.requireNonNull(data.getRoom()).getRoomid());

                statement.executeUpdate();
            }

            try (PreparedStatement statement = connection.prepareStatement("update roomhistory set status = ?, updated_at = now() where roomid = ? and status_date = ?")) {
                statement.setString(1, RoomStatus.OCCUPIED.name().toLowerCase());
                statement.setLong(2, data.getRoom().getRoomid());
                statement.setObject(3, OffsetDateTime.now(ZoneOffset.UTC).truncatedTo(ChronoUnit.DAYS));

                statement.executeUpdate();
            }

            connection.commit();
        }

        catch (SQLException e) {
            connection.rollback();
            throw e;
        }

        finally {
            connection.setAutoCommit(true);
        }

        this.onClose.run();
    }

    private void checkOut() throws SQLException {
        LocalDate checkOutDate = data.getCheckOutDate();

        if (checkOutDate != null && checkOutDate.isAfter(LocalDate.now())) {
            if (!confirm("Checkout date " + data.getCheckOut() + " has not arrived yet. Proceed anyway?")) {
                return;
            }
        }

        connection.setAutoCommit(false);

        try {
            try (PreparedStatement statement = connection.prepareStatement("update bookings set booking_status = ?::bookingstatus where booking_id = ?;")) {
                statement.setObject(1, BookingStatus.CHECKED_OUT.name(), java.sql.Types.OTHER);
                statement.setString(2, data.getBookingId());

                statement.executeUpdate();
            }

            try (PreparedStatement statement = connection.prepareStatement("update reservations set reservationstatus = ? where id = ?")) {
                statement.setObject(1, ReservationStatus.EXPIRED.name(), java.sql.Types.OTHER);
                statement.setLong(2, Objects.requireNonNull(data.getReservation()).getId());

                statement.execute();
            }

            try (PreparedStatement statement = connection.prepareStatement("update rooms set roomstatus = ? where roomid = ?")) {
                statement.setString(1, RoomStatus.AVAILABLE.name().toLowerCase());
                statement.setLong(2, Objects.requireNonNull(data.getRoom()).getRoomid());

                statement.executeUpdate();
            }

            try (PreparedStatement statement = connection.prepareStatement("update roomhistory set status = ?, updated_at = now() where roomid = ? and status_date = ?")) {
                statement.setString(1, RoomStatus.AVAILABLE.name().toLowerCase());
                statement.setLong(2, data.getRoom().getRoomid());
                statement.setObject(3, OffsetDateTime.now(ZoneOffset.UTC).truncatedTo(ChronoUnit.DAYS));

                statement.executeUpdate();
            }

            connection.commit();
        }

        catch (SQLException e) {
            connection.rollback();
            throw e;
        }

        finally {
            connection.setAutoCommit(true);
        }

        this.onClose.run();
    }
}