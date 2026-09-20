package ui.pages;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcons;
import enums.BookingStatus;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import models.*;
import models.Settings;
import records.Payment;
import records.Address;
import data.GuestRegistryData;
import ui.newModals.NewGuest;

import java.util.HashMap;
import java.util.Map;

public class GuestRegistry extends VBox {
    private Guest[] guests;
    private Address[] addresses;
    private Payment[] payments;
    private Booking[] bookings;
    private Room[] rooms;

    private Guest selectedGuest = null;
    private final TableView<GuestRegistryData> guest_displayer;

    private final ObservableList<GuestRegistryData> allData = FXCollections.observableArrayList();
    private final TextField searchField;
    private final ComboBox<String> filterCombo;
    private final Label recordsLabel;

    private VBox selectedGuestTable = null;

    public GuestRegistry() {
        getStyleClass().add("guest-registry");

        HBox button_row = new HBox();
        button_row.getStyleClass().add("registry-button-row");
        button_row.setAlignment(Pos.CENTER_LEFT);
        button_row.setSpacing(8);
        button_row.setPadding(new Insets(16, 20, 12, 20));
        button_row.setMaxWidth(Double.MAX_VALUE);

        Button newGuest = new Button("New Guest");
        newGuest.getStyleClass().add("registry-button-primary");

        FontAwesomeIcon plusIcon = new FontAwesomeIcon();
        plusIcon.setIcon(FontAwesomeIcons.PLUS);
        plusIcon.setSize("11");
        plusIcon.setFill(Color.web("#0a0908"));
        newGuest.setGraphic(plusIcon);

        newGuest.setOnAction(_ -> NewGuest.open(this));

        Label divider = new Label("|");
        divider.getStyleClass().add("registry-divider");

        Button clearFilters = new Button("Clear Filters");
        clearFilters.getStyleClass().add("registry-button");

        FontAwesomeIcon refreshIcon = new FontAwesomeIcon();
        refreshIcon.setIcon(FontAwesomeIcons.REFRESH);
        refreshIcon.setSize("11");
        refreshIcon.setFill(Color.web("#9a9080"));
        clearFilters.setGraphic(refreshIcon);

        recordsLabel = new Label("No records");
        recordsLabel.getStyleClass().add("registry-count");

        button_row.getChildren().addAll(newGuest, divider, clearFilters, recordsLabel);

        HBox filler_row = new HBox();
        filler_row.getStyleClass().add("registry-filter-row");
        filler_row.setSpacing(16);
        filler_row.setAlignment(Pos.CENTER_LEFT);
        filler_row.setPadding(new Insets(0, 20, 12, 20));
        filler_row.setMaxWidth(Double.MAX_VALUE);

        HBox search_box = new HBox();
        search_box.setSpacing(8);
        search_box.setAlignment(Pos.CENTER_LEFT);

        Label searchLabel = new Label("SEARCH");
        searchLabel.getStyleClass().add("registry-field-label");

        searchField = new TextField();
        searchField.setPromptText("Name, email, country, ID number…");
        searchField.getStyleClass().add("registry-input");
        searchField.setPrefWidth(280);

        search_box.getChildren().addAll(searchLabel, searchField);

        HBox filter_box = new HBox();
        filter_box.setSpacing(8);
        filter_box.setAlignment(Pos.CENTER_LEFT);

        Label filterLabel = new Label("COUNTRY");
        filterLabel.getStyleClass().add("registry-field-label");

        filterCombo = new ComboBox<>();
        filterCombo.getStyleClass().add("registry-combo");
        filterCombo.setPrefWidth(160);
        filterCombo.setValue("All");

        filter_box.getChildren().addAll(filterLabel, filterCombo);

        filler_row.getChildren().addAll(search_box, filter_box);

        this.guest_displayer = new TableView<>();
        this.guest_displayer.getStyleClass().add("registry-table");
        this.guest_displayer.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        this.guest_displayer.setMaxWidth(Double.MAX_VALUE);
        this.guest_displayer.setMaxHeight(Double.MAX_VALUE);
        VBox.setVgrow(this.guest_displayer, Priority.ALWAYS);

        TableColumn<GuestRegistryData, String> guest_id = new TableColumn<>("Guest ID");
        TableColumn<GuestRegistryData, String> lastName = new TableColumn<>("Last Name");
        TableColumn<GuestRegistryData, String> firstName = new TableColumn<>("First Name");
        TableColumn<GuestRegistryData, String> email = new TableColumn<>("Email");
        TableColumn<GuestRegistryData, String> phone = new TableColumn<>("Phone");
        TableColumn<GuestRegistryData, String> country = new TableColumn<>("Country");
        TableColumn<GuestRegistryData, String> city = new TableColumn<>("City");
        TableColumn<GuestRegistryData, String> idNumber = new TableColumn<>("ID Number");
        TableColumn<GuestRegistryData, String> num_stays = new TableColumn<>("Stays");
        TableColumn<GuestRegistryData, String> spent = new TableColumn<>("Spent");
        TableColumn<GuestRegistryData, String> lastStay = new TableColumn<>("Last Stay");

        guest_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        lastName.setCellValueFactory(new PropertyValueFactory<>("lastname"));
        firstName.setCellValueFactory(new PropertyValueFactory<>("firstname"));
        email.setCellValueFactory(new PropertyValueFactory<>("email"));
        phone.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        country.setCellValueFactory(new PropertyValueFactory<>("country"));
        city.setCellValueFactory(new PropertyValueFactory<>("city"));
        idNumber.setCellValueFactory(new PropertyValueFactory<>("idNumber"));
        num_stays.setCellValueFactory(new PropertyValueFactory<>("numStays"));
        spent.setCellValueFactory(new PropertyValueFactory<>("spent"));
        lastStay.setCellValueFactory(new PropertyValueFactory<>("lastStay"));

        this.guest_displayer.getColumns().addAll(guest_id, lastName, firstName, email, phone, country, city, idNumber, num_stays, spent, lastStay);

        searchField.textProperty().addListener((_, _, _) -> applyFilters());
        filterCombo.valueProperty().addListener((_, _, _) -> applyFilters());

        clearFilters.setOnAction(_ -> {
            searchField.clear();
            filterCombo.setValue("All");
            applyFilters();
        });

        getChildren().addAll(button_row, filler_row, this.guest_displayer);
        VBox.setVgrow(this, Priority.ALWAYS);
        setMaxWidth(Double.MAX_VALUE);
        setMaxHeight(Double.MAX_VALUE);

        guest_displayer.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1) {
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
                    this.selectedGuest = ((GuestRegistryData) row.getItem()).guest();
                }

