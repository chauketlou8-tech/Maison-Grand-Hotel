package ui.pages;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcons;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import models.Room;
import models.Service;
import models.ServiceTask;
import models.Settings;
import utils.Conn;
import data.HousekeepingData;

import java.io.IOException;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class Housekeeping extends VBox {

    private final Label pendingCleaningValue;
    private final Label inMaintenanceValue;
    private final Label availableRoomsValue;
    private final Label tasksCompletedValue;

    private final ObservableList<HousekeepingData.CleaningRoom> cleaningData = FXCollections.observableArrayList();
    private final ObservableList<HousekeepingData.DailyTask> taskData = FXCollections.observableArrayList();
    private final ObservableList<HousekeepingData.MaintenanceRoom> maintenanceData = FXCollections.observableArrayList();

    private boolean updateQueued = false;
    private Room[] pendingRooms;
    private Service[] pendingServices;
    private ServiceTask[] pendingTasks;

    private final Connection connection = Conn.getConnection();

    public Housekeeping() throws SQLException, IOException {
        assert(connection != null);

        getStyleClass().add("housekeeping");
        setSpacing(0);
        setMaxWidth(Double.MAX_VALUE);
        setMaxHeight(Double.MAX_VALUE);

        HBox stats = new HBox();
        stats.getStyleClass().add("housekeeping-stats");
        stats.setSpacing(12);
        stats.setPadding(new Insets(20, 20, 12, 20));
        stats.setMaxWidth(Double.MAX_VALUE);

        VBox pendingCard = statCard("PENDING CLEANING");
        pendingCleaningValue = (Label) pendingCard.getProperties().get("value");
        pendingCleaningValue.setText("0");

        VBox maintCard = statCard("IN MAINTENANCE");
        inMaintenanceValue = (Label) maintCard.getProperties().get("value");
        inMaintenanceValue.setText("0");

        VBox availCard = statCard("AVAILABLE ROOMS");
        availableRoomsValue = (Label) availCard.getProperties().get("value");
        availableRoomsValue.setText("0");

        VBox tasksCard = statCard("TASKS COMPLETED");
        tasksCompletedValue = (Label) tasksCard.getProperties().get("value");
        tasksCompletedValue.setText("0 / 0");

        stats.getChildren().addAll(pendingCard, maintCard, availCard, tasksCard);

        HBox middle = new HBox();
        middle.setSpacing(12);
        middle.setPadding(new Insets(0, 20, 12, 20));
        middle.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(middle, Priority.ALWAYS);

        VBox queueBox = sectionBox("CLEANING QUEUE");
        TableView<HousekeepingData.CleaningRoom> cleaningQueue = buildCleaningQueue();
        VBox.setVgrow(cleaningQueue, Priority.ALWAYS);
        queueBox.getChildren().add(cleaningQueue);
        HBox.setHgrow(queueBox, Priority.ALWAYS);

        VBox tasksBox = sectionBox("DAILY TASK CHECKLIST");
        TableView<HousekeepingData.DailyTask> taskTable = buildTaskTable();
        VBox.setVgrow(taskTable, Priority.ALWAYS);
        tasksBox.getChildren().add(taskTable);
        HBox.setHgrow(tasksBox, Priority.ALWAYS);

        middle.getChildren().addAll(queueBox, tasksBox);

        VBox maintenanceBox = sectionBox("ROOMS IN MAINTENANCE");
        maintenanceBox.setPadding(new Insets(0, 20, 20, 20));
        TableView<HousekeepingData.MaintenanceRoom> maintenanceRooms = buildMaintenanceRooms();
        VBox.setVgrow(maintenanceRooms, Priority.ALWAYS);
        maintenanceBox.getChildren().add(maintenanceRooms);

        getChildren().addAll(stats, middle, maintenanceBox);
        VBox.setVgrow(middle, Priority.ALWAYS);
    }

    private VBox statCard(String label) {
        VBox card = new VBox(4);
        card.getStyleClass().add("stat-card");
        card.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(card, Priority.ALWAYS);

        Label labelLbl = new Label(label);
        labelLbl.getStyleClass().add("stat-label");

        Label valueLbl = new Label("0");
        valueLbl.getStyleClass().add("stat-value");

        card.getChildren().addAll(labelLbl, valueLbl);
        card.getProperties().put("value", valueLbl);

        return card;
    }

    private VBox sectionBox(String title) {
        VBox box = new VBox(0);
        box.getStyleClass().add("dashboard-card");
        box.setMaxWidth(Double.MAX_VALUE);
        box.setMaxHeight(Double.MAX_VALUE);

        Label titleLbl = new Label(title);
        titleLbl.getStyleClass().add("dashboard-card-title");
        titleLbl.setPadding(new Insets(16, 20, 12, 20));

        box.getChildren().add(titleLbl);

        return box;
    }

    private TableView<HousekeepingData.CleaningRoom> buildCleaningQueue() {
        TableView<HousekeepingData.CleaningRoom> table = new TableView<>();
        table.getStyleClass().add("registry-table");
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        table.setMaxWidth(Double.MAX_VALUE);
        table.setMaxHeight(Double.MAX_VALUE);
        table.setItems(cleaningData);

        TableColumn<HousekeepingData.CleaningRoom, Number> room = new TableColumn<>("ROOM");
        TableColumn<HousekeepingData.CleaningRoom, String> type = new TableColumn<>("TYPE");
        TableColumn<HousekeepingData.CleaningRoom, Number> floor = new TableColumn<>("FLOOR");
        TableColumn<HousekeepingData.CleaningRoom, String> description = new TableColumn<>("DESCRIPTION");
        TableColumn<HousekeepingData.CleaningRoom, String> lastCleaned = new TableColumn<>("LAST CLEANED");
        TableColumn<HousekeepingData.CleaningRoom, Void> actions = new TableColumn<>("ACTIONS");

        room.setCellValueFactory(c -> new SimpleObjectProperty<>(c.getValue().roomNumber()));
        type.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().roomType()));
        floor.setCellValueFactory(c -> new SimpleObjectProperty<>(c.getValue().floor()));
        description.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().description()));
        lastCleaned.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().lastCleaned()));

        actions.setCellFactory(_ -> new TableCell<>() {
            private final Button cleanBtn = new Button("Clean");
            private final Button editBtn = new Button();

            {
                cleanBtn.setStyle("-fx-background-color: #4a9d6a; -fx-text-fill: #fff; -fx-font-size: 11px; -fx-padding: 4 10 4 10;");
                FontAwesomeIcon cleanIcon = new FontAwesomeIcon();
                cleanIcon.setIcon(FontAwesomeIcons.CHECK);
                cleanIcon.setSize("10");
                cleanIcon.setFill(Color.web("#fff"));
                cleanBtn.setGraphic(cleanIcon);

                editBtn.setStyle("-fx-background-color: #a84c4c; -fx-text-fill: #fff; -fx-padding: 4 8 4 8;");
                FontAwesomeIcon editIcon = new FontAwesomeIcon();
                editIcon.setIcon(FontAwesomeIcons.PENCIL);
                editIcon.setSize("10");
                editIcon.setFill(Color.web("#fff"));
                editBtn.setGraphic(editIcon);

                HBox box = new HBox(6, cleanBtn, editBtn);
                box.setAlignment(Pos.CENTER_LEFT);
                setGraphic(box);

                cleanBtn.setOnAction(_ -> {
                    HousekeepingData.CleaningRoom row = getTableView().getItems().get(getIndex());

                    if (row == null) return;

                    try {
                        markCleaned(row.taskId());
                    }

                    catch (IOException | SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                });

                editBtn.setOnAction(_ -> {
                    HousekeepingData.CleaningRoom row = getTableView().getItems().get(getIndex());

                    if (row == null) return;

                    try {
                        editCleaningTask(row.taskId());
                    }

                    catch (IOException | SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : getGraphic());
            }
        });

        table.getColumns().addAll(room, type, floor, description, lastCleaned, actions);

        return table;
    }

    private TableView<HousekeepingData.DailyTask> buildTaskTable() {
        TableView<HousekeepingData.DailyTask> table = new TableView<>();
        table.getStyleClass().add("registry-table");
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        table.setMaxWidth(Double.MAX_VALUE);
        table.setMaxHeight(Double.MAX_VALUE);
        table.setItems(taskData);

        TableColumn<HousekeepingData.DailyTask, String> name = new TableColumn<>("NAME");
        TableColumn<HousekeepingData.DailyTask, String> assignedTo = new TableColumn<>("ASSIGNED TO");
        TableColumn<HousekeepingData.DailyTask, String> notes = new TableColumn<>("NOTES");
        TableColumn<HousekeepingData.DailyTask, Void> done = new TableColumn<>("DONE");

        name.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().task()));
        assignedTo.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().assignedTo()));
        notes.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().notes()));

        done.setCellFactory(_ -> new TableCell<>() {
            private final CheckBox box = new CheckBox();

            {
                box.setOnAction(_ -> {
                    HousekeepingData.DailyTask row = getTableView().getItems().get(getIndex());

                    if (row == null) return;
                    if (row.done()) return;

                    try {
                        setComplete(row.id());
                    }

                    catch (IOException | SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || getIndex() < 0 || getIndex() >= getTableView().getItems().size()) {
                    setGraphic(null);
                    return;
                }

                HousekeepingData.DailyTask row = getTableView().getItems().get(getIndex());

                box.setSelected(row.done());
                box.setDisable(row.done());

                setGraphic(box);
            }
        });

        table.getColumns().addAll(name, assignedTo, notes, done);

        return table;
    }

    private TableView<HousekeepingData.MaintenanceRoom> buildMaintenanceRooms() {
        TableView<HousekeepingData.MaintenanceRoom> table = new TableView<>();
        table.getStyleClass().add("registry-table");
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        table.setMaxWidth(Double.MAX_VALUE);
        table.setMaxHeight(Double.MAX_VALUE);
        table.setItems(maintenanceData);

        TableColumn<HousekeepingData.MaintenanceRoom, Number> room = new TableColumn<>("ROOM");
        TableColumn<HousekeepingData.MaintenanceRoom, String> type = new TableColumn<>("TYPE");
        TableColumn<HousekeepingData.MaintenanceRoom, Number> floor = new TableColumn<>("FLOOR");
        TableColumn<HousekeepingData.MaintenanceRoom, String> description = new TableColumn<>("DESCRIPTION");
        TableColumn<HousekeepingData.MaintenanceRoom, String> lastCleaned = new TableColumn<>("LAST CLEANED");
        TableColumn<HousekeepingData.MaintenanceRoom, Void> action = new TableColumn<>("ACTION");

        room.setCellValueFactory(c -> new SimpleObjectProperty<>(c.getValue().roomNumber()));
        type.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().roomType()));
        floor.setCellValueFactory(c -> new SimpleObjectProperty<>(c.getValue().floor()));
        description.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().description()));
        lastCleaned.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().lastCleaned()));

        action.setCellFactory(_ -> new TableCell<>() {
            private final Button markAvailable = new Button("Mark Available");

            {
                markAvailable.setStyle("-fx-background-color: #4a9d6a; -fx-text-fill: #fff; -fx-font-size: 11px; -fx-padding: 4 12 4 12;");
                FontAwesomeIcon checkIcon = new FontAwesomeIcon();
                checkIcon.setIcon(FontAwesomeIcons.CHECK);
                checkIcon.setSize("10");
                checkIcon.setFill(Color.web("#fff"));
                markAvailable.setGraphic(checkIcon);

                markAvailable.setOnAction(_ -> {
                    HousekeepingData.MaintenanceRoom row = getTableView().getItems().get(getIndex());

                    if (row == null) return;

                    try {
                        markRoomAvailable(row.roomNumber());
                    }

                    catch (IOException | SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : markAvailable);
            }
        });

        table.getColumns().addAll(room, type, floor, description, lastCleaned, action);

        return table;
    }

    public void setData(Room[] rooms, Service[] services, ServiceTask[] serviceTasks, Settings settings) {
        if (rooms == null || services == null || serviceTasks == null) return;

        pendingRooms = rooms;
        pendingServices = services;
        pendingTasks = serviceTasks;

        if (updateQueued) return;
        updateQueued = true;

        Platform.runLater(() -> {
            updateQueued = false;
            try {
                processData(pendingRooms, pendingServices, pendingTasks, settings);
            }

            catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void processData(Room[] rooms, Service[] services, ServiceTask[] serviceTasks, Settings settings) throws SQLException {
        if (rooms == null || services == null || serviceTasks == null) return;

        Map<Integer, Service> servicesById = new HashMap<>();
        for (Service s : services) servicesById.put((int) s.getId(), s);

        Map<Long, Room> roomsById = new HashMap<>();
        for (Room r : rooms) roomsById.put(r.getRoomid(), r);

        Map<Long, String> employeeNames = loadEmployeeNames();

        ObservableList<HousekeepingData.CleaningRoom> newCleaning = FXCollections.observableArrayList();
        int pendingCleaning = 0;

        for (ServiceTask st : serviceTasks) {
            Service s = servicesById.get(st.getService_id());
            if (s == null || !"Cleaning".equalsIgnoreCase(s.getName())) continue;
            if (!"pending".equalsIgnoreCase(st.getStatus())) continue;

            Room room = roomsById.get(st.getRoom_id());
            if (room == null) continue;

            newCleaning.add(new HousekeepingData.CleaningRoom(st.getId(), room.getRoomnumber(), room.getRoomtype(), room.getFloornumber(), room.getDescription(), HousekeepingData.latestCleaningDate(room.getRoomid(), serviceTasks, servicesById, settings)));
            pendingCleaning++;
        }

        ObservableList<HousekeepingData.DailyTask> newTasks = FXCollections.observableArrayList();
        int completed = 0;

        for (ServiceTask st : serviceTasks) {
            if (st.getRoom_id() <= 0) continue;

            Service s = servicesById.get(st.getService_id());
            if (s == null) continue;

            boolean done = "completed".equalsIgnoreCase(st.getStatus());
            if (done) completed++;

            String assignedName = st.getAssigned_to() == 0 ? "—" : employeeNames.getOrDefault(st.getAssigned_to(), "—");
            String note = st.getNotes() == null || st.getNotes().isBlank() ? "—" : st.getNotes();

            newTasks.add(new HousekeepingData.DailyTask(st.getId(), done, s.getName(), assignedName, note));
        }

        ObservableList<HousekeepingData.MaintenanceRoom> newMaintenance = FXCollections.observableArrayList();
        int available = 0;

        for (Room r : rooms) {
            String status = r.getRoomstatus().name();

            if ("MAINTENANCE".equalsIgnoreCase(status)) {
                newMaintenance.add(new HousekeepingData.MaintenanceRoom(r.getRoomnumber(), r.getRoomtype(), r.getFloornumber(), r.getDescription(), HousekeepingData.latestCleaningDate(r.getRoomid(), serviceTasks, servicesById, settings)));
            }

            if ("AVAILABLE".equalsIgnoreCase(status)) {
                available++;
            }
        }

        if (!newCleaning.equals(cleaningData)) {
            cleaningData.setAll(newCleaning);
        }

        if (!newTasks.equals(taskData)) {
            taskData.setAll(newTasks);
        }

        if (!newMaintenance.equals(maintenanceData)) {
            maintenanceData.setAll(newMaintenance);
        }

        String pendingStr = String.valueOf(pendingCleaning);
        if (!pendingStr.equals(pendingCleaningValue.getText())) {
            pendingCleaningValue.setText(pendingStr);
        }

        String maintStr = String.valueOf(newMaintenance.size());
        if (!maintStr.equals(inMaintenanceValue.getText())) {
            inMaintenanceValue.setText(maintStr);
        }

        String availStr = String.valueOf(available);
        if (!availStr.equals(availableRoomsValue.getText())) {
            availableRoomsValue.setText(availStr);
        }

        String tasksStr = completed + " / " + newTasks.size();
        if (!tasksStr.equals(tasksCompletedValue.getText())) {
            tasksCompletedValue.setText(tasksStr);
        }
    }

    private Map<Long, String> loadEmployeeNames() throws SQLException {
        Map<Long, String> names = new HashMap<>();

        try (Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery("select id, firstname, lastname from employees")) {

            while (rs.next()) {
                names.put(rs.getLong("id"), rs.getString("firstname") + " " + rs.getString("lastname"));
            }
        }

        return names;
    }

    private void setComplete(int taskId) throws IOException, SQLException {
        try (Statement statement = connection.createStatement()){
            statement.execute("update servicetasks set status = 'completed', completed_at = now(), updated_at = now() where id = '" + taskId + "'");
        }

        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void markRoomAvailable(int roomNumber) throws IOException, SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("update rooms set roomstatus = 'available' where roomnumber = '" + roomNumber + "'");
        }

        try (PreparedStatement statement = connection.prepareStatement(
                "update servicetasks set status = 'completed', completed_at = now(), updated_at = now() " +
                    "where room_id = (select roomid from rooms where roomnumber = ?) " +
                    "and service_id = (select id from services where name = 'Maintenance') and status <> 'completed'")) {

            statement.setInt(1, roomNumber);

            statement.executeUpdate();
        }
    }

    private void markCleaned(int taskId) throws IOException, SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("update servicetasks set status = 'completed', completed_at = now(), updated_at = now() where id = '" + taskId + "'");
        }
    }

    private void editCleaningTask(int taskId) throws IOException, SQLException {
        // YOU WRITE THIS
    }
}