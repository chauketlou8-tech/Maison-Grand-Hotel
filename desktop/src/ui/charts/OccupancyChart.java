package ui.charts;

import enums.RoomStatus;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.RotateTransition;
import javafx.animation.Timeline;
import javafx.geometry.Pos;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import models.Room;

public class OccupancyChart extends VBox {
    private static final String[] SLICE_COLORS = {"#c9a84c", "#4a9d6a", "#d07a3c", "#a84c4c", "#8a7ec9"};

    private final PieChart pieChart;
    private final StackPane donut;
    private StackPane overlay;
    private final VBox legend;
    private boolean skeleton;

    public OccupancyChart(Room[] rooms) {
        if (rooms != null) {
            System.out.println(rooms[0]);
        }
        getStyleClass().add("occupancy-chart-container");
        setMaxWidth(Double.MAX_VALUE);

        skeleton = (rooms == null);

        pieChart = new PieChart();
        pieChart.getStyleClass().add("occupancy-chart");
        pieChart.setLegendVisible(false);
        pieChart.setAnimated(false);
        pieChart.setLabelsVisible(false);
        pieChart.setStartAngle(90);

        pieChart.getData().add(new PieChart.Data("Occupied", 20));
        pieChart.getData().add(new PieChart.Data("Available", 20));
        pieChart.getData().add(new PieChart.Data("Closed", 20));
        pieChart.getData().add(new PieChart.Data("Maintenance", 20));
        pieChart.getData().add(new PieChart.Data("Reserved", 20));

        for (int i = 0; i < pieChart.getData().size(); i++) {
            pieChart.getData().get(i).getNode().setStyle("-fx-pie-color: " + SLICE_COLORS[i] + ";");
        }

        Circle hole = new Circle(70);
        hole.getStyleClass().add("occupancy-hole");

        donut = new StackPane(pieChart, hole);
        donut.getStyleClass().add("occupancy-donut");
        donut.setMaxWidth(Double.MAX_VALUE);
        donut.setPrefHeight(200);
        donut.setMinHeight(200);
        donut.setMaxHeight(200);

        if (skeleton) {
            pieChart.setVisible(false);
            for (PieChart.Data d : pieChart.getData()) {
                d.getNode().setStyle("-fx-pie-color: #1f1d1a;");
            }
        }

        else {
            donut.setOpacity(0);
            javafx.animation.FadeTransition fade = new javafx.animation.FadeTransition(Duration.millis(600), donut);
            fade.setFromValue(0);
            fade.setToValue(1);
            fade.play();

            RotateTransition rotate = new RotateTransition(Duration.seconds(30), donut);
            rotate.setByAngle(360);
            rotate.setCycleCount(RotateTransition.INDEFINITE);
            rotate.setInterpolator(Interpolator.LINEAR);
            rotate.play();
        }

        legend = new VBox(10);
        legend.getStyleClass().add("occupancy-legend");

        legend.getChildren().add(legendRow("#c9a84c", "Occupied", 20, skeleton));
        legend.getChildren().add(legendRow("#4a9d6a", "Available", 6, skeleton));
        legend.getChildren().add(legendRow("#d4a84c", "Closed", 2, skeleton));
        legend.getChildren().add(legendRow("#a84c4c", "Maintenance", 2, skeleton));
        legend.getChildren().add(legendRow("#8a7ec9", "Reserved", 4, skeleton));

        getChildren().addAll(donut, legend);

        if (skeleton) {
            Circle ghostRing = new Circle(70);
            ghostRing.setFill(Color.TRANSPARENT);
            ghostRing.setStroke(Color.web("#1f1d1a"));
            ghostRing.setStrokeWidth(40);
            ghostRing.setMouseTransparent(true);

            Label loadingLabel = new Label("Loading...");
            loadingLabel.setStyle("-fx-text-fill: #6a6058; -fx-font-size: 12px; -fx-font-family: 'Inter','Segoe UI',sans-serif;");
            loadingLabel.setMouseTransparent(true);

            overlay = new StackPane(ghostRing, loadingLabel);
            overlay.setMouseTransparent(true);

            donut.getChildren().add(overlay);

            Timeline pulse = new Timeline(
                    new KeyFrame(Duration.ZERO, new KeyValue(overlay.opacityProperty(), 1.0)),
                    new KeyFrame(Duration.millis(1500), new KeyValue(overlay.opacityProperty(), 0.75)),
                    new KeyFrame(Duration.millis(3000), new KeyValue(overlay.opacityProperty(), 1.0))
            );
            pulse.setCycleCount(Timeline.INDEFINITE);
            pulse.play();
        }
    }