                else {
                    this.selectedGuest = null;
                    guest_displayer.getSelectionModel().clearSelection();
                }
            }
        });
    }

    public void setData(Guest[] guests, Address[] addresses, Payment[] payments, Booking[] bookings, Room[] rooms, Settings settings) {
        if (guests == null || addresses == null || payments == null || bookings == null || rooms == null) return;
        this.guests = guests;
        this.addresses = addresses;
        this.payments = payments;
        this.bookings = bookings;
        this.rooms = rooms;

        Map<Guest, Boolean> map = new HashMap<Guest, Boolean>();

        ObservableList<GuestRegistryData> newData = FXCollections.observableArrayList();

        for (Guest guest : guests) {
            boolean seen = map.containsKey(guest);

            if (seen) continue;
            map.put(guest, true);

            Address guest_address = null;

            for (Address address : addresses) {
                if (address.id() == guest.getAddress_id()) guest_address = address;
            }

            if (guest_address == null) continue;

            newData.add(new GuestRegistryData(guest, guest_address, payments, bookings, settings));
        }

        ObservableList<String> countries = FXCollections.observableArrayList();

        for (GuestRegistryData d : newData) {
            String c = d.getCountry();
            if (c != null && !countries.contains(c)) {
                countries.add(c);
            }
        }

        countries.sort(String::compareTo);

        Platform.runLater(() -> {
            allData.setAll(newData);

            ObservableList<String> newItems = FXCollections.observableArrayList();
            newItems.add("All");
            newItems.addAll(countries);

            if (!newItems.equals(filterCombo.getItems())) {
                String current = filterCombo.getValue();
                filterCombo.setItems(newItems);

                if (current != null && newItems.contains(current)) {
                    filterCombo.setValue(current);
                }

                else {
                    filterCombo.setValue("All");
                }
            }

            applyFilters();

            if (selectedGuest != null) {
                this.buildSelectedGuestTable(settings);
            }

            else {
                if (selectedGuestTable != null) {
                    getChildren().remove(selectedGuestTable);
                }
            }
        });
    }

    private void applyFilters() {
        String query = searchField.getText() == null ? "" : searchField.getText().trim().toLowerCase();
        String country = filterCombo.getValue() == null ? "All" : filterCombo.getValue();

        ObservableList<GuestRegistryData> filtered = FXCollections.observableArrayList();

        for (GuestRegistryData d : allData) {
            if (!country.equals("All") && !country.equals(d.getCountry())) continue;
            if (!query.isEmpty()) {
                String haystack = (d.getId() + " " + d.getFirstname() + " " + d.getLastname() + " "
                        + d.getEmail() + " " + d.getPhoneNumber() + " " + d.getCountry() + " "
                        + d.getCity() + " " + d.getIdNumber()).toLowerCase();
                if (!haystack.contains(query)) continue;
            }
            filtered.add(d);
        }

        if (filtered.equals(guest_displayer.getItems())) {
            recordsLabel.setText(filtered.size() + (filtered.size() == 1 ? " record" : " records"));
            return;
        }

        GuestRegistryData prev = guest_displayer.getSelectionModel().getSelectedItem();

        guest_displayer.setItems(filtered);
        recordsLabel.setText(filtered.size() + (filtered.size() == 1 ? " record" : " records"));

        if (prev != null && filtered.contains(prev)) {
            guest_displayer.getSelectionModel().select(prev);
        }
    }

    private void buildSelectedGuestTable(Settings settings) {
        if (selectedGuestTable != null) {
            getChildren().remove(selectedGuestTable);
        }

        if (guests == null || addresses == null || payments == null || bookings == null || rooms == null) return;

        Address guest_address = null;
        for (Address address : addresses) {
            if (address.id() == selectedGuest.getAddress_id()) guest_address = address;
        }
        if (guest_address == null) return;

        selectedGuestTable = new VBox();
        selectedGuestTable.getStyleClass().add("guest-record");
        GuestRegistryData guest_data = new GuestRegistryData(selectedGuest, guest_address, payments, bookings, settings);

        HBox topRow = new HBox();
        topRow.getStyleClass().add("guest-record-top");
        topRow.setMaxWidth(Double.MAX_VALUE);

        Label name_label = new Label("Guest Record — " + guest_data.getFirstname() + " " + guest_data.getLastname() + " (" + guest_data.getId() + ")");
        name_label.getStyleClass().add("guest-record-title");
        HBox.setHgrow(name_label, Priority.ALWAYS);
        name_label.setMaxWidth(Double.MAX_VALUE);

        Button remove_button = new Button("Remove");
        remove_button.getStyleClass().add("guest-record-remove");

        FontAwesomeIcon trashIcon = new FontAwesomeIcon();
        trashIcon.setIcon(FontAwesomeIcons.TRASH);
        trashIcon.setSize("11");
        trashIcon.setFill(Color.web("#a84c4c"));
        remove_button.setGraphic(trashIcon);

        topRow.getChildren().addAll(name_label, remove_button);

        HBox details_row = new HBox();
        details_row.getStyleClass().add("guest-record-details");
        details_row.setMaxWidth(Double.MAX_VALUE);

        VBox personal_info = new VBox();
        personal_info.getStyleClass().add("guest-record-section");
        HBox.setHgrow(personal_info, Priority.ALWAYS);
        personal_info.setMaxWidth(Double.MAX_VALUE);

        VBox identity_info = new VBox();
        identity_info.getStyleClass().add("guest-record-section");
        HBox.setHgrow(identity_info, Priority.ALWAYS);
        identity_info.setMaxWidth(Double.MAX_VALUE);

        VBox booking_info = new VBox();
        booking_info.getStyleClass().add("guest-record-section");
        HBox.setHgrow(booking_info, Priority.ALWAYS);
        booking_info.setMaxWidth(Double.MAX_VALUE);

        Label personal_info_header = new Label("Personal Information".toUpperCase());
        personal_info_header.getStyleClass().add("guest-record-section-title");

        HBox fullName_row = row("Full name", guest_data.getFirstname() + " " + guest_data.getLastname());
        HBox email_row    = row("Email",     guest_data.getEmail());
        HBox phone_row    = row("Phone",     guest_data.getPhoneNumber());
        HBox country_row  = row("Country",   guest_data.getCountry());
        HBox city_row     = row("City",      guest_data.getCity());

        personal_info.getChildren().addAll(
                personal_info_header, new Separator(),
                fullName_row, new Separator(),
                email_row,    new Separator(),
                phone_row,    new Separator(),
                country_row,  new Separator(),
                city_row
        );

        Label identity_info_header = new Label("Identity Information".toUpperCase());
        identity_info_header.getStyleClass().add("guest-record-section-title");

        HBox guest_id_row   = row("Guest ID",        guest_data.getId());
        HBox id_number_row  = row("ID number",       guest_data.getIdNumber());
        HBox num_stays_row  = row("Number of stays", String.valueOf(guest_data.getNumStays()));
        HBox spent_row      = row("Spent",           "R " + guest_data.getSpent());
        HBox lastStay_row   = row("Last stay",       guest_data.getLastStay());

        identity_info.getChildren().addAll(
                identity_info_header, new Separator(),
                guest_id_row,  new Separator(),
                id_number_row, new Separator(),
                num_stays_row, new Separator(),
                spent_row,     new Separator(),
                lastStay_row
        );

        int num_bookings = 0;
        for (Booking booking : bookings) {
            if (booking.getGuest_id() != selectedGuest.getId()) continue;
            if (booking.getBooking_status().equals(BookingStatus.CANCELLED)) continue;
            num_bookings++;
        }

        Label booking_info_header = new Label("Booking History (" + num_bookings + ")");
        booking_info_header.getStyleClass().add("guest-record-section-title");
        booking_info.getChildren().addAll(booking_info_header, new Separator());

        if (num_bookings == 0) {
            Label none = new Label("No bookings available");
            none.getStyleClass().add("guest-record-empty");
            booking_info.getChildren().add(none);
        }

        else {
            for (Booking booking : bookings) {
                if (booking.getGuest_id() != selectedGuest.getId()) continue;
                if (booking.getBooking_status().equals(BookingStatus.CANCELLED)) continue;

                HBox booking_row = new HBox();
                booking_row.getStyleClass().add("guest-record-booking-row");
                booking_row.setMaxWidth(Double.MAX_VALUE);

                int room_number = 0;
                for (Room room : rooms) {
                    if (room.getRoomid() == booking.getRoom_id()) {
                        room_number = room.getRoomnumber();
                        break;
                    }
                }
                if (room_number == 0) return;

                Label guest_id_lbl = new Label(guest_data.getId());
                guest_id_lbl.getStyleClass().add("guest-record-booking-id");

                Label room_number_label = new Label("Rm " + room_number);
                room_number_label.getStyleClass().add("guest-record-booking-room");

                Label reference_label = new Label(booking.getBooking_reference().toString());
                reference_label.getStyleClass().add("guest-record-booking-ref");

                Label amount_label = new Label("R" + booking.getTotal_cost());
                amount_label.getStyleClass().add("guest-record-booking-amount");

                booking_row.getChildren().addAll(guest_id_lbl, room_number_label, reference_label, amount_label);
                booking_info.getChildren().add(booking_row);
            }
        }

        details_row.getChildren().addAll(personal_info, identity_info, booking_info);
        selectedGuestTable.getChildren().addAll(topRow, new Separator(), details_row);
        getChildren().add(selectedGuestTable);
    }

    private HBox row(String key, String value) {
        HBox r = new HBox();
        r.getStyleClass().add("guest-record-row");
        r.setMaxWidth(Double.MAX_VALUE);

        Label k = new Label(key.toUpperCase());
        k.getStyleClass().add("guest-record-key");

        Label v = new Label(value);
        v.getStyleClass().add("guest-record-value");
        HBox.setHgrow(v, Priority.ALWAYS);
        v.setMaxWidth(Double.MAX_VALUE);

        r.getChildren().addAll(k, v);
        return r;
    }
}