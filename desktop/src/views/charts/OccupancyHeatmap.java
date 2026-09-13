package views.charts;

import enums.RoomStatus;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import models.Room;
import records.RoomHistory;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.TextStyle;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class OccupancyHeatmap extends VBox {
    private static final int ROWS = 10;
    private static final int COLS = 7;

    private static final String[] LEGEND_COLORS = {
            "#4a9d6a",
            "#c9a84c",
            "#d07a3c",
            "#a84c4c",
            "#8a7ec9"
    };

    private boolean skeleton;

    private final Rectangle[] legendDots = new Rectangle[5];
    private final Label[] legendLabels = new Label[5];
    private final Label[] roomLabels = new Label[ROWS];
    private final Label[] dayLabels = new Label[COLS];
    private final Region[][] cells = new Region[ROWS][COLS];

    private final GridPane grid;
    private final Timeline pulse;

    private int slideIndex = 0;
    private int totalSlides = 0;

    private Room[] allRooms;
    private Map<Long, Map<LocalDate, String>> historyByRoom;

    private Label slideLabel;
    private Button prevBtn;
    private Button nextBtn;

    public OccupancyHeatmap(RoomHistory[] roomHistory, Room[] rooms) {
        getStyleClass().add("dashboard-card");
        setMaxWidth(Double.MAX_VALUE);
        setMaxHeight(Double.MAX_VALUE);

        skeleton = (roomHistory == null || rooms == null);

        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(16, 20, 16, 20));
        header.setSpacing(16);
        header.setMaxWidth(Double.MAX_VALUE);

        Label title = new Label("Room Occupancy — 7-Day View");
        title.getStyleClass().add("heatmap-title");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox legend = new HBox(14);
        legend.setAlignment(Pos.CENTER_RIGHT);
        legend.getChildren().addAll(
                legendDot(0, "#4a9d6a", "Available"),
                legendDot(1, "#c9a84c", "Occupied"),
                legendDot(2, "#d07a3c", "Closed"),
                legendDot(3, "#a84c4c", "Maintenance"),
                legendDot(4, "#8a7ec9", "Reserved")
        );

        header.getChildren().addAll(title, spacer, legend);

        grid = new GridPane();
        grid.setHgap(4);
        grid.setVgap(4);
        grid.setPadding(new Insets(0, 20, 12, 20));
        grid.setMaxWidth(Double.MAX_VALUE);
        grid.setMaxHeight(Double.MAX_VALUE);
        VBox.setVgrow(grid, Priority.ALWAYS);

        ColumnConstraints roomCol = new ColumnConstraints();
        roomCol.setMinWidth(40);
        roomCol.setPrefWidth(40);
        grid.getColumnConstraints().add(roomCol);

        for (int c = 0; c < COLS; c++) {
            ColumnConstraints cellCol = new ColumnConstraints();
            cellCol.setHgrow(Priority.ALWAYS);
            cellCol.setFillWidth(true);
            grid.getColumnConstraints().add(cellCol);
        }

        for (int r = 0; r <= ROWS; r++) {
            RowConstraints row = new RowConstraints();
            row.setVgrow(Priority.ALWAYS);
            row.setFillHeight(true);
            grid.getRowConstraints().add(row);
        }

        LocalDate today = LocalDate.now();
        for (int d = 0; d < COLS; d++) {
            LocalDate date = today.minusDays(COLS - 1 - d);
            String text;
            if (d == COLS - 1) {
                text = "TODAY";
            } else {
                DayOfWeek dow = date.getDayOfWeek();
                text = dow.getDisplayName(TextStyle.SHORT, Locale.ENGLISH).toUpperCase();
            }

            Label dayLbl = new Label(text);
            dayLbl.getStyleClass().add("heatmap-day");
            dayLbl.setMaxWidth(Double.MAX_VALUE);
            dayLbl.setAlignment(Pos.CENTER);

            if (d == COLS - 1) {
                dayLbl.getStyleClass().add("heatmap-day-today");
            }
            dayLabels[d] = dayLbl;
            grid.add(dayLbl, d + 1, 0);
        }

        for (int r = 0; r < ROWS; r++) {
            Label roomLbl = new Label("");
            roomLbl.getStyleClass().add("heatmap-room");
            roomLbl.setMaxWidth(Double.MAX_VALUE);
            roomLbl.setMaxHeight(Double.MAX_VALUE);
            roomLbl.setAlignment(Pos.CENTER_RIGHT);

            if (skeleton) {
                roomLbl.setStyle("-fx-text-fill: #1f1d1a;");
            }

            roomLabels[r] = roomLbl;
            grid.add(roomLbl, 0, r + 1);

            for (int c = 0; c < COLS; c++) {
                Region cell = new Region();
                cell.setMaxWidth(Double.MAX_VALUE);
                cell.setMaxHeight(Double.MAX_VALUE);
                String cellColor = skeleton ? "#1f1d1a" : "#1a1c1a";
                cell.setStyle("-fx-background-color: " + cellColor + "; -fx-background-radius: 3;");
                cells[r][c] = cell;
                grid.add(cell, c + 1, r + 1);
            }
        }

        HBox controls = buildControls();

        getChildren().addAll(header, grid, controls);

        if (skeleton) {
            pulse = new Timeline(
                    new KeyFrame(Duration.ZERO, new KeyValue(grid.opacityProperty(), 1.0)),
                    new KeyFrame(Duration.millis(1500), new KeyValue(grid.opacityProperty(), 0.75)),
                    new KeyFrame(Duration.millis(3000), new KeyValue(grid.opacityProperty(), 1.0))
            );
            pulse.setCycleCount(Timeline.INDEFINITE);
            pulse.play();
        } else {
            pulse = null;
        }
    }

    private HBox buildControls() {
        prevBtn = new Button("◀");
        prevBtn.getStyleClass().add("heatmap-nav-btn");
        prevBtn.setOnAction(e -> {
            slideIndex = (slideIndex - 1 + totalSlides) % totalSlides;
            renderSlide();
        });

        nextBtn = new Button("▶");
        nextBtn.getStyleClass().add("heatmap-nav-btn");
        nextBtn.setOnAction(e -> {
            slideIndex = (slideIndex + 1) % totalSlides;
            renderSlide();
        });

        slideLabel = new Label("0 / 0");
        slideLabel.getStyleClass().add("heatmap-nav-label");

        HBox nav = new HBox(8, prevBtn, slideLabel, nextBtn);
        nav.setAlignment(Pos.CENTER_RIGHT);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox controls = new HBox(spacer, nav);
        controls.setAlignment(Pos.CENTER_RIGHT);
        controls.setPadding(new Insets(0, 20, 16, 20));
        controls.setMaxWidth(Double.MAX_VALUE);

        return controls;
    }

    private HBox legendDot(int index, String color, String label) {
        Rectangle dot = new Rectangle(8, 8);
        dot.setFill(Color.web(skeleton ? "#1f1d1a" : color));
        dot.setArcWidth(2);
        dot.setArcHeight(2);
        legendDots[index] = dot;

        Label lbl = new Label(label);
        lbl.getStyleClass().add("heatmap-legend-label");
        if (skeleton) {
            lbl.setStyle("-fx-text-fill: #1f1d1a;");
        }
        legendLabels[index] = lbl;

        HBox box = new HBox(5);
        box.setAlignment(Pos.CENTER_LEFT);
        box.getChildren().addAll(dot, lbl);
        return box;
    }

    public void setRoomHistory(RoomHistory[] roomHistory, Room[] rooms) {
        if (roomHistory == null || rooms == null) return;

        if (skeleton) {
            skeleton = false;

            if (pulse != null) {
                pulse.stop();
                grid.setOpacity(1.0);
            }

            for (int i = 0; i < 5; i++) {
                legendDots[i].setFill(Color.web(LEGEND_COLORS[i]));
                legendLabels[i].setStyle("");
            }

            for (int r = 0; r < ROWS; r++) {
                roomLabels[r].setStyle("");
            }
        }

        LocalDate today = LocalDate.now();
        LocalDate start = today.minusDays(COLS - 1);

        for (int d = 0; d < COLS; d++) {
            LocalDate date = today.minusDays(COLS - 1 - d);
            Label lbl = dayLabels[d];
            if (d == COLS - 1) {
                lbl.setText("TODAY");
            } else {
                lbl.setText(date.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.ENGLISH).toUpperCase());
            }
        }

        historyByRoom = new HashMap<>();
        for (RoomHistory h : roomHistory) {
            LocalDate date = h.status_date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

            if (date.isBefore(start) || date.isAfter(today)) continue;

            historyByRoom.computeIfAbsent(h.roomid(), k -> new HashMap<>()).put(date, h.status());
        }

        this.allRooms = rooms;
        this.totalSlides = (rooms.length + ROWS - 1) / ROWS;

        if (slideIndex >= totalSlides) {
            slideIndex = 0;
        }

        renderSlide();
    }

    private void renderSlide() {
        if (allRooms == null || historyByRoom == null) return;

        LocalDate today = LocalDate.now();

        int startIdx = slideIndex * ROWS;

        for (int r = 0; r < ROWS; r++) {
            int roomIdx = startIdx + r;

            if (roomIdx >= allRooms.length) {
                roomLabels[r].setText("");
                for (int c = 0; c < COLS; c++) {
                    cells[r][c].setStyle("-fx-background-color: #1a1c1a; -fx-background-radius: 3;");
                }
                continue;
            }

            Room room = allRooms[roomIdx];
            roomLabels[r].setText(String.valueOf(room.getRoomnumber()));

            Map<LocalDate, String> perDay = historyByRoom.getOrDefault(room.getRoomid(), new HashMap<>());

            for (int c = 0; c < COLS; c++) {
                LocalDate date = today.minusDays(COLS - 1 - c);
                String status = perDay.get(date);
                String color = colorFor(status);
                cells[r][c].setStyle("-fx-background-color: " + color + "; -fx-background-radius: 3;");
            }
        }

        slideLabel.setText((slideIndex + 1) + " / " + totalSlides);
    }

    private String colorFor(String status) {
        if (status == null) return "#1a1c1a";
        return switch (status) {
            case "available" -> "#4a9d6a";
            case "occupied" -> "#c9a84c";
            case "closed" -> "#d07a3c";
            case "maintenance" -> "#a84c4c";
            case "reserved" -> "#8a7ec9";
            default -> "#1a1c1a";
        };
    }
}