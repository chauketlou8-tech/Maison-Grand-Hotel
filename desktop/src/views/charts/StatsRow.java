package views.charts;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import models.Reservation;
import models.Room;
import enums.RoomStatus;
import enums.ReservationStatus;
import records.Payment;

import java.time.DayOfWeek;
import java.time.LocalDate;

public class StatsRow extends HBox {

    private Label arrivalsValue;
    private Label arrivalsSub;
    private Label inHouseValue;
    private Label inHouseSub;
    private Label departuresValue;
    private Label departuresSub;
    private Label revenueValue;
    private Label revenueSub;

    public StatsRow() {
        getStyleClass().add("dashboard-stats-row");
        setSpacing(12);
        setMaxWidth(Double.MAX_VALUE);

        VBox arrivalsCard = statCard("Arrivals");
        this.arrivalsValue = (Label) arrivalsCard.getProperties().get("value");
        this.arrivalsSub = (Label) arrivalsCard.getProperties().get("sub");

        VBox inHouseCard = statCard("In-House");
        this.inHouseValue = (Label) inHouseCard.getProperties().get("value");
        this.inHouseSub = (Label) inHouseCard.getProperties().get("sub");

        VBox departuresCard = statCard("Departures");
        this.departuresValue = (Label) departuresCard.getProperties().get("value");
        this.departuresSub = (Label) departuresCard.getProperties().get("sub");

        VBox revenueCard = statCard("Revenue");
        this.revenueValue = (Label) revenueCard.getProperties().get("value");
        this.revenueSub = (Label) revenueCard.getProperties().get("sub");

        getChildren().addAll(arrivalsCard, inHouseCard, departuresCard, revenueCard);
    }

    private VBox statCard(String label) {
        VBox card = new VBox(0);
        card.getStyleClass().add("stat-card");
        card.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(card, Priority.ALWAYS);

        Label labelLbl = new Label(label.toUpperCase());
        labelLbl.getStyleClass().add("stat-label");

        Label valueLbl = new Label("0");
        valueLbl.getStyleClass().add("stat-value");

        Label subLbl = new Label("");
        subLbl.getStyleClass().add("stat-sub");

        HBox subRow = new HBox(8);
        subRow.setAlignment(Pos.CENTER_LEFT);
        subRow.getChildren().add(subLbl);

        card.getChildren().addAll(labelLbl, valueLbl, subRow);
        card.getProperties().put("value", valueLbl);
        card.getProperties().put("sub", subLbl);

        return card;
    }

    public void setData(Reservation[] reservations, Room[] rooms, Payment[] payments) {
        if (reservations == null || rooms == null || payments == null) return;

        LocalDate today = LocalDate.now();
        LocalDate monday = today.with(DayOfWeek.MONDAY);

        int arrivals = 0;
        for (Reservation r : reservations) {
            if (r.getCheckindate() == null) continue;

            ReservationStatus s = r.getReservationstatus();
            if (s != ReservationStatus.CONFIRMED && s != ReservationStatus.PROCESSING && s != ReservationStatus.PENDING) continue;

            LocalDate d = r.getCheckindate().toLocalDate();
            if (d.equals(today)) arrivals++;
        }

        int inHouse = 0;
        int totalAvailable = 0;
        for (Room room : rooms) {
            RoomStatus status = room.getRoomstatus();

            if (status == RoomStatus.OCCUPIED) inHouse++;

            if (status != RoomStatus.MAINTENANCE && status != RoomStatus.CLOSED) {
                totalAvailable++;
            }
        }

        int departures = 0;
        for (Reservation r : reservations) {
            if (r.getCheckoutdate() == null) continue;
            if (r.getReservationstatus() != ReservationStatus.CONFIRMED) continue;

            LocalDate d = r.getCheckoutdate().toLocalDate();
            if (d.equals(today)) departures++;
        }

        double revenue = 0;
        for (Payment p : payments) {
            if (!"completed".equals(p.paymentstatus())) continue;

            LocalDate d = p.paymentdate().toLocalDate();
            if (d.isBefore(monday) || d.isAfter(today)) continue;

            revenue += p.amount();
        }

        arrivalsValue.setText(String.valueOf(arrivals));
        arrivalsSub.setText("Expected today");

        inHouseValue.setText(String.valueOf(inHouse));
        inHouseSub.setText("of " + totalAvailable + " rooms");

        departuresValue.setText(String.valueOf(departures));
        departuresSub.setText("Checking out today");

        revenueValue.setText(String.format("R %,.2f", revenue));
        revenueSub.setText("Collected this week");
    }
}