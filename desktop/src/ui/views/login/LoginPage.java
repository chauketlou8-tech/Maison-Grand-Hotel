package ui.views.login;

import javafx.animation.*;
import javafx.concurrent.Task;
import javafx.geometry.*;

import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;

import javafx.util.Duration;

import models.User;

import java.time.*;
import java.time.format.DateTimeFormatter;

import java.util.List;
import java.util.function.Consumer;

import org.mindrot.jbcrypt.BCrypt;

public class LoginPage extends VBox {
    private final Consumer<Boolean> set_login;
    private final Consumer<User> set_user;
    private final Runnable update_scene;

    private final StackPane page_stack;

    private final PasswordField password_input;
    private final ComboBox<User> account_combo;

    private final Label initials_label;
    private final Label name_label;
    private final Label email_label;

    private final Label role_label;
    private final Label error_label;

    private final Button login_button;

    private User selected_user;

    public LoginPage(List<User> users, Consumer<Boolean> set_login, Runnable update_scene, Consumer<User> set_user) {
        this.selected_user = users.getFirst();
        this.set_login = set_login;

        this.set_user = set_user;
        this.update_scene = update_scene;

        this.page_stack = new StackPane();
        this.page_stack.getStyleClass().add("root-pane");

        VBox page_box = new VBox();
        page_box.setFillWidth(true);

        HBox header_box = new HBox();
        header_box.getStyleClass().add("header");

        VBox body_box = new VBox();
        HBox footer_box = new HBox();

        footer_box.getStyleClass().add("footer");

        //Header
        VBox logo_box = new VBox();
        HBox time_box = new HBox();

        Label maison_label = new Label("MAISON");
        maison_label.getStyleClass().add("maison-label");

        Label hotel_label = new Label("GRAND HÔTEL");
        hotel_label.getStyleClass().add("grand-hotel-label");

        logo_box.getChildren().addAll(maison_label, hotel_label);
        logo_box.setAlignment(Pos.CENTER_LEFT);

        Label clock_label = new Label();
        clock_label.getStyleClass().add("date-label");

        DateTimeFormatter clock_format = DateTimeFormatter.ofPattern("EEE, d MMM yyyy HH:mm:ss");
        Timeline clock = new Timeline(new KeyFrame(Duration.seconds(1), _ -> clock_label.setText(LocalDateTime.now().format(clock_format).toUpperCase())));

        clock.setCycleCount(Animation.INDEFINITE);
        clock.play();

        time_box.getChildren().addAll(clock_label);
        time_box.setAlignment(Pos.CENTER_RIGHT);

        HBox.setHgrow(time_box, Priority.ALWAYS);
        HBox.setHgrow(logo_box, Priority.ALWAYS);

        header_box.getChildren().addAll(logo_box, time_box);

        header_box.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(header_box, Priority.ALWAYS);

        header_box.setPadding(new Insets(20));

        //body
        VBox head_box = new VBox();

        Label sign_label = new Label("SIGN IN");
        sign_label.getStyleClass().add("sign-in-label");

        sign_label.setMaxWidth(Double.MAX_VALUE);
        Label desc_label = new Label("Select your staff account and enter your credentials.");

        desc_label.getStyleClass().add("description-label");
        desc_label.setMaxWidth(Double.MAX_VALUE);

        head_box.getChildren().addAll(sign_label, desc_label);
        VBox.setMargin(head_box, new Insets(0, 0, 50, 0));

        HBox form_row = new HBox();
        form_row.getStyleClass().add("fields-container");

        form_row.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(form_row, Priority.ALWAYS);
        form_row.setAlignment(Pos.CENTER);

        form_row.setSpacing(10);
        VBox account_box = new VBox();

        VBox password_box = new VBox();

        account_box.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(account_box, Priority.ALWAYS);
        account_box.setAlignment(Pos.CENTER_LEFT);

        account_box.setSpacing(10);

        password_box.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(password_box, Priority.ALWAYS);
        password_box.setAlignment(Pos.CENTER_LEFT);

        password_box.setSpacing(10);
        Label account_label = new Label("STAFF ACCOUNT");

        account_label.getStyleClass().add("account-label");
        Label password_label = new Label("PASSWORD");

        password_label.getStyleClass().add("password-label");

        this.account_combo = new ComboBox<>();
        this.account_combo.getStyleClass().add("combo-box-style");

        this.account_combo.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(this.account_combo, Priority.ALWAYS);

        this.account_combo.setPadding(new Insets(8));

        this.account_combo.setValue(this.selected_user);
        this.account_combo.getItems().addAll(users);

        this.password_input = new PasswordField();

        this.password_input.getStyleClass().add("password-field-style");
        this.password_input.setPromptText("Enter your password...");

        this.password_input.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(this.password_input, Priority.ALWAYS);

        this.password_input.setPadding(new Insets(12));

        this.account_combo.prefWidthProperty().bind(form_row.widthProperty().divide(2));
        this.password_input.prefWidthProperty().bind(form_row.widthProperty().divide(2));

        HBox user_badge = new HBox();
        user_badge.getStyleClass().add("badge");

        user_badge.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(user_badge, Priority.ALWAYS);

        HBox initials_box = new HBox();
        initials_box.getStyleClass().add("initials");

        initials_box.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(initials_box, Priority.ALWAYS);

        this.initials_label = new Label(get_initials(this.selected_user.getName()));
        this.initials_label.getStyleClass().add("initials-label");

        initials_box.getChildren().add(this.initials_label);
        VBox text_box = new VBox();

        text_box.getStyleClass().add("text-box");
        HBox.setHgrow(text_box, Priority.ALWAYS);

        this.name_label = new Label(this.selected_user.getName());
        this.name_label.getStyleClass().add("name-label");

        this.email_label = new Label(this.selected_user.getEmail());
        this.email_label.getStyleClass().add("email-label");

        text_box.getChildren().addAll(this.name_label, this.email_label);

        HBox name_panel = new HBox();

        name_panel.getStyleClass().add("name-panel");

        name_panel.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(name_panel, Priority.ALWAYS);

        name_panel.getChildren().addAll(initials_box, text_box);

        HBox role_panel = new HBox();

        role_panel.getStyleClass().add("role-badge");

        role_panel.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(role_panel, Priority.ALWAYS);
        role_panel.setAlignment(Pos.CENTER_RIGHT);

        this.role_label = new Label(String.valueOf(this.selected_user.getRole()).toUpperCase());
        this.role_label.getStyleClass().add("role-label");

        role_panel.getChildren().add(this.role_label);

        user_badge.getChildren().addAll(name_panel, role_panel);

        this.login_button = new Button("LOGIN");
        this.login_button.getStyleClass().add("login-button");

        this.login_button.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(this.login_button, Priority.ALWAYS);

        this.login_button.setPadding(new Insets(12));
        this.error_label = new Label();

        this.error_label.getStyleClass().add("error-label");
        this.error_label.setMaxWidth(Double.MAX_VALUE);

        this.error_label.setVisible(false);
        this.error_label.setManaged(false);

        account_box.getChildren().addAll(account_label, this.account_combo);
        password_box.getChildren().addAll(password_label, this.password_input);

        form_row.getChildren().addAll(account_box, password_box);
        body_box.getChildren().addAll(head_box, form_row, user_badge, this.error_label, this.login_button);

        body_box.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        HBox.setHgrow(body_box, Priority.ALWAYS);
        VBox.setVgrow(body_box, Priority.ALWAYS);

        body_box.setAlignment(Pos.CENTER);
        body_box.setSpacing(10);

        body_box.setPadding(new Insets(20, 50, 20, 50));

        //Footer
        HBox left_panel = new HBox();
        HBox right_panel = new HBox();

        HBox status_box = new HBox();
        HBox divider_one = new HBox();
        HBox divider_two = new HBox();
        HBox app_box = new HBox();
        HBox sync_box = new HBox();

        Label status_label = new Label("System Online");

        status_label.getStyleClass().add("footer-label");
        Label div_one_label = new Label("|");

        div_one_label.getStyleClass().add("footer-label");
        Label app_label = new Label("Maison Grand Hotel PMS v1.0.0");

        app_label.getStyleClass().add("footer-label");

        String sync_time = LocalTime.now().format(DateTimeFormatter.ofPattern("hh:mm a")).toUpperCase();
        Label sync_label = new Label("Last Sync: " + sync_time);

        sync_label.getStyleClass().add("footer-label");
        Label div_two_label = new Label("|");

        div_two_label.getStyleClass().add("footer-label");

        status_box.getChildren().addAll(status_label);
        divider_one.getChildren().add(div_one_label);
        app_box.getChildren().add(app_label);

        divider_two.getChildren().add(div_two_label);
        sync_box.getChildren().add(sync_label);

        left_panel.getChildren().addAll(status_label, divider_one, app_box, divider_two, sync_box);

        Label copyright = new Label("© " + Year.now().getValue() + " Maison Grand Hôtel");
        copyright.getStyleClass().add("copyright-label");

        right_panel.getChildren().add(copyright);

        HBox.setHgrow(right_panel, Priority.ALWAYS);
        HBox.setHgrow(left_panel, Priority.ALWAYS);
        left_panel.setAlignment(Pos.CENTER_LEFT);
        right_panel.setAlignment(Pos.CENTER_RIGHT);

        left_panel.setSpacing(10);

        footer_box.getChildren().addAll(left_panel, right_panel);

        footer_box.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(footer_box, Priority.ALWAYS);

        footer_box.setPadding(new Insets(10, 20, 10, 20));

        page_box.getChildren().addAll(header_box, body_box, footer_box);

        VBox.setVgrow(page_box, Priority.ALWAYS);

        this.page_stack.getChildren().add(page_box);

        VBox.setVgrow(this.page_stack, Priority.ALWAYS);

        getChildren().add(this.page_stack);

        this.setFillWidth(true);
        VBox.setVgrow(this, Priority.ALWAYS);

        this.account_combo.setCellFactory(_ -> new ListCell<>() {
            @Override
            protected void updateItem(User user, boolean empty) {
                super.updateItem(user, empty);

                if (empty || user == null) {
                    setText(null);
                }

                else {
                    setText(user.getName() + " — " + user.getRole());
                }
            }
        });

        this.account_combo.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(User user, boolean empty) {
                super.updateItem(user, empty);

                if (empty || user == null) {
                    setText(null);
                }

                else {
                    setText(user.getName() + " — " + user.getRole());
                }
            }
        });

        this.account_combo.valueProperty().addListener((_, _, new_value) -> {
            if (new_value != null) {
                this.selected_user = new_value;
            }

            this.update_user();
        });

        this.login_button.setOnAction(_ -> this.attempt_login());

        this.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                this.attempt_login();
            }
        });
    }

    public static String get_initials(String name) {
        if (name.isEmpty()) {
            return "";
        }

        String[] parts = name.split(" ");

        if (parts.length == 1) {
            return parts[0].substring(0, 1).toUpperCase();
        }

        if (parts.length == 2) {
            return String.valueOf(new char[]{parts[0].charAt(0), parts[1].charAt(0)}).toUpperCase();
        }

        StringBuilder result = new StringBuilder();

        for (String part : parts) {
            result.append(String.valueOf(part.charAt(0)).toUpperCase());
        }

        return result.toString();
    }

    private void update_user() {
        this.initials_label.setText(get_initials(this.selected_user.getName()));
        this.name_label.setText(this.selected_user.getName());

        this.email_label.setText(this.selected_user.getEmail());
        this.role_label.setText(String.valueOf(this.selected_user.getRole()).toUpperCase());
    }

    private void reset_form() {
        this.password_input.clear();
        this.password_input.getStyleClass().remove("error");

        this.password_input.setDisable(false);

        this.error_label.setText("");

        this.error_label.setVisible(false);
        this.error_label.setManaged(false);

        this.login_button.setText("LOGIN");

        this.login_button.setDisable(false);

        this.selected_user = this.account_combo.getItems().isEmpty() ? null : this.account_combo.getItems().getFirst();

        if (this.selected_user != null) {
            this.account_combo.setValue(this.selected_user);

            this.update_user();
        }

        this.password_input.requestFocus();
    }

    private void attempt_login() {
        String password = this.password_input.getText();

        if (password.isEmpty()) {
            this.error_label.setText("Please enter your password");
            this.password_input.getStyleClass().add("error");

            this.error_label.setVisible(true);
            this.error_label.setManaged(true);

            return;
        }

        String hash = this.selected_user.getPassword_hash().trim();
        if (hash.startsWith("$2b$")) {
            hash = "$2a$" + hash.substring(4);
        }

        this.login_button.setText("SIGNING IN...");

        this.login_button.setDisable(true);
        this.password_input.setDisable(true);

        VBox loader_content = new VBox(20);

        loader_content.setStyle("-fx-background-color: rgba(15,14,12,0.95); -fx-border-color: rgba(201,168,76,0.2); -fx-border-width: 1px;");

        loader_content.setPadding(new Insets(40, 50, 40, 50));
        loader_content.setAlignment(Pos.CENTER);

        ProgressIndicator loader = new ProgressIndicator();

        loader.setStyle("-fx-progress-color: #c9a84c;");

        loader.setMaxSize(48, 48);

        Label loading_label = new Label("VERIFYING CREDENTIALS");

        loading_label.setStyle("-fx-text-fill: #c9a84c; -fx-font-size: 12px; -fx-font-weight: bold; -fx-font-family: 'DM Mono',monospace; -fx-letter-spacing: 2px;");

        loader_content.getChildren().addAll(loader, loading_label);

        StackPane overlay = new StackPane(loader_content);

        overlay.setStyle("-fx-background-color: rgba(10,9,8,0.75);");

        overlay.setAlignment(Pos.CENTER);

        this.page_stack.getChildren().add(overlay);
        StackPane.setAlignment(overlay, Pos.CENTER);

        long start_time = System.currentTimeMillis();
        String final_hash = hash;

        Task<Boolean> task = new Task<>() {
            @Override
            protected Boolean call() {
                return BCrypt.checkpw(password, final_hash);
            }
        };

        task.setOnSucceeded(_ -> {
            long elapsed = System.currentTimeMillis() - start_time;
            long remaining = 3000 - elapsed;

            Runnable finish = () -> {
                this.page_stack.getChildren().remove(overlay);

                this.login_button.setText("LOGIN");

                this.login_button.setDisable(false);
                this.password_input.setDisable(false);

                if (task.getValue()) {
                    User logged_in = this.selected_user;

                    this.set_login.accept(true);
                    this.set_user.accept(logged_in);

                    this.reset_form();

                    this.update_scene.run();
                }

                else {
                    this.error_label.setText("Invalid password. Please try again.");
                    this.password_input.getStyleClass().add("error");

                    this.password_input.clear();
                    this.password_input.requestFocus();

                    this.error_label.setVisible(true);
                    this.error_label.setManaged(true);
                }
            };

            if (remaining > 0) {
                PauseTransition pause = new PauseTransition(Duration.millis(remaining));
                pause.setOnFinished(_ -> finish.run());
                pause.play();
            }

            else {
                finish.run();
            }
        });

        task.setOnFailed(_ -> {
            this.page_stack.getChildren().remove(overlay);

            this.login_button.setText("LOGIN");
            this.error_label.setText("Authentication error. Please try again.");

            this.login_button.setDisable(false);
            this.password_input.setDisable(false);

            this.error_label.setVisible(true);
            this.error_label.setManaged(true);
        });

        new Thread(task).start();
    }
}