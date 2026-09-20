package ui.pages;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcons;
import enums.NotificationStatus;
import enums.NotificationType;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import models.Notification;
import models.Settings;
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

public class Notifications extends VBox {

    private final VBox list;
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

    private Connection connection = Conn.getConnection();

    public Notifications() throws SQLException, IOException {
        assert connection != null;

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

        allBtn.setOnAction(e -> setFilter("All", allBtn));
        unreadBtn.setOnAction(e -> setFilter("Unread", unreadBtn));
        readBtn.setOnAction(e -> setFilter("Read", readBtn));

        HBox filterGroup = new HBox(6, allBtn, unreadBtn, readBtn);
        filterGroup.setAlignment(Pos.CENTER_LEFT);

        Button markAll = new Button("Mark all as read");
        markAll.getStyleClass().add("registry-button");

        FontAwesomeIcon checkIcon = new FontAwesomeIcon();
        checkIcon.setIcon(FontAwesomeIcons.CHECK);
        checkIcon.setSize("11");
        checkIcon.setFill(Color.web("#9a9080"));
        markAll.setGraphic(checkIcon);

        markAll.setOnAction(e -> markAllRead());

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        countLabel = new Label("0 notifications");
        countLabel.getStyleClass().add("registry-count");

        topRow.getChildren().addAll(filterGroup, markAll, spacer, countLabel);

        list = new VBox(0);
        list.getStyleClass().add("notification-list");
        list.setFillWidth(true);
        list.setMaxWidth(Double.MAX_VALUE);

        ScrollPane scroll = new ScrollPane(list);
        scroll.getStyleClass().add("activity-scroll");
        scroll.setFitToWidth(true);
        scroll.setFitToHeight(true);
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scroll.setMaxWidth(Double.MAX_VALUE);
        scroll.setMaxHeight(Double.MAX_VALUE);
        VBox.setVgrow(scroll, Priority.ALWAYS);

        getChildren().addAll(topRow, scroll);
        VBox.setVgrow(this, Priority.ALWAYS);
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

        list.getChildren().clear();

        List<Notification> filtered = new ArrayList<>();

        for (Notification n : notifications) {
            boolean unread = n.getStatus() == NotificationStatus.unread;

            if ("Unread".equals(activeFilter) && !unread) continue;
            if ("Read".equals(activeFilter) && unread) continue;

            filtered.add(n);
        }

        filtered.sort(Comparator.comparing(Notification::getCreated_at, Comparator.nullsLast(Comparator.naturalOrder())).reversed());

        if (filtered.isEmpty()) {
            VBox emptyState = new VBox(10);
            emptyState.setAlignment(Pos.CENTER);
            emptyState.setMaxWidth(Double.MAX_VALUE);
            emptyState.setMaxHeight(Double.MAX_VALUE);
            VBox.setVgrow(emptyState, Priority.ALWAYS);

            FontAwesomeIcon emptyIcon = new FontAwesomeIcon();
            emptyIcon.setIcon(FontAwesomeIcons.BELL);
            emptyIcon.setSize("26");
            emptyIcon.setFill(Color.web("#3a3428"));

            Label emptyLabel = new Label("No notifications");
            emptyLabel.getStyleClass().add("notification-empty");

            emptyState.getChildren().addAll(emptyIcon, emptyLabel);
            list.getChildren().add(emptyState);
        }

        else {
            for (Notification n : filtered) {
                list.getChildren().add(notificationRow(n));
            }
        }

        int total = notifications.length;
        int unread = 0;

        for (Notification n : notifications) {
            if (n.getStatus() == NotificationStatus.unread) unread++;
        }

        countLabel.setText(total + (total == 1 ? " notification" : " notifications") + " · " + unread + " unread");
    }

    private HBox notificationRow(Notification n) {
        boolean unread = n.getStatus() == NotificationStatus.unread;

        HBox row = new HBox(14);
        row.getStyleClass().add("notification-row");
        row.setAlignment(Pos.CENTER_LEFT);
        row.setMaxWidth(Double.MAX_VALUE);
        row.setMinWidth(Region.USE_PREF_SIZE);

        if (unread) {
            row.getStyleClass().add("notification-row-unread");
        }

        StackPane dotColumn = new StackPane();
        dotColumn.setMinWidth(8);
        dotColumn.setPrefWidth(8);
        dotColumn.setMaxWidth(8);

        if (unread) {
            Circle dot = new Circle(4);
            dot.getStyleClass().add("notification-dot");
            dot.setFill(Color.web(colorForType(n.getType())));
            dotColumn.getChildren().add(dot);
        }

        VBox content = new VBox(4);
        content.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(content, Priority.ALWAYS);

        HBox titleRow = new HBox(8);
        titleRow.setAlignment(Pos.CENTER_LEFT);
        titleRow.setMaxWidth(Double.MAX_VALUE);

        Label title = new Label(n.getTitle() == null ? "" : n.getTitle());
        title.getStyleClass().add("notification-title");

        Region titleSpacer = new Region();
        HBox.setHgrow(titleSpacer, Priority.ALWAYS);

        Label time = new Label(formatTimestamp(n.getCreated_at()));
        time.getStyleClass().add("notification-time");

        titleRow.getChildren().addAll(title, titleSpacer, time);

        Label message = new Label(n.getMessage() == null ? "" : n.getMessage());
        message.getStyleClass().add("notification-message");
        message.setMaxWidth(Double.MAX_VALUE);
        message.setWrapText(true);

        content.getChildren().addAll(titleRow, message);

        row.getChildren().addAll(dotColumn, content);

        row.setOnMouseClicked(e -> markAsRead(n));

        return row;
    }

    private String formatTimestamp(Timestamp ts) {
        if (ts == null) return "";

        DateTimeFormatter dateFmt = DateFormats.of(settings);
        String timePattern = (settings != null && "12-hour".equalsIgnoreCase(settings.getTimeFormat())) ? "h:mm a" : "HH:mm";
        DateTimeFormatter timeFmt = DateTimeFormatter.ofPattern(timePattern);

        return ts.toLocalDateTime().format(dateFmt) + " · " + ts.toLocalDateTime().format(timeFmt);
    }

    private void markAsRead(Notification n) {
        try (PreparedStatement statement = connection.prepareStatement("update notifications set status = ?::notificationstatus, updated_at = now() where notificationid = ?")) {
            statement.setObject(1, NotificationStatus.read.name(), java.sql.Types.OTHER);
            statement.setLong(2, n.getNotificationid());

            statement.execute();
        }

        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void markAllRead() {
        try (PreparedStatement statement = connection.prepareStatement("update notifications set status = ?::notificationstatus, updated_at = now() where status = ?::notificationstatus")) {
            statement.setObject(1, NotificationStatus.read.name(), java.sql.Types.OTHER);
            statement.setObject(2, NotificationStatus.unread.name(), java.sql.Types.OTHER);

            statement.execute();
        }

        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private String colorForType(NotificationType type) {
        if (type == null) return "#9a9080";

        return switch (type) {
            case reservation_request -> "#4a9d6a";
            case payment_confirmation -> "#c9a84c";
            case reservation_cancelled -> "#a84c4c";
            case reservation_confirmation -> "#8a7ec9";
        };
    }
}