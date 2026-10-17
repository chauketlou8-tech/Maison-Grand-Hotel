package ui.pages;

import config.ConnectJedis;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcons;
import enums.RoomStatus;
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
import models.Booking;
import models.Guest;
import models.Room;
import redis.clients.jedis.RedisClient;
import utils.Assignment;
import utils.Conn;
import ui.data.RoomData;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class RoomManagement extends VBox {

    private static final AtomicInteger maintenanceIndex = new AtomicInteger();

    private final Connection connection = Conn.getConnection();
    private final RedisClient client = ConnectJedis.connect();

    private final TableView<RoomData> table_view;
    private final VBox rack;
    private final Label count;

    private final Button tableViewBtn;
    private final Button roomRackBtn;

    private final ComboBox<String> statusCombo;
    private final ComboBox<String> typeCombo;

    private final ObservableList<RoomData> allData = FXCollections.observableArrayList();
    private final ObservableList<RoomData> filtered = FXCollections.observableArrayList();

    private Booking[] bookings;
    private Guest[] guests;
    private Room[] rooms;

    private boolean tableViewMode = true;
    private boolean updating = false;

    private final HBox statusButtons;
    private final Map<RoomStatus, Button> statusButtonsMap = new HashMap<>();
    private RoomData selectedRoom;

    public RoomManagement() throws IOException, SQLException {
        HBox topRow = new HBox();
        topRow.getStyleClass().add("registry-button-row");
        topRow.setAlignment(Pos.CENTER_LEFT);
        topRow.setSpacing(8);
        topRow.setPadding(new Insets(16, 20, 12, 20));
        topRow.setMaxWidth(Double.MAX_VALUE);

        HBox left_panel = new HBox();
        left_panel.setSpacing(8);
        left_panel.setAlignment(Pos.CENTER_LEFT);

        tableViewBtn = new Button("Table View");
        tableViewBtn.getStyleClass().add("registry-button");
        roomRackBtn = new Button("Room Rack");
        roomRackBtn.getStyleClass().add("registry-button");

        FontAwesomeIcon tableIcon = new FontAwesomeIcon();
        tableIcon.setIcon(FontAwesomeIcons.TABLE);
        tableIcon.setSize("11");
        tableIcon.setFill(Color.web("#9a9080"));
        tableViewBtn.setGraphic(tableIcon);

        FontAwesomeIcon rackIcon = new FontAwesomeIcon();
        rackIcon.setIcon(FontAwesomeIcons.TH_LARGE);
        rackIcon.setSize("11");
        rackIcon.setFill(Color.web("#9a9080"));
        roomRackBtn.setGraphic(rackIcon);

        tableViewBtn.setOnAction(_ -> switchToTable());
        roomRackBtn.setOnAction(_ -> switchToRack());

        left_panel.getChildren().addAll(tableViewBtn, roomRackBtn);

        Label dividerTop = new Label("|");
        dividerTop.getStyleClass().add("registry-divider");

        statusButtons = new HBox();
        statusButtons.setSpacing(8);
        statusButtons.setAlignment(Pos.CENTER_LEFT);
        statusButtons.setVisible(false);
        statusButtons.setManaged(false);

        for (RoomStatus s : RoomStatus.values()) {
            Button b = new Button("Mark " + capitalize(s.name()));
            b.getStyleClass().addAll("registry-button", "status-btn-" + s.name().toLowerCase());
            b.setOnAction(_ -> {
                try {
                    updateSelectedRoomStatus(s);
                }

                catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            });
            statusButtonsMap.put(s, b);
            statusButtons.getChildren().add(b);
        }

        HBox right_panel = new HBox();
        right_panel.setAlignment(Pos.CENTER_RIGHT);
        HBox.setHgrow(right_panel, Priority.ALWAYS);

        count = new Label("0 rooms");
        count.getStyleClass().add("registry-count");
        right_panel.getChildren().add(count);

        topRow.getChildren().addAll(left_panel, dividerTop, statusButtons, right_panel);

        HBox filter_row = new HBox();
        filter_row.getStyleClass().add("registry-filter-row");
        filter_row.setSpacing(16);
        filter_row.setAlignment(Pos.CENTER_LEFT);
        filter_row.setPadding(new Insets(20));
        filter_row.setMaxWidth(Double.MAX_VALUE);

        HBox status_panel = new HBox();
        status_panel.setSpacing(8);
        status_panel.setAlignment(Pos.CENTER_LEFT);

        Label statusLabel = new Label("STATUS:");
        statusLabel.getStyleClass().add("registry-field-label");

        statusCombo = new ComboBox<>();
        statusCombo.getStyleClass().add("registry-combo");
        statusCombo.setPrefWidth(160);
        statusCombo.setValue("All");

        status_panel.getChildren().addAll(statusLabel, statusCombo);

        HBox type_panel = new HBox();
        type_panel.setSpacing(8);
        type_panel.setAlignment(Pos.CENTER_LEFT);

        Label typeLabel = new Label("TYPE:");
        typeLabel.getStyleClass().add("registry-field-label");

        typeCombo = new ComboBox<>();
        typeCombo.getStyleClass().add("registry-combo");
        typeCombo.setPrefWidth(180);
        typeCombo.setValue("All");

        type_panel.getChildren().addAll(typeLabel, typeCombo);

        Label divider2 = new Label("|");
        divider2.getStyleClass().add("registry-divider");

        filter_row.getChildren().addAll(status_panel, divider2, type_panel);

        statusCombo.valueProperty().addListener((_, _, _) -> applyFilters());
        typeCombo.valueProperty().addListener((_, _, _) -> applyFilters());

        this.table_view = new TableView<>();
        this.table_view.getStyleClass().add("registry-table");
        this.table_view.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        this.table_view.setMaxWidth(Double.MAX_VALUE);
        this.table_view.setMaxHeight(Double.MAX_VALUE);

        TableColumn<RoomData, Number> roomNumber = new TableColumn<>("Room");
        TableColumn<RoomData, String> type = new TableColumn<>("Type");
        TableColumn<RoomData, Number> floor = new TableColumn<>("Floor");
        TableColumn<RoomData, String> description = new TableColumn<>("Description");
        TableColumn<RoomData, Number> capacity = new TableColumn<>("Capacity");
        TableColumn<RoomData, String> price = new TableColumn<>("Price / Night");
        TableColumn<RoomData, String> status = new TableColumn<>("Status");
        TableColumn<RoomData, String> currentGuest = new TableColumn<>("Current Guest");

        roomNumber.setCellValueFactory(new PropertyValueFactory<>("roomNumber"));
        type.setCellValueFactory(new PropertyValueFactory<>("roomType"));
        floor.setCellValueFactory(new PropertyValueFactory<>("floor"));
        description.setCellValueFactory(new PropertyValueFactory<>("description"));
        capacity.setCellValueFactory(new PropertyValueFactory<>("capacity"));
        price.setCellValueFactory(new PropertyValueFactory<>("price"));
        status.setCellValueFactory(new PropertyValueFactory<>("status"));
        currentGuest.setCellValueFactory(new PropertyValueFactory<>("currentGuest"));

        this.table_view.getColumns().addAll(roomNumber, type, floor, description, capacity, price, status, currentGuest);

        this.table_view.getSelectionModel().selectedItemProperty().addListener((_, _, n) -> {
            if (n != null) {
                selectedRoom = n;
                updateStatusButtons();
            }
        });

        this.table_view.setOnMouseClicked(event -> {
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
                selectedRoom = null;
                table_view.getSelectionModel().clearSelection();
                updateStatusButtons();
            }
        });

        this.rack = new VBox();
        this.rack.getStyleClass().add("room-rack");
        this.rack.setSpacing(16);
        this.rack.setPadding(new Insets(0, 20, 20, 20));
        this.rack.setMaxWidth(Double.MAX_VALUE);
        this.rack.setMaxHeight(Double.MAX_VALUE);
        this.rack.setVisible(false);
        this.rack.setManaged(false);

        getChildren().addAll(topRow, filter_row, this.table_view, this.rack);
        VBox.setVgrow(this.table_view, Priority.ALWAYS);
        VBox.setVgrow(this.rack, Priority.ALWAYS);
        setMaxWidth(Double.MAX_VALUE);
        setMaxHeight(Double.MAX_VALUE);

        switchToTable();
    }

    private void switchToTable() {
        tableViewMode = true;
        table_view.setVisible(true);
        table_view.setManaged(true);
        rack.setVisible(false);
        rack.setManaged(false);

        tableViewBtn.getStyleClass().remove("registry-button");
        if (!tableViewBtn.getStyleClass().contains("registry-button-primary")) tableViewBtn.getStyleClass().add("registry-button-primary");

        roomRackBtn.getStyleClass().remove("registry-button-primary");
        if (!roomRackBtn.getStyleClass().contains("registry-button")) roomRackBtn.getStyleClass().add("registry-button");

        render();
    }

    private void switchToRack() {
        tableViewMode = false;
        table_view.setVisible(false);
        table_view.setManaged(false);
        rack.setVisible(true);
        rack.setManaged(true);

        roomRackBtn.getStyleClass().remove("registry-button");
        if (!roomRackBtn.getStyleClass().contains("registry-button-primary")) roomRackBtn.getStyleClass().add("registry-button-primary");

        tableViewBtn.getStyleClass().remove("registry-button-primary");
        if (!tableViewBtn.getStyleClass().contains("registry-button")) tableViewBtn.getStyleClass().add("registry-button");

        render();
    }

    public void setData(Booking[] bookings, Guest[] guests, Room[] rooms) {
        if (bookings == null || guests == null || rooms == null) return;

        this.bookings = bookings;
        this.guests = guests;
        this.rooms = rooms;

        ObservableList<RoomData> newData = FXCollections.observableArrayList();
        for (Room room : rooms) {
            newData.add(new RoomData(room, bookings, guests));
        }
        newData.sort(Comparator.comparingInt(RoomData::getRoomNumber));

        Platform.runLater(() -> {
            if (newData.equals(allData)) return;
            allData.setAll(newData);

            ObservableList<String> statuses = FXCollections.observableArrayList();
            statuses.add("All");
            for (RoomStatus s : RoomStatus.values()) statuses.add(capitalize(s.name()));

            if (!statuses.equals(statusCombo.getItems())) {
                String current = statusCombo.getValue();
                statusCombo.setItems(statuses);
                if (current != null && statuses.contains(current)) statusCombo.setValue(current);
                else statusCombo.setValue("All");
            }

            ObservableList<String> types = FXCollections.observableArrayList();
            types.add("All");
            for (RoomData d : newData) {
                String t = d.getRoomType();
                if (t != null && !types.contains(t)) types.add(t);
            }
            types.sort(String::compareTo);

            if (!types.equals(typeCombo.getItems())) {
                String current = typeCombo.getValue();
                typeCombo.setItems(types);
                if (current != null && types.contains(current)) typeCombo.setValue(current);
                else typeCombo.setValue("All");
            }

            applyFilters();
        });
    }

    private void applyFilters() {
        String status = statusCombo.getValue() == null ? "All" : statusCombo.getValue();
        String type = typeCombo.getValue() == null ? "All" : typeCombo.getValue();

        filtered.clear();

        for (RoomData d : allData) {
            if (!status.equals("All") && !status.equalsIgnoreCase(d.getStatus())) continue;
            if (!type.equals("All") && !type.equals(d.getRoomType())) continue;
            filtered.add(d);
        }

        filtered.sort(Comparator.comparingInt(RoomData::getRoomNumber));

        if (!filtered.equals(table_view.getItems())) {
            table_view.setItems(filtered);
        }

        count.setText(filtered.size() + (filtered.size() == 1 ? " room" : " rooms"));
        render();
    }

    private void render() {
        if (tableViewMode) {
            renderTable();
        }

        else {
            renderRack();
        }
    }

    private void renderTable() {
        // bound to filtered already
    }

    private void renderRack() {
        rack.getChildren().clear();

        java.util.TreeMap<Integer, java.util.List<RoomData>> byFloor = new java.util.TreeMap<>(java.util.Comparator.reverseOrder());

        for (RoomData d : filtered) {
            byFloor.computeIfAbsent(d.getFloor(), _ -> new java.util.ArrayList<>()).add(d);
        }

        for (var entry : byFloor.entrySet()) {
            int floor = entry.getKey();
            java.util.List<RoomData> roomsOnFloor = entry.getValue();
            roomsOnFloor.sort(Comparator.comparingInt(RoomData::getRoomNumber));

            HBox row = new HBox();
            row.getStyleClass().add("room-rack-floor-row");
            row.setSpacing(16);
            row.setAlignment(Pos.CENTER_LEFT);

            Label floorLabel = new Label("FLOOR\n" + floor);
            floorLabel.getStyleClass().add("room-rack-floor-label");
            floorLabel.setMinWidth(70);

            HBox roomsBox = new HBox();
            roomsBox.setSpacing(12);
            roomsBox.setAlignment(Pos.CENTER_LEFT);

            for (RoomData d : roomsOnFloor) {
                roomsBox.getChildren().add(rackCard(d));
            }

            row.getChildren().addAll(floorLabel, roomsBox);
            rack.getChildren().add(row);
        }
    }

    private VBox rackCard(RoomData d) {
        VBox card = new VBox(2);
        card.getStyleClass().addAll("room-rack-card", "room-status-" + d.getStatus().toLowerCase());
        card.setPadding(new Insets(8, 12, 8, 12));
        card.setMinWidth(140);
        card.setPrefWidth(140);

        Label num = new Label(String.valueOf(d.getRoomNumber()));
        num.getStyleClass().add("room-rack-number");

        Label type = new Label(d.getRoomType());
        type.getStyleClass().add("room-rack-type");

        Label guest = new Label(d.getCurrentGuest());
        guest.getStyleClass().add("room-rack-guest");

        Label status = new Label(d.getStatus());
        status.getStyleClass().add("room-rack-status");

        card.getChildren().addAll(num, type, guest, status);

        card.setOnMouseClicked(_ -> {
            selectedRoom = d;
            updateStatusButtons();
        });

        return card;
    }

    private void updateStatusButtons() {
        if (selectedRoom == null) {
            statusButtons.setVisible(false);
            statusButtons.setManaged(false);
            return;
        }

        for (var entry : statusButtonsMap.entrySet()) {
            boolean isCurrent = capitalize(entry.getKey().name()).equalsIgnoreCase(selectedRoom.getStatus());
            entry.getValue().setVisible(!isCurrent);
            entry.getValue().setManaged(!isCurrent);
        }

        statusButtons.setVisible(true);
        statusButtons.setManaged(true);
    }

    private void updateSelectedRoomStatus(RoomStatus newStatus) throws SQLException {
        if (selectedRoom == null || updating) return;

        updating = true;

        long roomId = selectedRoom.getRoomId();
        RoomStatus oldStatus = selectedRoom.getRoomStatus();

        if (oldStatus == newStatus) {
            updating = false;
            return;
        }

        try (PreparedStatement statement = connection.prepareStatement("update rooms set roomstatus = ?, updated_at = now() where roomid = ?")) {

            statement.setString(1, newStatus.name().toLowerCase());
            statement.setLong(2, roomId);
            statement.executeUpdate();
        }

        try(PreparedStatement statement = connection.prepareStatement("update roomhistory set status = ?, updated_at = now() where roomid = ? and status_date = ?")) {
            statement.setString(1, newStatus.name().toLowerCase());
            statement.setLong(2, roomId);
            statement.setObject(3, OffsetDateTime.now(ZoneOffset.UTC).truncatedTo(ChronoUnit.DAYS));

            statement.executeUpdate();
        }

        if (newStatus == RoomStatus.MAINTENANCE) {
            createMaintenanceTask(roomId, selectedRoom.getFloor());
        }

        for (int i = 0; i < rooms.length; i++) {
            if (rooms[i].getRoomid() == roomId) {
                Room old = rooms[i];
                rooms[i] = new Room(old.getRoomid(), old.getRoomnumber(), old.getFloornumber(), old.getRoomtype(), old.getPrice(), old.getCapacity(), newStatus, old.getDescription());
                break;
            }
        }

        client.del("rooms");
        client.del("availableRooms");

        selectedRoom = null;
        updateStatusButtons();
        setData(bookings, guests, rooms);

        updating = false;
    }

    private void createMaintenanceTask(long roomId, int floor) throws SQLException {
        int index = maintenanceIndex.getAndIncrement();

        Long assignedTo = Assignment.pick("maintenance", index, floor);

        int serviceId = getServiceId();

        if (serviceId <= 0) {
            return;
        }

        try (PreparedStatement statement = connection.prepareStatement("insert into servicetasks(service_id, room_id, assigned_to, scheduled_for) values (?, ?, ?, ?)")) {

            statement.setInt(1, serviceId);
            statement.setLong(2, roomId);

            if (assignedTo == null) {
                statement.setNull(3, Types.BIGINT);
            }

            else {
                statement.setLong(3, assignedTo);
            }

            statement.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now(ZoneOffset.UTC)));

            statement.executeUpdate();
        }
    }

    private int getServiceId() throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement("select id from services where name = ?")) {
            statement.setString(1, "Maintenance");

            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                }
            }
        }

        return 0;
    }

    private String capitalize(String s) {
        return s.charAt(0) + s.substring(1).toLowerCase();
    }
}