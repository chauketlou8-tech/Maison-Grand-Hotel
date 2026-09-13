package views.charts;

import javafx.application.Platform;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
import records.Payment;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class RevenueChart extends HBox {
    private final AreaChart<Number, Number> areaChart;
    private final XYChart.Series<Number, Number> series;
    private final XYChart.Series<Number, Number> lastWeekSeries;
    private final NumberAxis yAxis;
    private final StackPane wrapper;

    public RevenueChart() {
        getStyleClass().add("revenue-chart-container");
        setMaxWidth(Double.MAX_VALUE);

        NumberAxis xAxis = new NumberAxis(0, 6, 1);
        xAxis.setAutoRanging(false);
        xAxis.setMinorTickVisible(false);
        xAxis.setTickMarkVisible(false);
        xAxis.setTickLabelFormatter(new NumberAxis.DefaultFormatter(xAxis) {
            @Override
            public String toString(Number value) {
                int i = value.intValue();
                String[] days = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};
                if (i >= 0 && i < days.length) return days[i];
                return "";
            }
        });

        yAxis = new NumberAxis();
        yAxis.setTickLabelFormatter(new NumberAxis.DefaultFormatter(yAxis, "R", null));

        areaChart = new AreaChart<>(xAxis, yAxis);
        areaChart.getStyleClass().add("revenue-chart");
        areaChart.setLegendVisible(false);
        areaChart.setAnimated(false);
        areaChart.setMaxWidth(Double.MAX_VALUE);
        areaChart.setCreateSymbols(false);
        areaChart.setHorizontalZeroLineVisible(false);
        areaChart.setVerticalZeroLineVisible(false);

        series = new XYChart.Series<>();
        lastWeekSeries = new XYChart.Series<>();

        yAxis.setAutoRanging(false);
        yAxis.setLowerBound(0);
        yAxis.setUpperBound(12250000);
        yAxis.setTickUnit(2450000);

        areaChart.getData().add(lastWeekSeries);
        areaChart.getData().add(series);

        Platform.runLater(() -> {
            if (lastWeekSeries.getNode() != null) {
                lastWeekSeries.getNode().getStyleClass().add("series-last-week");
            }

            if (series.getNode() != null) {
                series.getNode().getStyleClass().add("series-this-week");
            }
        });

        Rectangle skeletonBar = new Rectangle();
        skeletonBar.setFill(Color.web("#1f1d1a"));
        skeletonBar.setArcWidth(3);
        skeletonBar.setArcHeight(3);
        skeletonBar.setWidth(60);
        skeletonBar.setHeight(120);

        StackPane skeleton = new StackPane(skeletonBar);
        skeleton.setStyle("-fx-background-color: #1f1d1a; -fx-background-radius: 3;");
        skeleton.setMaxWidth(Double.MAX_VALUE);

        wrapper = new StackPane(skeleton, areaChart);
        wrapper.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(wrapper, Priority.ALWAYS);

        areaChart.setVisible(false);

        getChildren().add(wrapper);
    }

    public void setPayments(Payment[] payments) {
        if (payments == null) return;

        areaChart.setVisible(true);

        LocalDate today = LocalDate.now();
        LocalDate monday = today.with(DayOfWeek.MONDAY);
        LocalDate lastMonday = monday.minusWeeks(1);
        LocalDate lastSunday = monday.minusDays(1);

        double[] daily = new double[7];
        double[] lastWeek = new double[7];

        for (Payment p : payments) {
            if (!"completed".equals(p.paymentstatus())) continue;

            LocalDate d = p.paymentdate().toLocalDate();

            if (!d.isBefore(monday) && !d.isAfter(today)) {
                int idx = d.getDayOfWeek().getValue() - 1;
                daily[idx] += p.amount();
            }

            else if (!d.isBefore(lastMonday) && !d.isAfter(lastSunday)) {
                int idx = d.getDayOfWeek().getValue() - 1;
                lastWeek[idx] += p.amount();
            }
        }

        double max = 0;
        for (double v : daily) max = Math.max(max, v);
        for (double v : lastWeek) max = Math.max(max, v);

        double upper = max <= 0 ? 1000 : Math.ceil(max / 1000.0) * 1000;
        yAxis.setUpperBound(upper);
        yAxis.setTickUnit(upper / 5);

        double[] xs = {0, 1, 2, 3, 4, 5, 6};

        series.getData().clear();
        for (double[] pt : monotoneSpline(xs, daily)) {
            series.getData().add(new XYChart.Data<>(pt[0], pt[1]));
        }

        lastWeekSeries.getData().clear();
        for (double[] pt : monotoneSpline(xs, lastWeek)) {
            lastWeekSeries.getData().add(new XYChart.Data<>(pt[0], pt[1]));
        }
    }

    private List<double[]> monotoneSpline(double[] xs, double[] ys) {
        int n = xs.length;
        double[] dx = new double[n - 1];
        double[] dy = new double[n - 1];
        double[] slope = new double[n - 1];

        for (int i = 0; i < n - 1; i++) {
            dx[i] = xs[i + 1] - xs[i];
            dy[i] = ys[i + 1] - ys[i];
            slope[i] = dy[i] / dx[i];
        }

        double[] tangent = new double[n];
        tangent[0] = slope[0];
        tangent[n - 1] = slope[n - 2];

        for (int i = 1; i < n - 1; i++) {
            if (slope[i - 1] * slope[i] <= 0) {
                tangent[i] = 0;
            }

            else {
                tangent[i] = (slope[i - 1] + slope[i]) / 2;
            }
        }

        List<double[]> out = new ArrayList<>();

        for (int i = 0; i < n - 1; i++) {
            double x0 = xs[i];
            double x1 = xs[i + 1];
            double y0 = ys[i];
            double y1 = ys[i + 1];
            double m0 = tangent[i] * dx[i];
            double m1 = tangent[i + 1] * dx[i];

            for (int s = 0; s < 20; s++) {
                double t = (double) s / 20;
                double t2 = t * t;
                double t3 = t2 * t;

                double h00 = 2 * t3 - 3 * t2 + 1;
                double h10 = t3 - 2 * t2 + t;
                double h01 = -2 * t3 + 3 * t2;
                double h11 = t3 - t2;

                double x = x0 + t * dx[i];
                double y = h00 * y0 + h10 * m0 + h01 * y1 + h11 * m1;

                out.add(new double[]{x, y});
            }
        }

        out.add(new double[]{xs[n - 1], ys[n - 1]});

        return out;
    }
}