    private HBox legendRow(String color, String label, int target, boolean skeleton) {
        HBox row = new HBox(8);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setMaxWidth(Double.MAX_VALUE);

        Rectangle dot = new Rectangle(6, 6);
        dot.setFill(Color.web(skeleton ? "#1f1d1a" : color));
        dot.setArcWidth(2);
        dot.setArcHeight(2);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        if (skeleton) {
            Region labelGhost = new Region();
            labelGhost.setPrefWidth(60);
            labelGhost.setPrefHeight(16);
            labelGhost.setMinHeight(16);
            labelGhost.setMaxHeight(16);
            labelGhost.setStyle("-fx-background-color: #1f1d1a; -fx-background-radius: 2;");

            Region valueGhost = new Region();
            valueGhost.setPrefWidth(20);
            valueGhost.setPrefHeight(16);
            valueGhost.setMinHeight(16);
            valueGhost.setMaxHeight(16);
            valueGhost.setStyle("-fx-background-color: #1f1d1a; -fx-background-radius: 2;");

            row.getChildren().addAll(dot, labelGhost, spacer, valueGhost);
        } else {
            Label name = new Label(label);
            name.getStyleClass().add("occupancy-legend-label");

            Label count = new Label(String.valueOf(target));
            count.getStyleClass().add("occupancy-legend-value");

            row.getChildren().addAll(dot, name, spacer, count);
        }

        return row;
    }

    public void setRooms(Room[] rooms) {

        if (rooms == null) return;

        int occupied = 0;
        int available = 0;
        int closed = 0;
        int maintenance = 0;
        int reserved = 0;

        for (Room room : rooms) {
            switch (room.getRoomstatus()) {
                case RoomStatus.CLOSED:      closed++;      break;
                case RoomStatus.MAINTENANCE: maintenance++; break;
                case RoomStatus.RESERVED:    reserved++;    break;
                case RoomStatus.AVAILABLE:   available++;   break;
                case RoomStatus.OCCUPIED:    occupied++;    break;
            }
        }

        pieChart.getData().get(0).setPieValue(Math.max(occupied, 0));
        pieChart.getData().get(1).setPieValue(Math.max(available, 0));
        pieChart.getData().get(2).setPieValue(Math.max(closed, 0));
        pieChart.getData().get(3).setPieValue(Math.max(maintenance, 0));
        pieChart.getData().get(4).setPieValue(Math.max(reserved, 0));

        pieChart.setVisible(true);
        for (int i = 0; i < pieChart.getData().size(); i++) {
            pieChart.getData().get(i).getNode().setStyle("-fx-pie-color: " + SLICE_COLORS[i] + ";");
        }

        if (skeleton) {
            skeleton = false;

            donut.getChildren().remove(overlay);

            donut.setOpacity(0);
            javafx.animation.FadeTransition fade = new javafx.animation.FadeTransition(Duration.millis(600), donut);
            fade.setFromValue(0);
            fade.setToValue(1);
            fade.play();

            RotateTransition rotate = new RotateTransition(Duration.seconds(30), donut);
            rotate.setByAngle(360);
            rotate.setCycleCount(RotateTransition.INDEFINITE);
            rotate.setInterpolator(Interpolator.LINEAR);
            rotate.play();
        }

        legend.getChildren().clear();
        legend.getChildren().add(legendRow("#c9a84c", "Occupied", occupied, false));
        legend.getChildren().add(legendRow("#4a9d6a", "Available", available, false));
        legend.getChildren().add(legendRow("#d07a3c", "Closed", closed, false));
        legend.getChildren().add(legendRow("#a84c4c", "Maintenance", maintenance, false));
        legend.getChildren().add(legendRow("#8a7ec9", "Reserved", reserved, false));
    }
}