package views.dashboard;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcons;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import models.Booking;
import models.Guest;
import models.Reservation;
import models.Room;
import utils.BookingData;

import java.io.IOException;

public class Bookings extends VBox {
    private Booking[] bookings;
    private final TableView<BookingData> booking_displayer;
    private final ObservableList<BookingData> allData = FXCollections.observableArrayList();

    public Bookings() {
        HBox topRow = new HBox();
        topRow.getStyleClass().add("registry-button-row");
        topRow.setAlignment(Pos.CENTER_LEFT);
        topRow.setSpacing(8);
        topRow.setPadding(new Insets(16, 20, 12, 20));
        topRow.setMaxWidth(Double.MAX_VALUE);

        HBox left_panel = new HBox();
        left_panel.setSpacing(8);
        left_panel.setAlignment(Pos.CENTER_LEFT);

        Button newBooking = new Button("New Booking");
        newBooking.getStyleClass().add("registry-button-primary");

        FontAwesomeIcon plusIcon = new FontAwesomeIcon();
        plusIcon.setIcon(FontAwesomeIcons.PLUS);
        plusIcon.setSize("11");
        plusIcon.setFill(Color.web("#0a0908"));
        newBooking.setGraphic(plusIcon);

        Label divider = new Label("|");
        divider.getStyleClass().add("registry-divider");

        Button clear = new Button("Clear");
        clear.getStyleClass().add("registry-button");

        FontAwesomeIcon refreshIcon = new FontAwesomeIcon();
        refreshIcon.setIcon(FontAwesomeIcons.REFRESH);
        refreshIcon.setSize("11");
        refreshIcon.setFill(Color.web("#9a9080"));
        clear.setGraphic(refreshIcon);

        left_panel.getChildren().addAll(newBooking, divider, clear);

        HBox right_panel = new HBox();
        right_panel.setAlignment(Pos.CENTER_RIGHT);
        HBox.setHgrow(right_panel, Priority.ALWAYS);

        Label count = new Label("0 bookings");
        count.getStyleClass().add("registry-count");
        right_panel.getChildren().add(count);

        topRow.getChildren().addAll(left_panel, right_panel);

        HBox filter_row = new HBox();
        filter_row.getStyleClass().add("registry-filter-row");
        filter_row.setSpacing(16);
        filter_row.setAlignment(Pos.CENTER_LEFT);
        filter_row.setPadding(new Insets(0, 20, 12, 20));
        filter_row.setMaxWidth(Double.MAX_VALUE);

        HBox search_panel = new HBox();
        search_panel.setSpacing(8);
        search_panel.setAlignment(Pos.CENTER_LEFT);

        Label searchLabel = new Label("SEARCH:");
        searchLabel.getStyleClass().add("registry-field-label");

        FontAwesomeIcon searchIcon = new FontAwesomeIcon();
        searchIcon.setIcon(FontAwesomeIcons.SEARCH);
        searchIcon.setSize("11");
        searchIcon.setFill(Color.web("#6a6058"));

        TextField searchField = new TextField();
        searchField.setPromptText("Guest name, reference, room…");
        searchField.getStyleClass().add("registry-input");
        searchField.setPrefWidth(280);

        search_panel.getChildren().addAll(searchLabel, searchIcon, searchField);

        HBox filter_panel = new HBox();
        filter_panel.setSpacing(8);
        filter_panel.setAlignment(Pos.CENTER_LEFT);

        Label filterLabel = new Label("STATUS:");
        filterLabel.getStyleClass().add("registry-field-label");

        ComboBox<String> filterCombo = new ComboBox<>();
        filterCombo.getStyleClass().add("registry-combo");
        filterCombo.setPrefWidth(160);
        filterCombo.setValue("All");

        filter_panel.getChildren().addAll(filterLabel, filterCombo);

        Label divider2 = new Label("|");
        divider2.getStyleClass().add("registry-divider");

        filter_row.getChildren().addAll(search_panel, divider2, filter_panel);

        this.booking_displayer = new TableView<>();
        this.booking_displayer.getStyleClass().add("registry-table");
        this.booking_displayer.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        this.booking_displayer.setMaxWidth(Double.MAX_VALUE);
        this.booking_displayer.setMaxHeight(Double.MAX_VALUE);
        VBox.setVgrow(this.booking_displayer, Priority.ALWAYS);

        TableColumn<BookingData, String> bookingId = new TableColumn<>("Booking ID");
        TableColumn<BookingData, String> guestName = new TableColumn<>("Guest");
        TableColumn<BookingData, String> roomNumber = new TableColumn<>("Room");
        TableColumn<BookingData, String> roomType = new TableColumn<>("Type");
        TableColumn<BookingData, String> checkIn = new TableColumn<>("Check-in");
        TableColumn<BookingData, String> checkOut = new TableColumn<>("Check-out");
        TableColumn<BookingData, Number> nights = new TableColumn<>("Nights");
        TableColumn<BookingData, String> guests = new TableColumn<>("Guests");
        TableColumn<BookingData, String> reference = new TableColumn<>("Reference");
        TableColumn<BookingData, String> total = new TableColumn<>("Total");
        TableColumn<BookingData, String> packages = new TableColumn<>("Packages");
        TableColumn<BookingData, String> requests = new TableColumn<>("Requests");
        TableColumn<BookingData, String> status = new TableColumn<>("Status");

        bookingId.setCellValueFactory(new PropertyValueFactory<>("bookingId"));
        guestName.setCellValueFactory(new PropertyValueFactory<>("guestName"));
        roomNumber.setCellValueFactory(new PropertyValueFactory<>("roomNumber"));
        roomType.setCellValueFactory(new PropertyValueFactory<>("roomType"));
        checkIn.setCellValueFactory(new PropertyValueFactory<>("checkIn"));
        checkOut.setCellValueFactory(new PropertyValueFactory<>("checkOut"));
        nights.setCellValueFactory(new PropertyValueFactory<>("nights"));
        guests.setCellValueFactory(new PropertyValueFactory<>("guests"));
        reference.setCellValueFactory(new PropertyValueFactory<>("reference"));
        total.setCellValueFactory(new PropertyValueFactory<>("total"));
        packages.setCellValueFactory(new PropertyValueFactory<>("packages"));
        requests.setCellValueFactory(new PropertyValueFactory<>("requests"));
        status.setCellValueFactory(new PropertyValueFactory<>("status"));

        this.booking_displayer.getColumns().addAll(
                bookingId, guestName, roomNumber, roomType,
                checkIn, checkOut, nights, guests,
                reference, total, packages, requests, status
        );

        booking_displayer.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                Node target = (Node) event.getTarget();
                TableRow<?> row = null;

                while (target != null) {
                    if (target instanceof TableRow) {
                        row = (TableRow<?>) target;
                        break;
                    }
                    target = target.getParent();
                }

                if (row != null && row.getItem() != null) {
                    BookingData data = (BookingData) row.getItem();
                    try {
                        openBookingModal(data);
                    }

                    catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    booking_displayer.getSelectionModel().clearSelection();
                }
            }
        });

        getChildren().addAll(topRow, filter_row, this.booking_displayer);
        VBox.setVgrow(this, Priority.ALWAYS);
        setMaxWidth(Double.MAX_VALUE);
        setMaxHeight(Double.MAX_VALUE);
    }

    public void setData(Booking[] bookings, Room[] rooms, Guest[] guests, Reservation[] reservations) {
        if (bookings == null || rooms == null || guests == null || reservations == null) return;

        this.bookings = bookings;

        ObservableList<BookingData> newData = FXCollections.observableArrayList();

        for (Booking booking : bookings) {
            newData.add(new BookingData(booking, rooms, guests, reservations));
        }

        Platform.runLater(() -> {
            if (newData.equals(allData)) return;

            allData.setAll(newData);

            if (booking_displayer.getItems() != allData) {
                booking_displayer.setItems(allData);
            }
        });
    }

    private void openBookingModal(BookingData data) throws IOException {
        Stage modal = new Stage();
        modal.initModality(Modality.APPLICATION_MODAL);
        modal.initStyle(StageStyle.TRANSPARENT);
        modal.initOwner(this.getScene().getWindow());

        Runnable close = modal::close;

        BookingRecord record = new BookingRecord(data, close);

        StackPane root = new StackPane(record);
        root.setStyle("-fx-background-color: transparent; -fx-padding: 40;");

        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);
        scene.getStylesheets().addAll(this.getScene().getStylesheets());

        modal.setScene(scene);
        modal.showAndWait();
    }
}