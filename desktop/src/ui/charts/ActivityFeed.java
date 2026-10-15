package ui.charts;

import enums.NotificationType;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import models.Notification;
import models.Reservation;
import models.ServiceTask;
import records.Settings;
import records.Payment;
import records.RoomHistory;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

import records.Event;

public class ActivityFeed extends VBox {

    private static final int MAX_EVENTS = 15;

    private final VBox feed;
    private final Label updatedLabel;
    private final VBox skeleton;
    private final Timeline pulse;
    private final ScrollPane scroll;
    private final Rectangle liveDot;
    private final VBox emptyState;

    private boolean firstRender = true;

    private Settings settings;

    public ActivityFeed() {
        getStyleClass().add("dashboard-card");
        setMaxWidth(Double.MAX_VALUE);
        setAlignment(Pos.TOP_LEFT);

        HBox header = new HBox(10);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(14, 20, 14, 20));
        header.setMaxWidth(Double.MAX_VALUE);

        liveDot = new Rectangle(8, 8);
        liveDot.setFill(Color.web("#1f1d1a"));
        liveDot.setArcWidth(8);
        liveDot.setArcHeight(8);

        Label title = new Label("Live Activity");
        title.getStyleClass().add("activity-title");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        updatedLabel = new Label("");
        updatedLabel.getStyleClass().add("activity-updated");

        header.getChildren().addAll(liveDot, title, spacer, updatedLabel);

        skeleton = new VBox(0);
        skeleton.setMaxWidth(Double.MAX_VALUE);
        skeleton.setAlignment(Pos.TOP_LEFT);

        for (int i = 0; i < 6; i++) {
            skeleton.getChildren().add(ghostRow(i == 0));
        }

        feed = new VBox(0);
        feed.setMaxWidth(Double.MAX_VALUE);
        feed.setAlignment(Pos.TOP_LEFT);

        StackPane wrapper = new StackPane(feed);
        wrapper.setAlignment(Pos.TOP_LEFT);
        wrapper.setMinHeight(Region.USE_PREF_SIZE);

        scroll = new ScrollPane(wrapper);
        scroll.getStyleClass().add("activity-scroll");
        scroll.setFitToWidth(true);
        scroll.setFitToHeight(false);
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scroll.setMaxWidth(Double.MAX_VALUE);
        scroll.setMaxHeight(Double.MAX_VALUE);
        scroll.setVisible(false);
        scroll.setManaged(false);
        VBox.setVgrow(scroll, Priority.ALWAYS);

        emptyState = buildEmptyState();
        emptyState.setVisible(false);
        emptyState.setManaged(false);
        VBox.setVgrow(emptyState, Priority.ALWAYS);

        getChildren().addAll(header, skeleton, scroll, emptyState);

