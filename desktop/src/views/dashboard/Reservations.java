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
import models.Guest;
import models.Reservation;
import models.Room;
import records.Payment;
import utils.ReservationsData;

public class Reservations extends VBox {
    private Reservation[] reservations;
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

        Button newReservation = new Button("New Reservation");
        newReservation.getStyleClass().add("registry-button-primary");

        FontAwesomeIcon plusIcon = new FontAwesomeIcon();
        plusIcon.setIcon(FontAwesomeIcons.PLUS);
        plusIcon.setSize("11");
        plusIcon.setFill(Color.web("#0a0908"));
        newReservation.setGraphic(plusIcon);

        Label divider = new Label("|");
        divider.getStyleClass().add("registry-divider");

        Button print = new Button("Print");
        print.getStyleClass().add("registry-button");

        FontAwesomeIcon printIcon = new FontAwesomeIcon();
        printIcon.setIcon(FontAwesomeIcons.PRINT);
        printIcon.setSize("11");
        printIcon.setFill(Color.web("#9a9080"));
        print.setGraphic(printIcon);

        Button export = new Button("Export");
        export.getStyleClass().add("registry-button");

        FontAwesomeIcon exportIcon = new FontAwesomeIcon();
        exportIcon.setIcon(FontAwesomeIcons.ARROW_DOWN);
        exportIcon.setSize("11");
        exportIcon.setFill(Color.web("#9a9080"));
        export.setGraphic(exportIcon);

        left_panel.getChildren().addAll(newReservation, divider, print, export);

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
                    ReservationsData data = (ReservationsData) row.getItem();
                    openReservationModal(data);
                    reservation_displayer.getSelectionModel().clearSelection();
                }
            }
        });

        getChildren().addAll(topRow, this.reservation_displayer);
        VBox.setVgrow(this, Priority.ALWAYS);
        setMaxWidth(Double.MAX_VALUE);
        setMaxHeight(Double.MAX_VALUE);
    }

    public void setData(Reservation[] reservations, Room[] rooms, Guest[] guests, Payment[] payments) {
        if (reservations == null || rooms == null || guests == null || payments == null) return;

        this.reservations = reservations;

        ObservableList<ReservationsData> newData = FXCollections.observableArrayList();

        for (Reservation reservation : reservations) {
            newData.add(new ReservationsData(reservation, rooms, guests, payments));
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

    private void openReservationModal(ReservationsData data) {
        Stage modal = new Stage();
        modal.initModality(Modality.APPLICATION_MODAL);
        modal.initStyle(StageStyle.TRANSPARENT);
        modal.initOwner(this.getScene().getWindow());

        Runnable close = () -> modal.close();

        ReservationRecord record = new ReservationRecord(
                data,
                close,
                () -> { },
                () -> { },
                () -> { },
                () -> { }
        );

        StackPane root = new StackPane(record);
        root.setStyle("-fx-background-color: transparent; -fx-padding: 40;");

        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);
        scene.getStylesheets().addAll(this.getScene().getStylesheets());

        modal.setScene(scene);
        modal.showAndWait();
    }
}