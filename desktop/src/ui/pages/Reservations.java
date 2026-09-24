package ui.pages;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcons;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.print.PrinterJob;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import models.Guest;
import models.Reservation;
import models.Room;
import records.Payment;
import data.ReservationsData;
import ui.records.ReservationRecord;

import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.time.LocalDate;
import models.Settings;

public class Reservations extends VBox {
    private final TableView<ReservationsData> reservation_displayer;
    private final ObservableList<ReservationsData> allData = FXCollections.observableArrayList();
    private final Label count;

    public Reservations() {
        HBox topRow = new HBox();
        topRow.getStyleClass().add("registry-button-row");
        topRow.setAlignment(Pos.CENTER_LEFT);
        topRow.setSpacing(8);
        topRow.setPadding(new Insets(16, 20, 12, 20));
        topRow.setMaxWidth(Double.MAX_VALUE);

        HBox left_panel = new HBox();
        left_panel.setSpacing(8);
        left_panel.setAlignment(Pos.CENTER_LEFT);

        Button print = new Button("Print");
        print.getStyleClass().add("registry-button");

        FontAwesomeIcon printIcon = new FontAwesomeIcon();
        printIcon.setIcon(FontAwesomeIcons.PRINT);
        printIcon.setSize("11");
        printIcon.setFill(Color.web("#9a9080"));
        print.setGraphic(printIcon);

        print.setOnAction(_ -> printSelected());

        Button export = new Button("Export");
        export.getStyleClass().add("registry-button");

        FontAwesomeIcon exportIcon = new FontAwesomeIcon();
        exportIcon.setIcon(FontAwesomeIcons.ARROW_DOWN);
        exportIcon.setSize("11");
        exportIcon.setFill(Color.web("#9a9080"));
        export.setGraphic(exportIcon);

        export.setOnAction(_ -> exportCsv());

        left_panel.getChildren().addAll(print, export);

        HBox right_panel = new HBox();
        right_panel.setAlignment(Pos.CENTER_RIGHT);
        HBox.setHgrow(right_panel, Priority.ALWAYS);

        count = new Label("0 reservations");
        count.getStyleClass().add("registry-count");
        right_panel.getChildren().add(count);

        topRow.getChildren().addAll(left_panel, right_panel);

        this.reservation_displayer = new TableView<>();
        this.reservation_displayer.getStyleClass().add("registry-table");
        this.reservation_displayer.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        this.reservation_displayer.setMaxWidth(Double.MAX_VALUE);
        this.reservation_displayer.setMaxHeight(Double.MAX_VALUE);
        VBox.setVgrow(this.reservation_displayer, Priority.ALWAYS);

        TableColumn<ReservationsData, String> reservationId = new TableColumn<>("Reservation ID");
        TableColumn<ReservationsData, String> guestName = new TableColumn<>("Guest");
        TableColumn<ReservationsData, String> roomNumber = new TableColumn<>("Room");
        TableColumn<ReservationsData, String> roomType = new TableColumn<>("Type");
        TableColumn<ReservationsData, String> arrival = new TableColumn<>("Arrival");
        TableColumn<ReservationsData, String> departure = new TableColumn<>("Departure");
        TableColumn<ReservationsData, Number> nights = new TableColumn<>("Nights");
        TableColumn<ReservationsData, String> total = new TableColumn<>("Total");
        TableColumn<ReservationsData, Number> guests = new TableColumn<>("Guests");
        TableColumn<ReservationsData, String> bookedOn = new TableColumn<>("Booked On");

        reservationId.setCellValueFactory(new PropertyValueFactory<>("reservationId"));
        guestName.setCellValueFactory(new PropertyValueFactory<>("guestName"));
        roomNumber.setCellValueFactory(new PropertyValueFactory<>("roomNumber"));
        roomType.setCellValueFactory(new PropertyValueFactory<>("roomType"));
        arrival.setCellValueFactory(new PropertyValueFactory<>("arrival"));
        departure.setCellValueFactory(new PropertyValueFactory<>("departure"));
        nights.setCellValueFactory(new PropertyValueFactory<>("nights"));
        total.setCellValueFactory(new PropertyValueFactory<>("total"));
        guests.setCellValueFactory(new PropertyValueFactory<>("guests"));
        bookedOn.setCellValueFactory(new PropertyValueFactory<>("bookedOn"));

        this.reservation_displayer.getColumns().addAll(reservationId, guestName, roomNumber, roomType, arrival, departure, nights, total, guests, bookedOn);

        reservation_displayer.setOnMouseClicked(event -> {
            Node target = (Node) event.getTarget();
            TableRow<?> row = null;

            while (target != null) {
                if (target instanceof TableRow) {
                    row = (TableRow<?>) target;
                    break;
                }
                target = target.getParent();
            }

            if (row == null || row.getItem() == null) {
                reservation_displayer.getSelectionModel().clearSelection();
                return;
            }

            if (event.getClickCount() == 2) {
                ReservationsData data = (ReservationsData) row.getItem();
                try {
                    openReservationModal(data);
                }

                catch (IOException | SQLException e) {
                    throw new RuntimeException(e);
                }
                reservation_displayer.getSelectionModel().clearSelection();
            }
        });

        getChildren().addAll(topRow, this.reservation_displayer);
        VBox.setVgrow(this, Priority.ALWAYS);
        setMaxWidth(Double.MAX_VALUE);
        setMaxHeight(Double.MAX_VALUE);
    }

