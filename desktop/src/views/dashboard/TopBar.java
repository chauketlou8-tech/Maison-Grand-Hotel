package views.dashboard;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcons;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import models.User;
import views.login.LoginPage;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class TopBar extends HBox {
    private final User currentUser;

    public TopBar(User currentUser) {
        this.currentUser = currentUser;

        getStyleClass().add("dashboard-header");
        setAlignment(Pos.CENTER_LEFT);
        setMinHeight(80);
        setPrefHeight(80);
        setPadding(new Insets(0, 28, 0, 28));
        setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(this, Priority.ALWAYS);

        HBox left_header_panel = new HBox();
        left_header_panel.getStyleClass().add("header-left-panel");
        left_header_panel.setAlignment(Pos.CENTER_LEFT);
        left_header_panel.setSpacing(20);
        left_header_panel.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(left_header_panel, Priority.ALWAYS);

        HBox right_header_panel = new HBox();
        right_header_panel.getStyleClass().add("header-right-panel");
        right_header_panel.setAlignment(Pos.CENTER_RIGHT);
        right_header_panel.setSpacing(16);
        right_header_panel.setMaxWidth(Double.MAX_VALUE);

        VBox greetings_header = new VBox();
        greetings_header.getStyleClass().add("greetings-header");
        greetings_header.setAlignment(Pos.CENTER_LEFT);

        Label divider = new Label("|");
        divider.getStyleClass().add("header-divider");

        HBox search_bar = new HBox();
        search_bar.getStyleClass().add("search-bar");
        search_bar.setAlignment(Pos.CENTER_LEFT);
        search_bar.setSpacing(10);

        Label greetings_label = new Label(this.getGreetings());
        greetings_label.getStyleClass().add("greetings-label");

        Label name_label = new Label(this.getName().toUpperCase());
        name_label.getStyleClass().add("header-name-label");

        greetings_header.getChildren().addAll(greetings_label, name_label);

        FontAwesomeIcon search = new FontAwesomeIcon();
        search.setIcon(FontAwesomeIcons.SEARCH);
        search.setSize("13");
        search.setFill(Color.web("#6a6058"));

        TextField search_input = new TextField();
        search_input.setPromptText("Search guest, booking, room…");
        search_input.getStyleClass().add("search-input");
        search_input.setPrefHeight(30);
        search_input.setMinHeight(30);
        search_input.setMaxHeight(30);

        search_bar.getChildren().addAll(search, search_input);

        left_header_panel.getChildren().addAll(greetings_header, divider, search_bar);

        Label date_label = new Label();
        date_label.getStyleClass().add("header-date-label");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE, d MMM yyyy HH:mm:ss");

        Timeline clock = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            date_label.setText(LocalDateTime.now().format(formatter).toUpperCase());
        }));

        clock.setCycleCount(Animation.INDEFINITE);
        clock.play();

        Label divider2 = new Label("|");
        divider2.getStyleClass().add("header-divider");

        HBox name_not_box = new HBox();
        name_not_box.getStyleClass().add("name-not-box");
        name_not_box.setAlignment(Pos.CENTER_RIGHT);
        name_not_box.setSpacing(16);

        StackPane notification_box = new StackPane();
        notification_box.getStyleClass().add("notification-box");
        notification_box.setMinSize(36, 36);
        notification_box.setPrefSize(36, 36);
        notification_box.setMaxSize(36, 36);

        FontAwesomeIcon notification_bell = new FontAwesomeIcon();
        notification_bell.setIcon(FontAwesomeIcons.BELL);
        notification_bell.setSize("15");
        notification_bell.setFill(Color.web("#6a6058"));

        Circle badge_circle = new Circle(7.5);
        badge_circle.setFill(Color.web("#c9a84c"));

        Label badge_label = new Label("0");
        badge_label.getStyleClass().add("notification-badge-label");

        StackPane badge = new StackPane(badge_circle, badge_label);
        badge.setMaxSize(15, 15);
        badge.setMinSize(15, 15);
        badge.setPrefSize(15, 15);
        badge.setVisible(false);
        badge.setManaged(false);

        StackPane.setAlignment(badge, Pos.TOP_RIGHT);
        StackPane.setMargin(badge, new Insets(-1, -1, 0, 0));

        notification_box.getChildren().addAll(notification_bell, badge);

        HBox initials = new HBox();
        initials.getStyleClass().add("initials");
        initials.setAlignment(Pos.CENTER);
        initials.setMinSize(36, 36);
        initials.setPrefSize(36, 36);
        initials.setMaxSize(36, 36);

        Label initials_label = new Label(LoginPage.getInitials(this.currentUser.getName()));
        initials_label.getStyleClass().add("initials-label");
        initials.getChildren().add(initials_label);

        VBox text_box = new VBox();
        text_box.getStyleClass().add("text-box");
        text_box.setAlignment(Pos.CENTER_LEFT);
        text_box.setSpacing(2);

        Label nm_label = new Label(this.currentUser.getName());
        nm_label.getStyleClass().add("user-name-label");

        Label role_label = new Label(String.valueOf(this.currentUser.getRole()));
        role_label.getStyleClass().add("user-role-label");

        text_box.getChildren().addAll(nm_label, role_label);

        HBox user_chip = new HBox(12, initials, text_box);
        user_chip.getStyleClass().add("user-chip");
        user_chip.setAlignment(Pos.CENTER_LEFT);

        name_not_box.getChildren().addAll(notification_box, user_chip);

        right_header_panel.getChildren().addAll(date_label, divider2, name_not_box);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        getChildren().addAll(left_header_panel, spacer, right_header_panel);
    }

    private String getGreetings() {
        LocalTime time = LocalTime.now();
        int hour = time.getHour();

        if (hour >= 5 && hour < 12) return "Good Morning";
        if (hour >= 12 && hour < 17) return "Good Afternoon";
        if (hour >= 17 && hour < 21) return "Good Evening";

        return "Good Night";
    }

    private String getName() {
        String[] names = this.currentUser.getName().split(" ");
        return names[0];
    }
}