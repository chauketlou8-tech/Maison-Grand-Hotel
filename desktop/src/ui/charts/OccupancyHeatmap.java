package ui.charts;

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
import records.Settings;
import records.RoomHistory;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.TextStyle;
import java.time.temporal.TemporalAdjusters;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class OccupancyHeatmap extends VBox {
    private static final int ROWS = 10;

    private static final String[] LEGEND_COLORS = {
            "#4a9d6a",
            "#c9a84c",
            "#d07a3c",
            "#a84c4c",
            "#8a7ec9"
    };

    private enum Mode { DAILY, WEEKLY, MONTHLY }

    private Mode mode = Mode.DAILY;
    private int cols = 7;

    private boolean skeleton;

    private final Rectangle[] legendDots = new Rectangle[5];
    private final Label[] legendLabels = new Label[5];
    private final Label[] roomLabels = new Label[ROWS];
    private Label[] dayLabels;
    private Region[][] cells;

    private final GridPane grid;
    private final Timeline pulse;

    private int slideIndex = 0;
    private int totalSlides = 0;

    private Room[] allRooms;
    private Map<Long, Map<LocalDate, String>> historyByRoom;

    private Label slideLabel;
    private final Label titleLabel;

    public OccupancyHeatmap() {
        getStyleClass().add("dashboard-card");
        setMaxWidth(Double.MAX_VALUE);
        setMaxHeight(Double.MAX_VALUE);

        skeleton = true;

        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(16, 20, 16, 20));
        header.setSpacing(16);
        header.setMaxWidth(Double.MAX_VALUE);

        titleLabel = new Label(titleForMode());
        titleLabel.getStyleClass().add("heatmap-title");

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

        header.getChildren().addAll(titleLabel, spacer, legend);

        grid = new GridPane();
        grid.setHgap(4);
        grid.setVgap(4);
        grid.setPadding(new Insets(0, 20, 12, 20));
        grid.setMaxWidth(Double.MAX_VALUE);
        grid.setMaxHeight(Double.MAX_VALUE);
        VBox.setVgrow(grid, Priority.ALWAYS);

        rebuildGrid();

        HBox controls = buildControls();

        getChildren().addAll(header, grid, controls);

        pulse = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(grid.opacityProperty(), 1.0)),
                new KeyFrame(Duration.millis(1500), new KeyValue(grid.opacityProperty(), 0.75)),
                new KeyFrame(Duration.millis(3000), new KeyValue(grid.opacityProperty(), 1.0))
        );
        pulse.setCycleCount(Timeline.INDEFINITE);
        pulse.play();
    }

    /** Tear down and rebuild the grid for the current mode/cols. */
    private void rebuildGrid() {
        grid.getChildren().clear();
        grid.getColumnConstraints().clear();
        grid.getRowConstraints().clear();

        ColumnConstraints roomCol = new ColumnConstraints();
        roomCol.setMinWidth(40);
        roomCol.setPrefWidth(40);
        grid.getColumnConstraints().add(roomCol);

        for (int c = 0; c < cols; c++) {
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

        dayLabels = new Label[cols];
        cells = new Region[ROWS][cols];

        LocalDate[] periodStarts = periodStarts();

        for (int d = 0; d < cols; d++) {
            Label dayLbl = new Label(labelFor(periodStarts[d], d));
            dayLbl.getStyleClass().add("heatmap-day");
            dayLbl.setMaxWidth(Double.MAX_VALUE);
            dayLbl.setAlignment(Pos.CENTER);

            if (d == cols - 1) {
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

            for (int c = 0; c < cols; c++) {
                Region cell = new Region();
                cell.setMaxWidth(Double.MAX_VALUE);
                cell.setMaxHeight(Double.MAX_VALUE);
                String cellColor = skeleton ? "#1f1d1a" : "#1a1c1a";
                cell.setStyle("-fx-background-color: " + cellColor + "; -fx-background-radius: 3;");
                cells[r][c] = cell;
                grid.add(cell, c + 1, r + 1);
            }
        }
    }

    private void applyMode(Settings settings) {
        String frequency = settings == null ? null : settings.nightAuditFrequency();

        Mode newMode;
        int newCols;

        if ("Weekly".equalsIgnoreCase(frequency)) {
            newMode = Mode.WEEKLY;
            newCols = 7;      // 7 weeks
        } else if ("Monthly".equalsIgnoreCase(frequency)) {
            newMode = Mode.MONTHLY;
            newCols = 12;     // 12 months
        } else {
            newMode = Mode.DAILY;
            newCols = 7;      // 7 days
        }

        if (newMode != mode || newCols != cols) {
            mode = newMode;
            cols = newCols;
            rebuildGrid();

            if (titleLabel != null) {
                titleLabel.setText(titleForMode());
            }
        }
    }

    private String titleForMode() {
        return switch (mode) {
            case DAILY -> "Room Occupancy — 7-Day View";
            case WEEKLY -> "Room Occupancy — 7-Week View";
            case MONTHLY -> "Room Occupancy — 12-Month View";
        };
    }

    private LocalDate[] periodStarts() {
        LocalDate today = LocalDate.now();
        LocalDate[] out = new LocalDate[cols];

        switch (mode) {
            case DAILY -> {
                for (int i = 0; i < cols; i++) {
                    out[i] = today.minusDays(cols - 1 - i);
                }
            }
            case WEEKLY -> {
                LocalDate thisWeekStart = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
                for (int i = 0; i < cols; i++) {
                    out[i] = thisWeekStart.minusWeeks(cols - 1 - i);
                }
            }
            case MONTHLY -> {
                LocalDate thisMonthStart = today.withDayOfMonth(1);
                for (int i = 0; i < cols; i++) {
                    out[i] = thisMonthStart.minusMonths(cols - 1 - i);
                }
            }
        }

        return out;
    }

    private String labelFor(LocalDate periodStart, int colIdx) {
        boolean current = (colIdx == cols - 1);

        switch (mode) {
            case DAILY -> {
                if (current) return "TODAY";
                return periodStart.getDayOfWeek()
                        .getDisplayName(TextStyle.SHORT, Locale.ENGLISH)
                        .toUpperCase();
            }
            case WEEKLY -> {
                if (current) return "THIS WK";
                return periodStart.format(java.time.format.DateTimeFormatter.ofPattern("MMM d", Locale.ENGLISH)).toUpperCase();
            }
            case MONTHLY -> {
                if (current) return "THIS MO";
                return periodStart.getMonth()
                        .getDisplayName(TextStyle.SHORT, Locale.ENGLISH)
                        .toUpperCase();
            }
        }

        return "";
    }

    private HBox buildControls() {
        Button prevBtn = new Button("◀");
        prevBtn.getStyleClass().add("heatmap-nav-btn");
        prevBtn.setOnAction(_ -> {
            slideIndex = (slideIndex - 1 + totalSlides) % totalSlides;
            renderSlide();
        });

        Button nextBtn = new Button("▶");
        nextBtn.getStyleClass().add("heatmap-nav-btn");
        nextBtn.setOnAction(_ -> {
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

    public void setRoomHistory(RoomHistory[] roomHistory, Room[] rooms, Settings settings) {
        if (roomHistory == null || rooms == null) return;

        applyMode(settings);

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

        LocalDate[] periodStarts = periodStarts();
        LocalDate rangeStart = periodStarts[0];
        LocalDate today = LocalDate.now();

        for (int d = 0; d < cols; d++) {
            Label lbl = dayLabels[d];
            lbl.setText(labelFor(periodStarts[d], d));
        }

        historyByRoom = new HashMap<>();
        for (RoomHistory h : roomHistory) {
            LocalDate raw = h.status_date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate bucket = bucketFor(raw);

            if (bucket.isBefore(rangeStart) || bucket.isAfter(today)) continue;

            historyByRoom.computeIfAbsent(h.roomid(), _ -> new HashMap<>()).put(bucket, h.status());
        }

        this.allRooms = rooms;
        this.totalSlides = (rooms.length + ROWS - 1) / ROWS;

        if (slideIndex >= totalSlides) {
            slideIndex = 0;
        }

        renderSlide();
    }

    private LocalDate bucketFor(LocalDate raw) {
        return switch (mode) {
            case DAILY -> raw;
            case WEEKLY -> raw.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
            case MONTHLY -> raw.withDayOfMonth(1);
        };
    }

    private void renderSlide() {
        if (allRooms == null || historyByRoom == null) return;

        LocalDate[] periodStarts = periodStarts();

        int startIdx = slideIndex * ROWS;

        for (int r = 0; r < ROWS; r++) {
            int roomIdx = startIdx + r;

            if (roomIdx >= allRooms.length) {
                roomLabels[r].setText("");
                for (int c = 0; c < cols; c++) {
                    cells[r][c].setStyle("-fx-background-color: #1a1c1a; -fx-background-radius: 3;");
                }
                continue;
            }

            Room room = allRooms[roomIdx];
            roomLabels[r].setText(String.valueOf(room.getRoomnumber()));

            Map<LocalDate, String> perPeriod = historyByRoom.getOrDefault(room.getRoomid(), new HashMap<>());

            for (int c = 0; c < cols; c++) {
                String status = perPeriod.get(periodStarts[c]);
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