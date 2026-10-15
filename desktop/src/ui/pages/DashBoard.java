package ui.pages;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

import models.Notification;
import models.Reservation;
import models.Room;
import models.ServiceTask;
import records.Settings;
import records.Payment;
import records.RoomHistory;
import ui.charts.*;

import java.time.DayOfWeek;
import java.time.LocalDate;

public class DashBoard extends VBox {

    private Room[] rooms;
    private Reservation[] reservations;
    private Payment[] payments;
    private Notification[] notifications;
    private RoomHistory[] roomHistory;
    private ServiceTask[] serviceTasks;

    private OccupancyChart occupancy_chart;
    private OccupancyHeatmap heatmap;
    private RevenueChart revenue_chart;
    private StatsRow stats_row;
    private ActivityFeed activity;

    private Label revenue_total;
    private Label revenue_trend;

    public DashBoard() {
        this.initUI();
    }

    private void initUI() {
        this.stats_row = new StatsRow();

        GridPane charts_row = new GridPane();
        charts_row.getStyleClass().add("dashboard-charts-row");
        charts_row.setHgap(12);
        charts_row.setMaxWidth(Double.MAX_VALUE);

        ColumnConstraints revenueCol = new ColumnConstraints();
        revenueCol.setPercentWidth(70);
        revenueCol.setHgrow(Priority.ALWAYS);

        ColumnConstraints occupancyCol = new ColumnConstraints();
        occupancyCol.setPercentWidth(30);
        occupancyCol.setHgrow(Priority.ALWAYS);

        charts_row.getColumnConstraints().addAll(revenueCol, occupancyCol);

        VBox revenue_card = new VBox();
        revenue_card.getStyleClass().add("dashboard-card");
        revenue_card.setSpacing(0);
        revenue_card.setMaxWidth(Double.MAX_VALUE);

        VBox occupancy_card = new VBox();
        occupancy_card.getStyleClass().add("dashboard-card");
        occupancy_card.setSpacing(0);
        occupancy_card.setMaxWidth(Double.MAX_VALUE);
        occupancy_card.setAlignment(Pos.TOP_LEFT);
        occupancy_card.setPadding(new Insets(20, 0, 0, 15));

        HBox revenue_header = new HBox();
        revenue_header.getStyleClass().add("dashboard-card-header");
        revenue_header.setAlignment(Pos.CENTER_LEFT);
        revenue_header.setMaxWidth(Double.MAX_VALUE);

        VBox revenue_stats = new VBox();
        revenue_stats.getStyleClass().add("dashboard-card-stats");
        revenue_stats.setMaxWidth(Double.MAX_VALUE);

        Label revenue_title = new Label("Revenue · This Week".toUpperCase());
        revenue_title.getStyleClass().add("dashboard-card-title");

        Label occupancy_title = new Label("Occupancy Breakdown".toUpperCase());
        occupancy_title.getStyleClass().add("dashboard-card-title");

        this.revenue_total = new Label("R 0");
        this.revenue_total.getStyleClass().add("dashboard-card-total");

        HBox revenue_trend_box = new HBox();
        revenue_trend_box.setMaxWidth(Double.MAX_VALUE);

        this.revenue_trend = new Label("");
        this.revenue_trend.getStyleClass().add("dashboard-card-trend");
        revenue_trend_box.getChildren().addAll(this.revenue_trend);

        Region header_spacer = new Region();
        HBox.setHgrow(header_spacer, Priority.ALWAYS);

        HBox legend_this = new HBox(6);
        legend_this.setAlignment(Pos.CENTER_LEFT);

        Region line_this = new Region();
        line_this.getStyleClass().add("legend-line-gold");
        line_this.setMinSize(18, 3);
        line_this.setPrefSize(18, 3);
        line_this.setMaxSize(18, 3);

        Label legend_this_label = new Label("This week");
        legend_this_label.getStyleClass().add("legend-label");

        legend_this.getChildren().addAll(line_this, legend_this_label);

        HBox legend_last = new HBox(6);
        legend_last.setAlignment(Pos.CENTER_LEFT);

        Line line_last = new Line(0, 0, 18, 0);
        line_last.setStroke(Color.web("#6a6058"));
        line_last.setStrokeWidth(1);
        line_last.getStrokeDashArray().addAll(4.0, 3.0);

        Label legend_last_label = new Label("Last week");
        legend_last_label.getStyleClass().add("legend-label");

        legend_last.getChildren().addAll(line_last, legend_last_label);

        HBox legend_box = new HBox(16);
        legend_box.setAlignment(Pos.CENTER_RIGHT);
        legend_box.getChildren().addAll(legend_this, legend_last);

        revenue_header.getChildren().addAll(revenue_title, header_spacer, legend_box);

        this.revenue_chart = new RevenueChart();
        this.revenue_chart.setMaxWidth(Double.MAX_VALUE);
        this.revenue_chart.setPrefHeight(250);
        this.revenue_chart.setMinHeight(250);
        this.revenue_chart.setMaxHeight(250);

        this.occupancy_chart = new OccupancyChart(this.rooms);
        this.occupancy_chart.setMaxWidth(Double.MAX_VALUE);
        this.occupancy_chart.setPrefHeight(250);
        this.occupancy_chart.setMinHeight(250);
        this.occupancy_chart.setMaxHeight(250);

        revenue_stats.getChildren().addAll(this.revenue_total, revenue_trend_box);

        revenue_card.getChildren().addAll(revenue_header, revenue_stats, this.revenue_chart);
        occupancy_card.getChildren().addAll(occupancy_title, this.occupancy_chart);

        charts_row.add(revenue_card, 0, 0);
        charts_row.add(occupancy_card, 1, 0);

        GridPane bottom_row = new GridPane();
        bottom_row.getStyleClass().add("dashboard-bottom-row");
        bottom_row.setHgap(12);
        bottom_row.setMaxWidth(Double.MAX_VALUE);

        ColumnConstraints heatmapCol = new ColumnConstraints();
        heatmapCol.setPercentWidth(70);
        heatmapCol.setHgrow(Priority.ALWAYS);

        ColumnConstraints activityCol = new ColumnConstraints();
        activityCol.setPercentWidth(30);
        activityCol.setHgrow(Priority.ALWAYS);

        bottom_row.getColumnConstraints().addAll(heatmapCol, activityCol);

        this.heatmap = new OccupancyHeatmap();
        this.activity = new ActivityFeed();

        bottom_row.add(this.heatmap, 0, 0);
        bottom_row.add(this.activity, 1, 0);

        getChildren().addAll(stats_row, charts_row, bottom_row);
    }

