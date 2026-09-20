package ui.pages;

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

    private final VBox dailyTasks;

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

    public Housekeeping() {
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
        dailyTasks = new VBox(0);
        dailyTasks.getStyleClass().add("task-list");
        VBox.setVgrow(dailyTasks, Priority.ALWAYS);
        tasksBox.getChildren().add(dailyTasks);
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

        room.setCellValueFactory(new PropertyValueFactory<>("roomNumber"));
        type.setCellValueFactory(new PropertyValueFactory<>("roomType"));
        floor.setCellValueFactory(new PropertyValueFactory<>("floor"));
        description.setCellValueFactory(new PropertyValueFactory<>("description"));
        lastCleaned.setCellValueFactory(new PropertyValueFactory<>("lastCleaned"));

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

        room.setCellValueFactory(new PropertyValueFactory<>("roomNumber"));
        type.setCellValueFactory(new PropertyValueFactory<>("roomType"));
        floor.setCellValueFactory(new PropertyValueFactory<>("floor"));
        description.setCellValueFactory(new PropertyValueFactory<>("description"));
        lastCleaned.setCellValueFactory(new PropertyValueFactory<>("lastCleaned"));

        action.setCellFactory(_ -> new TableCell<>() {
            private final Button markAvailable = new Button("Mark Available");

            {
                markAvailable.setStyle("-fx-background-color: #4a9d6a; -fx-text-fill: #fff; -fx-font-size: 11px; -fx-padding: 4 12 4 12;");
                FontAwesomeIcon checkIcon = new FontAwesomeIcon();
                checkIcon.setIcon(FontAwesomeIcons.CHECK);
                checkIcon.setSize("10");
                checkIcon.setFill(Color.web("#fff"));
                markAvailable.setGraphic(checkIcon);
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
            processData(pendingRooms, pendingServices, pendingTasks, settings);
        });
    }

    private void processData(Room[] rooms, Service[] services, ServiceTask[] serviceTasks, Settings settings) {
        if (rooms == null || services == null || serviceTasks == null) return;

        Map<Integer, Service> servicesById = new HashMap<>();
        for (Service s : services) servicesById.put((int) s.getId(), s);

        Map<Long, Room> roomsById = new HashMap<>();
        for (Room r : rooms) roomsById.put(r.getRoomid(), r);

        ObservableList<HousekeepingData.CleaningRoom> newCleaning = FXCollections.observableArrayList();
        int pendingCleaning = 0;

        for (ServiceTask st : serviceTasks) {
            Service s = servicesById.get(st.getService_id());
            if (s == null || !"Cleaning".equalsIgnoreCase(s.getName())) continue;
            if (!"pending".equalsIgnoreCase(st.getStatus())) continue;

            Room room = roomsById.get(st.getRoom_id());
            if (room == null) continue;

            newCleaning.add(new HousekeepingData.CleaningRoom(room.getRoomnumber(), room.getRoomtype(), room.getFloornumber(), room.getDescription(), HousekeepingData.latestCleaningDate(room.getRoomid(), serviceTasks, servicesById, settings)));
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

            newTasks.add(new HousekeepingData.DailyTask(st.getId(), done, s.getName()));
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

        syncDailyTasks(newTasks);

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

    private void syncDailyTasks(ObservableList<HousekeepingData.DailyTask> newTasks) {
        if (!newTasks.equals(taskData)) {
            rebuildDailyTasks(newTasks);
        }
    }

    private void rebuildDailyTasks(ObservableList<HousekeepingData.DailyTask> newTasks) {
        taskData.setAll(newTasks);
        dailyTasks.getChildren().clear();

        if (taskData.isEmpty()) {
            Label empty = new Label("No tasks");
            empty.getStyleClass().add("task-placeholder");
            VBox.setVgrow(empty, Priority.ALWAYS);
            dailyTasks.getChildren().add(empty);
            return;
        }

        for (HousekeepingData.DailyTask task : taskData) {
            CheckBox box = new CheckBox(task.task());
            box.getStyleClass().add("task-checkbox");

            box.setSelected(task.done());
            box.setDisable(task.done());

            box.setOnAction(_ -> {
                try {
                    setComplete(task.id());
                } catch (IOException | SQLException ex) {
                    throw new RuntimeException(ex);
                }
            });

            dailyTasks.getChildren().add(box);
        }
    }

    private void setComplete(int taskId) throws IOException, SQLException {
        Connection connection = Conn.getConnection();
        assert(connection != null);

        try(Statement statement = connection.createStatement()){
            statement.execute("update servicetasks set status = 'completed', completed_at = now(), updated_at = now() where id = '" + taskId + "'");
        }

        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}