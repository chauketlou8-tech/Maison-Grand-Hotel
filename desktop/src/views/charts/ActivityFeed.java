package views.charts;

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
import records.Payment;
import records.RoomHistory;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import records.Event;

public class ActivityFeed extends VBox {

    private static final int MAX_EVENTS = 15;

    private final VBox feed;
    private final Label updatedLabel;
    private final VBox skeleton;
    private final Timeline pulse;
    private final ScrollPane scroll;
    private final Rectangle liveDot;

    private boolean firstRender = true;

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

        getChildren().addAll(header, skeleton, scroll);

        pulse = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(skeleton.opacityProperty(), 1.0)),
                new KeyFrame(Duration.millis(1500), new KeyValue(skeleton.opacityProperty(), 0.75)),
                new KeyFrame(Duration.millis(3000), new KeyValue(skeleton.opacityProperty(), 1.0))
        );
        pulse.setCycleCount(Timeline.INDEFINITE);
        pulse.play();
    }

    public void setData(Notification[] notifications, Reservation[] reservations, Payment[] payments, RoomHistory[] roomHistory) {
        if (notifications == null || reservations == null || payments == null || roomHistory == null) return;

        pulse.stop();
        skeleton.setVisible(false);
        skeleton.setManaged(false);
        scroll.setVisible(true);
        scroll.setManaged(true);
        liveDot.setFill(Color.web("#4a9d6a"));

        List<Event> events = new ArrayList<>();

        for (Reservation r : reservations) {
            if (r.getCheckindate() == null) continue;

            Date created_at = r.getCreated_at();
            LocalDateTime when = toLocalDateTime(created_at);

            events.add(new Event("#4a9d6a", "Booking received · " + r.getRequestedroomtype() + " room", when));
        }

        for (Payment p : payments) {
            if (!"completed".equals(p.paymentstatus())) continue;

            LocalDateTime when = toLocalDateTime(p.created_at());

            events.add(new Event("#c9a84c", "Payment R" + String.format("%,.2f", p.amount()) + " received", when));
        }

        for (RoomHistory h : roomHistory) {
            LocalDateTime when = toLocalDateTime(h.created_at());

            events.add(new Event(colorForStatus(h.status()), "Room " + h.roomid() + " · " + h.status(), when));
        }

        for (Notification n : notifications) {
            LocalDateTime when = toLocalDateTime(n.getCreated_at());
            events.add(new Event(colorForNotification(n.getType()), n.getTitle() + " — " + n.getMessage(), when));
        }

        events.sort(Comparator.comparing(Event::when).reversed());

        feed.getChildren().clear();

        int limit = Math.min(events.size(), MAX_EVENTS);
        for (int i = 0; i < limit; i++) {
            Event ev = events.get(i);
            feed.getChildren().add(activityRow(ev.color(), ev.text(), relativeTime(ev.when()), i == 0));
        }

        if (firstRender) {
            scroll.setVvalue(0);
            firstRender = false;
        }

        updatedLabel.setText("Updated " + LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("HH:mm")));
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
            case reservation_confirmation ->  "#8a7ec9";
        };
    }
}