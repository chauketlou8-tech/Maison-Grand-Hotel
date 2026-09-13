package views.dashboard;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcons;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import models.Booking;
import models.Guest;
import models.Reservation;
import records.Payment;
import records.Address;
import utils.GuestRegistryData;

import java.util.HashMap;
import java.util.Map;

public class GuestRegistry extends VBox {
    private Guest[] guests;
    private Reservation[] reservations;
    private Address[] addresses;
    private Payment[] payments;

    private Guest selectedGuest;
    int num_records = 0;
    private TableView<GuestRegistryData> guest_displayer;

    private final ObservableList<GuestRegistryData> allData = FXCollections.observableArrayList();
    private TextField searchField;
    private ComboBox<String> filterCombo;
    private Label recordsLabel;

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

        searchField.textProperty().addListener((obs, oldVal, newVal) -> applyFilters());
        filterCombo.valueProperty().addListener((obs, oldVal, newVal) -> applyFilters());

        clearFilters.setOnAction(e -> {
            searchField.clear();
            filterCombo.setValue("All");
            applyFilters();
        });

        getChildren().addAll(button_row, filler_row, this.guest_displayer);
        VBox.setVgrow(this, Priority.ALWAYS);
        setMaxWidth(Double.MAX_VALUE);
        setMaxHeight(Double.MAX_VALUE);

        guest_displayer.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                GuestRegistryData selected = guest_displayer.getSelectionModel().getSelectedItem();

                if (selected != null) {
                    this.selectedGuest = selected.guest;
                    System.out.println(selected.guest);
                }
            }
        });
    }

    public void setData(Guest[] guests, Address[] addresses, Payment[] payments, Booking[] bookings) {
        if (guests == null || addresses == null || payments == null || bookings == null) return;
        this.guests = guests;
        this.addresses = addresses;
        this.payments = payments;

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

            newData.add(new GuestRegistryData(guest, guest_address, payments, bookings));
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

            filterCombo.setItems(FXCollections.observableArrayList());
            filterCombo.getItems().add("All");
            filterCombo.getItems().addAll(countries);
            filterCombo.setValue("All");

            applyFilters();
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
}