package ui.pages;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcons;
import enums.NotificationStatus;
import enums.NotificationType;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import models.Notification;
import records.Settings;
import utils.Conn;
import utils.DateFormats;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Notifications extends VBox {

    private final ExecutorService dbPool = Executors.newSingleThreadExecutor(r -> {
        Thread t = new Thread(r, "notifications-db");
        t.setDaemon(true);
        return t;
    });

    private final ObservableList<Notification> items = FXCollections.observableArrayList();

    private final Label countLabel;

    private final Button allBtn;
    private final Button unreadBtn;
    private final Button readBtn;

    private String activeFilter = "All";

    private Notification[] notifications;
    private Settings settings;

    private boolean updateQueued = false;
    private Notification[] pendingNotifications;
    private Settings pendingSettings;

    public Notifications() {
        getStyleClass().addAll("guest-registry", "notifications-page");
        setSpacing(0);
        setMaxWidth(Double.MAX_VALUE);
        setMaxHeight(Double.MAX_VALUE);

        HBox topRow = new HBox();
        topRow.getStyleClass().add("notification-toolbar");
        topRow.setAlignment(Pos.CENTER_LEFT);
        topRow.setSpacing(10);
        topRow.setMaxWidth(Double.MAX_VALUE);

        allBtn = new Button("All");
        allBtn.getStyleClass().add("registry-button-primary");

        unreadBtn = new Button("Unread");
        unreadBtn.getStyleClass().add("registry-button");

        readBtn = new Button("Read");
        readBtn.getStyleClass().add("registry-button");

        allBtn.setOnAction(_ -> setFilter("All", allBtn));
        unreadBtn.setOnAction(_ -> setFilter("Unread", unreadBtn));
        readBtn.setOnAction(_ -> setFilter("Read", readBtn));

        HBox filterGroup = new HBox(6, allBtn, unreadBtn, readBtn);
        filterGroup.setAlignment(Pos.CENTER_LEFT);

        Button markAll = new Button("Mark all as read");
        markAll.getStyleClass().add("registry-button");

        FontAwesomeIcon checkIcon = new FontAwesomeIcon();
        checkIcon.setIcon(FontAwesomeIcons.CHECK);
        checkIcon.setSize("11");
        checkIcon.setFill(Color.web("#9a9080"));
        markAll.setGraphic(checkIcon);

        markAll.setOnAction(_ -> markAllRead());

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        countLabel = new Label("0 notifications");
        countLabel.getStyleClass().add("registry-count");

        topRow.getChildren().addAll(filterGroup, markAll, spacer, countLabel);

        ListView<Notification> list = new ListView<>(items);
        list.getStyleClass().add("notification-list");
        list.setPlaceholder(emptyState());
        list.setCellFactory(_ -> new NotificationCell());
        list.setMaxWidth(Double.MAX_VALUE);
        list.setMaxHeight(Double.MAX_VALUE);
        VBox.setVgrow(list, Priority.ALWAYS);

        getChildren().addAll(topRow, list);
        VBox.setVgrow(this, Priority.ALWAYS);
    }

    private VBox emptyState() {
        VBox emptyState = new VBox(10);
        emptyState.setAlignment(Pos.CENTER);

        FontAwesomeIcon emptyIcon = new FontAwesomeIcon();
        emptyIcon.setIcon(FontAwesomeIcons.BELL);
        emptyIcon.setSize("26");
        emptyIcon.setFill(Color.web("#3a3428"));

        Label emptyLabel = new Label("No notifications");
        emptyLabel.getStyleClass().add("notification-empty");

        emptyState.getChildren().addAll(emptyIcon, emptyLabel);
        return emptyState;
    }

    public void setData(Notification[] notifications, Settings settings) {
        if (notifications == null) return;

        pendingNotifications = notifications;
        pendingSettings = settings;

        if (updateQueued) return;
        updateQueued = true;

        Platform.runLater(() -> {
            updateQueued = false;
            processData(pendingNotifications, pendingSettings);
        });
    }

    private void processData(Notification[] notifications, Settings settings) {
        if (notifications == null) return;

        if (sameAsCurrent(notifications, settings)) {
            return;
        }

        this.notifications = notifications;
        this.settings = settings;

        renderList();
    }

    private boolean sameAsCurrent(Notification[] incoming, Settings settings) {
        if (this.notifications == null || this.settings == null) {
            return false;
        }

        if (incoming.length != this.notifications.length) {
            return false;
        }

        if (settings != this.settings) {
            return false;
        }

        for (int i = 0; i < incoming.length; i++) {
            Notification a = incoming[i];
            Notification b = this.notifications[i];

            if (a.getNotificationid() != b.getNotificationid()) {
                return false;
            }

            if (a.getStatus() != b.getStatus()) {
                return false;
            }
        }

        return true;
    }

    private void setFilter(String filter, Button source) {
        activeFilter = filter;

        for (Button b : new Button[]{allBtn, unreadBtn, readBtn}) {
            b.getStyleClass().remove("registry-button-primary");
            if (!b.getStyleClass().contains("registry-button")) {
                b.getStyleClass().add("registry-button");
            }
        }

        source.getStyleClass().remove("registry-button");
        if (!source.getStyleClass().contains("registry-button-primary")) {
            source.getStyleClass().add("registry-button-primary");
        }

        renderList();
    }

    private void renderList() {
        if (notifications == null) return;

        List<Notification> filtered = new ArrayList<>();
        int unread = 0;

        for (Notification n : notifications) {
            boolean isUnread = n.getStatus() == NotificationStatus.unread;
            if (isUnread) unread++;

            if ("Unread".equals(activeFilter) && !isUnread) continue;
            if ("Read".equals(activeFilter) && isUnread) continue;

            filtered.add(n);
        }

        filtered.sort(Comparator.comparing(Notification::getCreated_at, Comparator.nullsLast(Comparator.naturalOrder())).reversed());

        if (listDiffers(filtered, items)) {
            items.setAll(filtered);
        }

        int shown = filtered.size();
        String label;

        if ("All".equals(activeFilter)) {
            label = shown + (shown == 1 ? " notification" : " notifications") + " · " + unread + " unread";
        }
        else if ("Unread".equals(activeFilter)) {
            label = shown + (shown == 1 ? " unread notification" : " unread notifications");
        }
        else {
            label = shown + (shown == 1 ? " read notification" : " read notifications");
        }

        countLabel.setText(label);
    }

    private boolean listDiffers(List<Notification> incoming, ObservableList<Notification> current) {
        if (incoming.size() != current.size()) {
            return true;
        }

        for (int i = 0; i < incoming.size(); i++) {
            Notification a = incoming.get(i);
            Notification b = current.get(i);

            if (a.getNotificationid() != b.getNotificationid()) {
                return true;
            }

            if (a.getStatus() != b.getStatus()) {
                return true;
            }
        }

        return false;
    }

    private void markAsRead(Notification n) {
        if (n.getStatus() == NotificationStatus.read) {
            return;
        }

        dbPool.submit(() -> {
            try (Connection connection = Conn.getConnection();
                 PreparedStatement statement = connection.prepareStatement("update notifications set status = ?::notificationstatus, updated_at = now() where notificationid = ?")) {
                statement.setObject(1, NotificationStatus.read.name(), java.sql.Types.OTHER);
                statement.setLong(2, n.getNotificationid());

                statement.execute();
            }

            catch (SQLException | IOException e) {
                Platform.runLater(() -> showError("Could not mark notification as read", e));
            }
        });
    }

    private void markAllRead() {
        dbPool.submit(() -> {
            try (Connection connection = Conn.getConnection();
                 PreparedStatement statement = connection.prepareStatement("update notifications set status = ?::notificationstatus, updated_at = now() where status = ?::notificationstatus")) {
                statement.setObject(1, NotificationStatus.read.name(), java.sql.Types.OTHER);
                statement.setObject(2, NotificationStatus.unread.name(), java.sql.Types.OTHER);

                statement.execute();
            }

            catch (SQLException | IOException e) {
                Platform.runLater(() -> showError("Could not mark all notifications as read", e));
            }
        });
    }

    private void showError(String message, Exception e) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message + "\n\n" + e.getMessage(), ButtonType.OK);
        alert.setHeaderText(null);
        alert.initOwner(this.getScene() == null ? null : this.getScene().getWindow());
        alert.showAndWait();
    }

    private String colorForType(NotificationType type) {
        if (type == null) return "#9a9080";
        return switch (type) {
            case reservation_request -> "#4a9d6a";
            case payment_confirmation -> "#c9a84c";
            case reservation_cancelled -> "#a84c4c";
            case reservation_confirmation -> "#8a7ec9";
            case low_occupancy -> "#d07a3c";
            case checkin_reminder -> "#4a7cc9";
            case task_completed -> "#5ba8c9";
            case feedback_request -> "#c97ec9";
            case daily_report -> "#b8b06a";
            case late_checkout -> "#d9543c";
        };
    }

    private String formatTimestamp(Timestamp ts) {
        if (ts == null) return "";

        Settings s = this.settings;
        DateTimeFormatter dateFmt = DateFormats.of(s);
        String timePattern = (s != null && "12-hour".equalsIgnoreCase(s.timeFormat())) ? "h:mm a" : "HH:mm";
        DateTimeFormatter timeFmt = DateTimeFormatter.ofPattern(timePattern);

        return ts.toLocalDateTime().format(dateFmt) + " · " + ts.toLocalDateTime().format(timeFmt);
    }

    private class NotificationCell extends ListCell<Notification> {

        private final StackPane dotColumn = new StackPane();
        private final Circle dot = new Circle(4);
        private final Label title = new Label();
        private final Label time = new Label();
        private final Label message = new Label();
        private final HBox root = new HBox(14);

        NotificationCell() {
            dotColumn.setMinWidth(8);
            dotColumn.setPrefWidth(8);
            dotColumn.setMaxWidth(8);

            dot.getStyleClass().add("notification-dot");

            title.getStyleClass().add("notification-title");
            time.getStyleClass().add("notification-time");
            message.getStyleClass().add("notification-message");
            message.setWrapText(true);
            message.setMaxWidth(Double.MAX_VALUE);

            Region titleSpacer = new Region();
            HBox.setHgrow(titleSpacer, Priority.ALWAYS);
            HBox titleRow = new HBox(8);
            titleRow.setAlignment(Pos.CENTER_LEFT);
            titleRow.setMaxWidth(Double.MAX_VALUE);
            titleRow.getChildren().addAll(title, titleSpacer, time);

            VBox content = new VBox(4);
            content.setMaxWidth(Double.MAX_VALUE);
            HBox.setHgrow(content, Priority.ALWAYS);
            content.getChildren().addAll(titleRow, message);

            root.getStyleClass().add("notification-row");
            root.setAlignment(Pos.CENTER_LEFT);
            root.setMaxWidth(Double.MAX_VALUE);
            root.setMinWidth(Region.USE_PREF_SIZE);
            root.getChildren().addAll(dotColumn, content);

            setOnMouseClicked(_ -> {
                Notification n = getItem();
                if (n != null) markAsRead(n);
            });
        }

        @Override
        protected void updateItem(Notification n, boolean empty) {
            super.updateItem(n, empty);

            if (empty || n == null) {
                setText(null);
                setGraphic(null);
                return;
            }

            boolean unread = n.getStatus() == NotificationStatus.unread;

            root.getStyleClass().remove("notification-row-unread");
            if (unread) {
                root.getStyleClass().add("notification-row-unread");
            }

            dotColumn.getChildren().clear();
            if (unread) {
                dot.setFill(Color.web(colorForType(n.getType())));
                dotColumn.getChildren().add(dot);
            }

            title.setText(n.getTitle() == null ? "" : n.getTitle());
            message.setText(n.getMessage() == null ? "" : n.getMessage());
            time.setText(formatTimestamp(n.getCreated_at()));

            setText(null);
            setGraphic(root);
        }
    }
}