    public void setData(Reservation[] reservations, Room[] rooms, Guest[] guests, Payment[] payments, Settings settings) {
        if (reservations == null || rooms == null || guests == null || payments == null) return;

        ObservableList<ReservationsData> newData = FXCollections.observableArrayList();

        for (Reservation reservation : reservations) {
            newData.add(new ReservationsData(reservation, rooms, guests, payments, settings));
        }

        Platform.runLater(() -> {
            if (newData.equals(allData)) return;

            allData.setAll(newData);

            if (reservation_displayer.getItems() != allData) {
                reservation_displayer.setItems(allData);
            }

            count.setText(allData.size() + (allData.size() == 1 ? " reservation" : " reservations"));
        });
    }

    private void openReservationModal(ReservationsData data) throws IOException, SQLException {
        Stage modal = new Stage();
        modal.initModality(Modality.APPLICATION_MODAL);
        modal.initStyle(StageStyle.TRANSPARENT);
        modal.initOwner(this.getScene().getWindow());

        ReservationRecord record = new ReservationRecord(data);

        StackPane root = new StackPane(record);
        root.setStyle("-fx-background-color: transparent; -fx-padding: 40;");

        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);
        scene.getStylesheets().addAll(this.getScene().getStylesheets());

        modal.setScene(scene);
        modal.showAndWait();
    }

    private void printSelected() {
        if (allData.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "No reservations to print.", ButtonType.OK);
            alert.setHeaderText(null);
            alert.initOwner(this.getScene().getWindow());
            alert.showAndWait();
            return;
        }

        ReservationsData selected = reservation_displayer.getSelectionModel().getSelectedItem();

        VBox slip = selected != null ? buildPrintSlip(selected) : buildPrintSlip();

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

    private VBox buildPrintSlip(ReservationsData data) {
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

        Label footer = new Label(settings == null ? "" : settings.getHotelName() + " · " + settings.getHotelPhone() + " · " + settings.getHotelEmail());
        footer.setFont(Font.font("Georgia", 9));
        footer.setTextFill(Color.web("#666666"));

        slip.getChildren().addAll(hotel, address, contact, top, title, grid, bottom, footer);

        return slip;
    }

    private VBox buildPrintSlip() {
        VBox slip = new VBox(12);
        slip.setPadding(new Insets(40));
        slip.setStyle("-fx-background-color: #ffffff;");
        slip.setPrefWidth(595);
        slip.setMinWidth(595);
        slip.setMaxWidth(595);

        Settings settings = allData.isEmpty() ? null : allData.getFirst().getSettings();

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

        Label title = new Label("RESERVATIONS LIST");
        title.setFont(Font.font("Georgia", FontWeight.BOLD, 14));
        title.setTextFill(Color.web("#1a1814"));

        GridPane grid = new GridPane();
        grid.setHgap(12);
        grid.setVgap(6);
        grid.setMaxWidth(Double.MAX_VALUE);

        String[] headers = {"Reservation ID", "Guest", "Room", "Type", "Arrival", "Departure", "Nights", "Guests", "Total"};

        for (int c = 0; c < headers.length; c++) {
            Label h = new Label(headers[c].toUpperCase());
            h.setFont(Font.font("Georgia", FontWeight.BOLD, 9));
            h.setTextFill(Color.web("#444444"));
            grid.add(h, c, 0);
        }

        int row = 1;

        for (ReservationsData d : allData) {
            Label id      = listCell(d.getReservationId());
            Label guest   = listCell(d.getGuestName());
            Label room    = listCell(d.getRoomNumber());
            Label type    = listCell(d.getRoomType());
            Label arrival = listCell(d.getArrival());
            Label depart  = listCell(d.getDeparture());
            Label nights  = listCell(String.valueOf(d.getNights()));
            Label guests  = listCell(String.valueOf(d.getGuests()));
            Label total   = listCell(d.getTotal());

            grid.add(id,      0, row);
            grid.add(guest,   1, row);
            grid.add(room,    2, row);
            grid.add(type,    3, row);
            grid.add(arrival, 4, row);
            grid.add(depart,  5, row);
            grid.add(nights,  6, row);
            grid.add(guests,  7, row);
            grid.add(total,   8, row);

            row++;
        }

        Separator bottom = new Separator();

        Label footer = new Label(settings == null ? "" : settings.getHotelName() + " · " + settings.getHotelPhone() + " · " + settings.getHotelEmail());
        footer.setFont(Font.font("Georgia", 9));
        footer.setTextFill(Color.web("#666666"));

        slip.getChildren().addAll(hotel, address, contact, top, title, grid, bottom, footer);

        return slip;
    }

    private Label listCell(String value) {
        Label v = new Label(value == null ? "—" : value);
        v.setFont(Font.font("Georgia", 9));
        v.setTextFill(Color.web("#1a1814"));
        return v;
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

    private void exportCsv() {
        if (allData.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "No reservations to export.", ButtonType.OK);
            alert.setHeaderText(null);
            alert.initOwner(this.getScene().getWindow());
            alert.showAndWait();
            return;
        }

        Path dir = Paths.get(System.getProperty("user.dir")).getParent().resolve("resources").resolve("records");
        Path file = dir.resolve("reservations-" + LocalDate.now() + ".csv");

        try {
            Files.createDirectories(dir);

            try (Writer w = Files.newBufferedWriter(file)) {
                w.write("reservation_id,guest,room,type,arrival,departure,nights,guests,total,booked_on\n");

                for (ReservationsData d : allData) {
                    w.write(csv(d.getReservationId()) + "," +
                            csv(d.getGuestName()) + "," +
                            csv(d.getRoomNumber()) + "," +
                            csv(d.getRoomType()) + "," +
                            csv(d.getArrival()) + "," +
                            csv(d.getDeparture()) + "," +
                            d.getNights() + "," +
                            d.getGuests() + "," +
                            csv(d.getTotal()) + "," +
                            csv(d.getBookedOn()) + "\n");
                }
            }

            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Exported to " + file.toAbsolutePath(), ButtonType.OK);
            alert.setHeaderText(null);
            alert.initOwner(this.getScene().getWindow());
            alert.showAndWait();
        }

        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String csv(String s) {
        if (s == null) return "";

        String v = s.replace("\"", "\"\"");

        if (v.contains(",") || v.contains("\"") || v.contains("\n")) {
            return "\"" + v + "\"";
        }

        return v;
    }
}