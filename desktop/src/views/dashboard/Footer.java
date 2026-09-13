package views.dashboard;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import models.User;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Footer extends HBox {

    public Footer(User currentUser) {

        getStyleClass().add("status-bar");
        setAlignment(Pos.CENTER_LEFT);
        setMinHeight(24);
        setPrefHeight(24);
        setMaxHeight(24);
        setPadding(new Insets(0, 28, 0, 28));
        setSpacing(12);
        setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(this, Priority.ALWAYS);

        Circle statusDot = new Circle(3);
        statusDot.setFill(Color.web("#4a9c5a"));

        Label system_label = new Label("System Ready");
        system_label.getStyleClass().add("status-label");

        HBox system_box = new HBox(6, statusDot, system_label);
        system_box.setAlignment(Pos.CENTER_LEFT);

        Label divider3 = new Label("·");
        divider3.getStyleClass().add("status-divider");

        Label user_label = new Label(currentUser.getName() + " · " + String.valueOf(currentUser.getRole()));
        user_label.getStyleClass().add("status-label");

        Label divider4 = new Label("·");
        divider4.getStyleClass().add("status-divider");

        Label hotel_label = new Label("Maison Grand Hotel — Cape Town");
        hotel_label.getStyleClass().add("status-label");

        Label divider5 = new Label("·");
        divider5.getStyleClass().add("status-divider");

        String d = LocalTime.now().format(DateTimeFormatter.ofPattern("hh:mm a"));
        Label last_sync_label = new Label("Synced " + d);
        last_sync_label.getStyleClass().add("status-label");

        getChildren().addAll(system_box, divider3, user_label, divider4, hotel_label, divider5, last_sync_label);
    }
}