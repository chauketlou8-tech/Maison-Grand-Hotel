package ui.newModals;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.Scene;
import javafx.scene.paint.Color;

public class NewGuest extends VBox {

    private final TextField guestIdField;
    private final TextField firstNameField;
    private final TextField lastNameField;
    private final TextField emailField;
    private final TextField phoneField;
    private final TextField idNumberField;
    private final TextField nationalityField;
    private final DatePicker dobPicker;
    private final TextField roomIdField;

    private final TextField streetField;
    private final TextField cityField;
    private final TextField provinceField;
    private final TextField countryField;
    private final TextField postalCodeField;

    public NewGuest(Runnable close) {
        getStyleClass().add("booking-record");
        setSpacing(12);
        setPadding(new Insets(20, 24, 20, 24));
        setMaxWidth(Double.MAX_VALUE);

        HBox header = new HBox();
        header.getStyleClass().add("booking-record-header");
        header.setAlignment(Pos.CENTER_LEFT);

        Label title = new Label("New Guest");
        title.getStyleClass().add("booking-record-title");
        HBox.setHgrow(title, Priority.ALWAYS);
        title.setMaxWidth(Double.MAX_VALUE);

        Button closeBtn = new Button("✕");
        closeBtn.getStyleClass().add("booking-record-close");
        closeBtn.setOnAction(e -> close.run());

        header.getChildren().addAll(title, closeBtn);

        Label personalHeader = new Label("Personal");
        personalHeader.getStyleClass().add("booking-record-section-title");

        guestIdField = field("Guest ID");
        firstNameField = field("First name");
        lastNameField = field("Last name");
        emailField = field("Email");
        phoneField = field("Phone");
        idNumberField = field("ID number");
        nationalityField = field("Nationality");

        dobPicker = new DatePicker();
        dobPicker.getStyleClass().add("registry-input");
        dobPicker.setMaxWidth(Double.MAX_VALUE);

        roomIdField = field("Room ID");

        Label addressHeader = new Label("Address");
        addressHeader.getStyleClass().add("booking-record-section-title");

        streetField = field("Street");
        cityField = field("City");
        provinceField = field("Province");
        countryField = field("Country");
        postalCodeField = field("Postal code");

        GridPane grid = new GridPane();
        grid.setHgap(12);
        grid.setVgap(8);
        grid.setMaxWidth(Double.MAX_VALUE);

        grid.add(labeled("GUEST ID", guestIdField), 0, 0);
        grid.add(labeled("ROOM ID", roomIdField), 1, 0);
        grid.add(labeled("FIRST NAME", firstNameField), 0, 1);
        grid.add(labeled("LAST NAME", lastNameField), 1, 1);
        grid.add(labeled("EMAIL", emailField), 0, 2);
        grid.add(labeled("PHONE", phoneField), 1, 2);
        grid.add(labeled("ID NUMBER", idNumberField), 0, 3);
        grid.add(labeled("NATIONALITY", nationalityField), 1, 3);
        grid.add(labeled("DATE OF BIRTH", dobPicker), 0, 4);

        grid.add(labeled("STREET", streetField), 0, 5);
        grid.add(labeled("CITY", cityField), 1, 5);
        grid.add(labeled("PROVINCE", provinceField), 0, 6);
        grid.add(labeled("COUNTRY", countryField), 1, 6);
        grid.add(labeled("POSTAL CODE", postalCodeField), 0, 7);

        ColumnConstraints c1 = new ColumnConstraints();
        c1.setPercentWidth(50);
        ColumnConstraints c2 = new ColumnConstraints();
        c2.setPercentWidth(50);
        grid.getColumnConstraints().addAll(c1, c2);

        Button saveBtn = new Button("Save");
        saveBtn.getStyleClass().add("registry-button-primary");

        HBox footer = new HBox(saveBtn);
        footer.setAlignment(Pos.CENTER_RIGHT);
        footer.setMaxWidth(Double.MAX_VALUE);

        saveBtn.setOnAction(e -> save());

        getChildren().addAll(header, new Separator(), personalHeader, grid, addressHeader, new Separator(), footer);
    }

    private TextField field(String prompt) {
        TextField tf = new TextField();
        tf.setPromptText(prompt);
        tf.getStyleClass().add("registry-input");
        tf.setMaxWidth(Double.MAX_VALUE);
        return tf;
    }

    private VBox labeled(String label, javafx.scene.Node input) {
        Label l = new Label(label);
        l.getStyleClass().add("registry-field-label");
        VBox v = new VBox(4, l, input);
        HBox.setHgrow(v, Priority.ALWAYS);
        v.setMaxWidth(Double.MAX_VALUE);
        return v;
    }

    private void save() {
        // YOU WRITE THIS
    }

    public static void open(javafx.scene.Node owner) {
        Stage modal = new Stage();
        modal.initModality(Modality.APPLICATION_MODAL);
        modal.initStyle(StageStyle.TRANSPARENT);
        modal.initOwner(owner.getScene().getWindow());

        Runnable close = modal::close;
        NewGuest content = new NewGuest(close);

        StackPane root = new StackPane(content);
        root.setStyle("-fx-background-color: transparent; -fx-padding: 40;");

        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);
        scene.getStylesheets().addAll(owner.getScene().getStylesheets());

        modal.setScene(scene);
        modal.showAndWait();
    }
}