    public void setRooms(Room[] rooms, Settings settings) {
        Platform.runLater(() -> {
            this.rooms = rooms;

            if (this.occupancy_chart != null) {
                this.occupancy_chart.setRooms(rooms);
                this.activity.setData(notifications, reservations, payments, roomHistory, serviceTasks, settings);
            }
        });
    }

    public void setNotifications(Notification[] notifications) {
        Platform.runLater(() -> this.notifications = notifications);
    }

    public void setServiceTasks(ServiceTask[] serviceTasks) {
        Platform.runLater(() -> this.serviceTasks = serviceTasks);
    }

    public void setReservations(Reservation[] reservations) {
        Platform.runLater(() -> {
            this.reservations = reservations;

            if (this.stats_row != null) {
                this.stats_row.setData(reservations, rooms, payments);
            }
        });
    }

    public void setPayments(Payment[] payments) {
        Platform.runLater(() -> {
            this.payments = payments;

            if (this.revenue_chart != null) {
                this.revenue_chart.setPayments(payments);
            }

            this.updateRevenueTotal();
        });
    }

    public void setRoomHistory(RoomHistory[] roomHistory, Settings settings) {
        Platform.runLater(() -> {
            this.roomHistory = roomHistory;

            if (this.heatmap != null) {
                this.heatmap.setRoomHistory(this.roomHistory, this.rooms, settings);
            }
        });
    }

    private double calculateRevenue() {
        return calculateRevenueForRange(0);
    }

    private double calculateRevenueForRange(int weeksAgo) {
        if (payments == null || payments.length == 0) return 0;

        LocalDate today = LocalDate.now().minusWeeks(weeksAgo);
        LocalDate monday = today.with(DayOfWeek.MONDAY);

        double revenue = 0;

        for (Payment p : payments) {
            if (!"completed".equals(p.paymentstatus())) continue;

            LocalDate d = p.paymentdate().toLocalDate();

            if (d.isBefore(monday) || d.isAfter(today)) continue;

            revenue += p.amount();
        }

        return revenue;
    }

    public void updateRevenueTotal() {
        if (this.revenue_total == null) return;

        double revenue = this.calculateRevenue();
        this.revenue_total.setText(String.format("R %,.2f", revenue));

        if (this.revenue_trend == null) return;

        double lastWeek = calculateRevenueForRange(1);

        this.revenue_trend.getStyleClass().removeAll("stat-delta-up", "stat-delta-down", "stat-delta-none");

        if (lastWeek <= 0) {
            this.revenue_trend.setText("No data for last week");
            this.revenue_trend.getStyleClass().add("stat-delta-none");
            return;
        }

        double pct = ((revenue - lastWeek) / lastWeek) * 100;
        String arrow = pct >= 0 ? "▲" : "▼";

        this.revenue_trend.setText(String.format("%s %.0f%% vs same day last week", arrow, Math.abs(pct)));
        this.revenue_trend.getStyleClass().add(pct >= 0 ? "stat-delta-up" : "stat-delta-down");
    }
}