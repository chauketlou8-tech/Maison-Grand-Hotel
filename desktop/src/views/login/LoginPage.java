package views.login;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.util.Duration;
import models.User;

import javafx.scene.layout.*;
import javafx.scene.control.*;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.function.Consumer;

import org.mindrot.jbcrypt.BCrypt;

public class LoginPage extends VBox {
    private final PasswordField password_input;
    private User selectedUser;
    private final Consumer<Boolean> setIsLoggedIn;
    private final Runnable updateScene;
    private StackPane layout;

    public LoginPage(List<User> users, Consumer<Boolean> setIsLoggedIn, Runnable updateScene) {
        this.selectedUser = users.getFirst();
        this.setIsLoggedIn = setIsLoggedIn;
        this.updateScene = updateScene;

        this.layout = new StackPane();
        this.layout.getStyleClass().add("root-pane");

        VBox content = new VBox();
        content.setFillWidth(true);

        HBox header = new HBox();
        header.getStyleClass().add("header");
        VBox children = new VBox();
        HBox footer = new HBox();
        footer.getStyleClass().add("footer");

        //Header
        VBox logoBox = new VBox();
        HBox timeBox = new HBox();

        Label maison = new Label("MAISON");
        maison.getStyleClass().add("maison-label");

        Label grandHotel = new Label("GRAND HÔTEL");
        grandHotel.getStyleClass().add("grand-hotel-label");

        logoBox.getChildren().addAll(maison, grandHotel);
        logoBox.setAlignment(Pos.CENTER_LEFT);

        Label date = new Label();
        date.getStyleClass().add("date-label");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE, d MMM yyyy HH:mm:ss");

        Timeline clock = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            date.setText(LocalDateTime.now().format(formatter).toUpperCase());
        }));

        clock.setCycleCount(Animation.INDEFINITE);
        clock.play();

        timeBox.getChildren().addAll(date);
        timeBox.setAlignment(Pos.CENTER_RIGHT);

        HBox.setHgrow(timeBox, Priority.ALWAYS);
        HBox.setHgrow(logoBox, Priority.ALWAYS);

        header.getChildren().addAll(logoBox, timeBox);
        header.setPadding(new Insets(20));
        header.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(header, Priority.ALWAYS);

        //body
        VBox text_field = new VBox();

        Label signIn = new Label("SIGN IN");
        signIn.getStyleClass().add("sign-in-label");
        signIn.setMaxWidth(Double.MAX_VALUE);

        Label description = new Label("Select your staff account and enter your credentials.");
        description.getStyleClass().add("description-label");
        description.setMaxWidth(Double.MAX_VALUE);

        text_field.getChildren().addAll(signIn, description);
        VBox.setMargin(text_field, new Insets(0, 0, 50, 0));

        HBox fields = new HBox();
        fields.getStyleClass().add("fields-container");
        fields.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(fields, Priority.ALWAYS);
        fields.setSpacing(10);
        fields.setAlignment(Pos.CENTER);

        VBox account_field = new VBox();
        VBox password_field = new VBox();

        account_field.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(account_field, Priority.ALWAYS);
        account_field.setAlignment(Pos.CENTER_LEFT);
        account_field.setSpacing(10);

        password_field.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(password_field, Priority.ALWAYS);
        password_field.setAlignment(Pos.CENTER_LEFT);
        password_field.setSpacing(10);

        Label account_label = new Label("STAFF ACCOUNT");
        account_label.getStyleClass().add("account-label");

        Label password_label = new Label("PASSWORD");
        password_label.getStyleClass().add("password-label");

        ComboBox<User> account_combo = new ComboBox<>();
        account_combo.getStyleClass().add("combo-box-style");
        account_combo.setPadding(new Insets(8));
        account_combo.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(account_combo, Priority.ALWAYS);

        account_combo.setValue(this.selectedUser);
        account_combo.getItems().addAll(users);

        account_combo.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(User user, boolean empty) {
                super.updateItem(user, empty);

                if (empty || user == null) {
                    setText(null);
                } else {
                    setText(user.getName() + " — " + user.getRole());
                }
            }
        });

        account_combo.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(User user, boolean empty) {
                super.updateItem(user, empty);
                if (empty || user == null) {
                    setText(null);
                } else {
                    setText(user.getName() + " — " + user.getRole());
                }
            }
        });

        this.password_input = new PasswordField();
        this.password_input.getStyleClass().add("password-field-style");
        this.password_input.setPromptText("Enter your password...");
        this.password_input.setPadding(new Insets(12));
        this.password_input.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(this.password_input, Priority.ALWAYS);

        account_combo.prefWidthProperty().bind(fields.widthProperty().divide(2));
        this.password_input.prefWidthProperty().bind(fields.widthProperty().divide(2));

        HBox role_badge = new HBox();
        role_badge.getStyleClass().add("badge");
        role_badge.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(role_badge, Priority.ALWAYS);

        HBox initials = new HBox();
        initials.getStyleClass().add("initials");
        initials.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(initials, Priority.ALWAYS);

        Label initials_label = new Label(getInitials(this.selectedUser.getName()));
        initials_label.getStyleClass().add("initials-label");
        initials.getChildren().add(initials_label);

        VBox text_box = new VBox();
        text_box.getStyleClass().add("text-box");
        HBox.setHgrow(text_box, Priority.ALWAYS);

        Label nm_label = new Label(this.selectedUser.getName());
        nm_label.getStyleClass().add("name-label");

        Label email_label = new Label(this.selectedUser.getEmail());
        email_label.getStyleClass().add("email-label");

        text_box.getChildren().addAll(nm_label, email_label);

        HBox name_panel = new HBox();
        name_panel.getStyleClass().add("name-panel");
        name_panel.getChildren().addAll(initials, text_box);
        name_panel.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(name_panel, Priority.ALWAYS);

        HBox role_panel = new HBox();
        role_panel.getStyleClass().add("role-badge");
        role_panel.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(role_panel, Priority.ALWAYS);
        role_panel.setAlignment(Pos.CENTER_RIGHT);

        Label role_label = new Label(String.valueOf(this.selectedUser.getRole()).toUpperCase());
        role_label.getStyleClass().add("role-label");
        role_panel.getChildren().add(role_label);

        role_badge.getChildren().addAll(name_panel, role_panel);

        Button login_button = new Button("LOGIN");
        login_button.getStyleClass().add("login-button");
        login_button.setPadding(new Insets(12));
        login_button.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(login_button, Priority.ALWAYS);
        VBox.setMargin(login_button, new Insets(10, 0, 10, 0));

        account_field.getChildren().addAll(account_label, account_combo);
        password_field.getChildren().addAll(password_label, password_input);

        fields.getChildren().addAll(account_field, password_field);
        children.getChildren().addAll(text_field, fields, role_badge, login_button);

        children.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        HBox.setHgrow(children, Priority.ALWAYS);
        VBox.setVgrow(children, Priority.ALWAYS);
        children.setAlignment(Pos.CENTER);
        children.setSpacing(10);
        children.setPadding(new Insets(20, 50, 20, 50));

        //Footer
        HBox left_panel = new HBox();
        HBox right_panel = new HBox();

        HBox system_status = new HBox();
        HBox divider1 = new HBox();
        HBox divider2 = new HBox();
        HBox name = new HBox();
        HBox last_sync = new HBox();

        Label systemStatus = new Label("System Online");
        systemStatus.getStyleClass().add("footer-label");
        Label divider_label1 = new Label("|");
        divider_label1.getStyleClass().add("footer-label");
        Label name_label = new Label("Maison Grand Hotel PMS v1.0.0");
        name_label.getStyleClass().add("footer-label");

        String d = LocalTime.now().format(DateTimeFormatter.ofPattern("hh:mm a")).toUpperCase();
        Label last_sync_label = new Label("Last Sync: " + d);
        last_sync_label.getStyleClass().add("footer-label");
        Label divider_label2 = new Label("|");
        divider_label2.getStyleClass().add("footer-label");

        system_status.getChildren().addAll(systemStatus);
        divider1.getChildren().add(divider_label1);
        name.getChildren().add(name_label);
        divider2.getChildren().add(divider_label2);
        last_sync.getChildren().add(last_sync_label);

        left_panel.getChildren().addAll(systemStatus, divider1, name, divider2, last_sync);

        Label copyright = new Label("© " + Year.now().getValue() + " Maison Grand Hôtel");
        copyright.getStyleClass().add("copyright-label");

        right_panel.getChildren().add(copyright);

        HBox.setHgrow(right_panel, Priority.ALWAYS);
        HBox.setHgrow(left_panel, Priority.ALWAYS);

        left_panel.setAlignment(Pos.CENTER_LEFT);
        right_panel.setAlignment(Pos.CENTER_RIGHT);

        left_panel.setSpacing(10);

        footer.getChildren().addAll(left_panel, right_panel);

        footer.setPadding(new Insets(10, 20, 10, 20));
        footer.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(footer, Priority.ALWAYS);

        content.getChildren().addAll(header, children, footer);
        VBox.setVgrow(content, Priority.ALWAYS);

        this.layout.getChildren().add(content);
        VBox.setVgrow(this.layout, Priority.ALWAYS);

        getChildren().add(this.layout);
        this.setFillWidth(true);
        VBox.setVgrow(this, Priority.ALWAYS);

        account_combo.valueProperty().addListener((ov, oldValue, newValue) -> {
            if (newValue != null) {
                this.selectedUser = newValue;
            }

            this.updateUser(initials_label, nm_label, email_label, role_label);
        });

        login_button.setOnAction(e -> this.login());
    }

    private String getInitials(String name) {
        if (name.isEmpty()) {
            return "";
        }

        String[] n = name.split(" ");

        if (n.length == 1) {
            String nm = n[0];
            return nm.substring(0, 1).toUpperCase();
        } else if (n.length == 2) {
            String firstname = n[0];
            String lastname = n[1];
            return String.valueOf(new char[]{firstname.charAt(0), lastname.charAt(0)}).toUpperCase();
        }

        StringBuilder initials = new StringBuilder();
        for (String nm : n) {
            initials.append(String.valueOf(nm.charAt(0)).toUpperCase());
        }
        return initials.toString();
    }

    private void updateUser(Label initials_label, Label nm_label, Label email_label, Label role_label) {
        initials_label.setText(getInitials(this.selectedUser.getName()));
        nm_label.setText(this.selectedUser.getName());
        email_label.setText(this.selectedUser.getEmail());
        role_label.setText(String.valueOf(this.selectedUser.getRole()).toUpperCase());
    }

    private void login() {
        String password = this.password_input.getText();

        // Error label
        Label errorLabel = new Label();
        errorLabel.getStyleClass().add("error-label");
        errorLabel.setMaxWidth(Double.MAX_VALUE);
        errorLabel.setVisible(false);

        if (password.isEmpty()) {
            errorLabel.setText("Please enter your password");
            errorLabel.setVisible(true);
            this.password_input.getStyleClass().add("error");
            // Add to layout or show somewhere
            return;
        }

        String hash = this.selectedUser.getPassword_hash().trim();
        if (hash.startsWith("$2b$")) {
            hash = "$2a$" + hash.substring(4);
        }

        Button loginButton = (Button) this.lookup(".login-button");
        loginButton.setDisable(true);
        loginButton.setText("SIGNING IN...");
        this.password_input.setDisable(true);

        VBox loaderContent = new VBox(20);
        loaderContent.setAlignment(Pos.CENTER);
        loaderContent.setPadding(new Insets(40, 50, 40, 50));
        loaderContent.setStyle("-fx-background-color: rgba(15,14,12,0.95); -fx-border-color: rgba(201,168,76,0.2); -fx-border-width: 1px;");

        ProgressIndicator loader = new ProgressIndicator();
        loader.setMaxSize(48, 48);
        loader.setStyle("-fx-progress-color: #c9a84c;");

        Label loadingText = new Label("VERIFYING CREDENTIALS");
        loadingText.setStyle("-fx-text-fill: #c9a84c; -fx-font-size: 12px; -fx-font-weight: bold; -fx-font-family: 'DM Mono',monospace; -fx-letter-spacing: 2px;");

        loaderContent.getChildren().addAll(loader, loadingText);

        StackPane overlay = new StackPane(loaderContent);
        overlay.setStyle("-fx-background-color: rgba(10,9,8,0.75);");
        overlay.setAlignment(Pos.CENTER);

        this.layout.getChildren().add(overlay);
        StackPane.setAlignment(overlay, Pos.CENTER);

        long startTime = System.currentTimeMillis();
        String finalHash = hash;

        Task<Boolean> task = new Task<>() {
            @Override
            protected Boolean call() {
                return BCrypt.checkpw(password, finalHash);
            }
        };

        task.setOnSucceeded(e -> {
            long elapsed = System.currentTimeMillis() - startTime;
            long remaining = 3000 - elapsed;

            Runnable finish = () -> {
                this.layout.getChildren().remove(overlay);
                loginButton.setDisable(false);
                loginButton.setText("LOGIN");
                this.password_input.setDisable(false);

                if (task.getValue()) {
                    this.setIsLoggedIn.accept(true);
                    this.updateScene.run();
                } else {
                    // Show error for wrong password
                    errorLabel.setText("Invalid password. Please try again.");
                    errorLabel.setVisible(true);
                    this.password_input.getStyleClass().add("error");
                    this.password_input.clear();
                    this.password_input.requestFocus();
                    // Add errorLabel to layout
                    VBox layout = (VBox) this.layout.getChildren().get(0);
                    layout.getChildren().add(errorLabel);
                }
            };

            if (remaining > 0) {
                PauseTransition pause = new PauseTransition(Duration.millis(remaining));
                pause.setOnFinished(event -> finish.run());
                pause.play();
            } else {
                finish.run();
            }
        });

        task.setOnFailed(e -> {
            this.layout.getChildren().remove(overlay);
            loginButton.setDisable(false);
            loginButton.setText("LOGIN");
            this.password_input.setDisable(false);
            errorLabel.setText("Authentication error. Please try again.");
            errorLabel.setVisible(true);
        });

        new Thread(task).start();
    }
}