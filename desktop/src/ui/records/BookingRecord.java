package ui.records;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcons;
import enums.BookingStatus;
import enums.ReservationStatus;
import enums.RoomStatus;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.print.PrinterJob;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import data.BookingData;
import models.Settings;
import utils.Assignment;
import utils.Conn;

import java.io.IOException;
import java.sql.*;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class BookingRecord extends VBox {

    private static final AtomicInteger inspectionIndex = new AtomicInteger();

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

        Button printFolio = new Button("Print PortFolio");
        printFolio.getStyleClass().add("registry-button");

        FontAwesomeIcon printerIcon = new FontAwesomeIcon();
        printerIcon.setIcon(FontAwesomeIcons.PRINT);
        printerIcon.setSize("11");
        printerIcon.setFill(Color.web("#9a9080"));
        printFolio.setGraphic(printerIcon);

        printFolio.setOnAction(_ -> print());

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

        Settings settings = data.getSettings();

        Label hotel = new Label(settings == null ? "" : settings.getHotelName());
        hotel.setFont(Font.font("Georgia", FontWeight.BOLD, 20));
        hotel.setTextFill(Color.web("#1a1814"));

        Label address = new Label(settings == null ? "" : settings.getHotelStreet() + ", " + settings.getHotelCity() + ", " + settings.getHotelProvince() + ", " + settings.getHotelPostalCode());
        address.setFont(Font.font("Georgia", 11));
        address.setTextFill(Color.web("#444444"));

        Label contact = new Label(settings == null ? "" : settings.getHotelPhone() + " · " + settings.getHotelEmail());
        contact.setFont(Font.font("Georgia", 11));
        contact.setTextFill(Color.web("#444444"));

        Separator top = new Separator();

        Label title = new Label("BOOKING PORTFOLIO");
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

        row = addPrintRow(grid, row, "Booking ID",  data.getBookingId());
        row = addPrintRow(grid, row, "Reference",   data.getReference());
        row = addPrintRow(grid, row, "Guest",       data.getGuestName());
        row = addPrintRow(grid, row, "Room",        data.getRoomNumber() + " · " + data.getRoomType());
        row = addPrintRow(grid, row, "Check-In",    data.getCheckIn());
        row = addPrintRow(grid, row, "Check-Out",   data.getCheckOut());
        row = addPrintRow(grid, row, "Nights",      String.valueOf(data.getNights()));
        row = addPrintRow(grid, row, "Guests",      String.valueOf(data.getGuests()));
        row = addPrintRow(grid, row, "Room Rate",   data.getRoomRate());
        row = addPrintRow(grid, row, "Total Cost",  data.getTotal());
        row = addPrintRow(grid, row, "Status",      data.getStatus());
        row = addPrintRow(grid, row, "Packages",    data.getPackages());
        addPrintRow(grid, row, "Requests",          data.getRequests());

        Separator bottom = new Separator();

        Label footer = new Label(settings == null ? "" : settings.getHotelName() + " · " + settings.getHotelPhone() + " · " + settings.getHotelEmail());
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

            try (PreparedStatement statement = connection.prepareStatement("update reservations set reservationstatus = ?::reservationstatus where id = ?")) {
                statement.setObject(1, ReservationStatus.EXPIRED.name(), java.sql.Types.OTHER);
                statement.setLong(2, Objects.requireNonNull(data.getReservation()).getId());

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

        int index = inspectionIndex.getAndIncrement();

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

            try (PreparedStatement statement = connection.prepareStatement("insert into servicetasks(service_id, room_id, assigned_to, scheduled_for, completed_at) values (2, ?, ?, ?, now())")) {
                statement.setLong(1, data.getRoom().getRoomid());
                statement.setObject(2, Assignment.pick("supervisors", index, data.getRoom().getFloornumber()), Types.BIGINT);
                statement.setObject(3, OffsetDateTime.now(ZoneOffset.UTC).plusMinutes(20));

                statement.executeUpdate();
            }

            try (PreparedStatement statement = connection.prepareStatement("insert into servicetasks(service_id, room_id, assigned_to, scheduled_for, completed_at) values (1, ?, ?, ?, now())")) {
                statement.setLong(1, data.getRoom().getRoomid());
                statement.setObject(2, Assignment.pick("supervisors", index, data.getRoom().getFloornumber()), Types.BIGINT);
                statement.setObject(3, OffsetDateTime.now(ZoneOffset.UTC).plusMinutes(20));

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