        pulse = new Timeline(new KeyFrame(Duration.ZERO, new KeyValue(skeleton.opacityProperty(), 1.0)), new KeyFrame(Duration.millis(1500), new KeyValue(skeleton.opacityProperty(), 0.75)), new KeyFrame(Duration.millis(3000), new KeyValue(skeleton.opacityProperty(), 1.0)));
        pulse.setCycleCount(Timeline.INDEFINITE);
        pulse.play();
    }

    private VBox buildEmptyState() {
        VBox box = new VBox(10);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(40, 20, 40, 20));
        box.setMaxWidth(Double.MAX_VALUE);
        box.setMaxHeight(Double.MAX_VALUE);

        Rectangle dot = new Rectangle(10, 10);
        dot.setFill(Color.web("#3a352e"));
        dot.setArcWidth(10);
        dot.setArcHeight(10);

        Label titleLbl = new Label("No activity yet");
        titleLbl.getStyleClass().add("activity-empty-title");

        Label subLbl = new Label("Events will show up here as they happen.");
        subLbl.getStyleClass().add("activity-empty-sub");
        subLbl.setWrapText(true);

        box.getChildren().addAll(dot, titleLbl, subLbl);
        return box;
    }

    public void setData(Notification[] notifications, Reservation[] reservations, Payment[] payments, RoomHistory[] roomHistory, ServiceTask[] serviceTasks, Settings settings) {
        if (notifications == null || reservations == null || payments == null || roomHistory == null || serviceTasks == null) return;

        this.settings = settings;

        pulse.stop();
        skeleton.setVisible(false);
        skeleton.setManaged(false);
        liveDot.setFill(Color.web("#4a9d6a"));

        List<Event> events = new ArrayList<>();

        for (Reservation r : reservations) {
            if (r.getCheckindate() == null) continue;

            Date updated_at = r.getUpdated_at();
            LocalDateTime when = toLocalDateTime(updated_at);

            events.add(new Event("#4a9d6a", "Reservation received · " + r.getRequestedroomtype() + " room", when));
        }

        for (Payment p : payments) {
            if (!"completed".equals(p.paymentstatus())) continue;

            LocalDateTime when = toLocalDateTime(p.updated_at());

            events.add(new Event("#c9a84c", "Payment R" + String.format("%,.2f", p.amount()) + " received", when));
        }

        for (Notification n : notifications) {
            LocalDateTime when = toLocalDateTime(n.getUpdated_at());
            events.add(new Event(colorForNotification(n.getType()), n.getTitle() + " — " + n.getMessage(), when));
        }

        for (RoomHistory rh : roomHistory) {
            if (rh.status_date() == null) continue;

            LocalDateTime when = rh.updated_at().toLocalDateTime();

            events.add(new Event(colorForStatus(rh.status()), "Room " + rh.roomid() + " · " + rh.status(), when));
        }

        for (ServiceTask st : serviceTasks) {
            LocalDateTime when;

            if ("completed".equalsIgnoreCase(st.getStatus()) && st.getCompleted_at() != null) {
                when = st.getCompleted_at().toLocalDateTime();
            }

            else {
                when = toLocalDateTime(st.getUpdated_at());
            }

            String verb = "completed".equalsIgnoreCase(st.getStatus()) ? "completed" : "created";

            events.add(new Event("#4a7cc9", "Task " + verb + " · Service #" + st.getService_id(), when));
        }

        events.sort(Comparator.comparing(Event::when).reversed());

        feed.getChildren().clear();

        if (events.isEmpty()) {
            scroll.setVisible(false);
            scroll.setManaged(false);
            emptyState.setVisible(true);
            emptyState.setManaged(true);
        }

        else {
            emptyState.setVisible(false);
            emptyState.setManaged(false);
            scroll.setVisible(true);
            scroll.setManaged(true);

            int limit = Math.min(events.size(), MAX_EVENTS);
            for (int i = 0; i < limit; i++) {
                Event ev = events.get(i);
                feed.getChildren().add(activityRow(ev.color(), ev.text(), relativeTime(ev.when()), i == 0));
            }

            if (firstRender) {
                scroll.setVvalue(0);
                firstRender = false;
            }
        }

        updatedLabel.setText("Updated " + LocalDateTime.now().format(timeFormatter()));
    }

    private DateTimeFormatter timeFormatter() {
        String format = settings == null ? null : settings.timeFormat();

        String pattern = "12-hour".equalsIgnoreCase(format) ? "h:mm a" : "HH:mm";

        return DateTimeFormatter.ofPattern(pattern);
    }

    private HBox ghostRow(boolean highlight) {
        HBox row = new HBox(12);
        row.setAlignment(Pos.TOP_LEFT);
        row.setPadding(new Insets(12, 20, 12, 20));
        row.setMaxWidth(Double.MAX_VALUE);
        row.getStyleClass().add("activity-row");

        if (highlight) {
            row.getStyleClass().add("activity-row-active");
        }

        Rectangle dot = new Rectangle(6, 6);
        dot.setFill(Color.web("#1f1d1a"));
        dot.setArcWidth(2);
        dot.setArcHeight(2);

        VBox textBox = new VBox(4);
        HBox.setHgrow(textBox, Priority.ALWAYS);
        textBox.setMaxWidth(Double.MAX_VALUE);

        Region textGhost = new Region();
        textGhost.setPrefHeight(12);
        textGhost.setMaxWidth(Double.MAX_VALUE);
        textGhost.setStyle("-fx-background-color: #1f1d1a; -fx-background-radius: 2;");

        Region timeGhost = new Region();
        timeGhost.setPrefWidth(50);
        timeGhost.setPrefHeight(10);
        timeGhost.setStyle("-fx-background-color: #1f1d1a; -fx-background-radius: 2;");

        textBox.getChildren().addAll(textGhost, timeGhost);

        VBox dotBox = new VBox(dot);
        dotBox.setPadding(new Insets(5, 0, 0, 0));

        row.getChildren().addAll(dotBox, textBox);
        return row;
    }

    private HBox activityRow(String color, String text, String time, boolean highlight) {
        HBox row = new HBox(12);
        row.setAlignment(Pos.TOP_LEFT);
        row.setPadding(new Insets(12, 20, 12, 20));
        row.setMaxWidth(Double.MAX_VALUE);
        row.getStyleClass().add("activity-row");

        if (highlight) {
            row.getStyleClass().add("activity-row-active");
        }

        Rectangle dot = new Rectangle(6, 6);
        dot.setFill(Color.web(color));
        dot.setArcWidth(2);
        dot.setArcHeight(2);

        VBox textBox = new VBox(4);
        HBox.setHgrow(textBox, Priority.ALWAYS);
        textBox.setMaxWidth(Double.MAX_VALUE);

        Label textLbl = new Label(text);
        textLbl.getStyleClass().add("activity-text");
        textLbl.setWrapText(true);
        textLbl.setMaxWidth(Double.MAX_VALUE);

        Label timeLbl = new Label(time);
        timeLbl.getStyleClass().add("activity-time");

        textBox.getChildren().addAll(textLbl, timeLbl);

        VBox dotBox = new VBox(dot);
        dotBox.setPadding(new Insets(5, 0, 0, 0));

        row.getChildren().addAll(dotBox, textBox);
        return row;
    }

    private LocalDateTime toLocalDateTime(Date date) {
        if (date == null) return LocalDateTime.MIN;

        if (date instanceof java.sql.Date sqlDate) {
            return sqlDate.toLocalDate().atStartOfDay();
        }

        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    private String relativeTime(LocalDateTime when) {
        if (when == null || when.equals(LocalDateTime.MIN)) return "";

        java.time.Duration d = java.time.Duration.between(when, LocalDateTime.now());
        long seconds = d.getSeconds();

        if (seconds < 60) return "just now";
        if (seconds < 3600) return (seconds / 60) + "m ago";
        if (seconds < 86400) return (seconds / 3600) + "h " + ((seconds % 3600) / 60) + "m";
        return (seconds / 86400) + "d ago";
    }

    private String colorForStatus(String status) {
        if (status == null) return "#9a9080";
        return switch (status) {
            case "available" -> "#4a9d6a";
            case "occupied" -> "#c9a84c";
            case "closed" -> "#d07a3c";
            case "maintenance" -> "#a84c4c";
            case "reserved" -> "#8a7ec9";
            default -> "#9a9080";
        };
    }

    private String colorForNotification(NotificationType type) {
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
}