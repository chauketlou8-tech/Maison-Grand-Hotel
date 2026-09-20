package ui.newModals;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class NewService extends VBox {

    private final TextField serviceIdField;
    private final TextField nameField;
    private final TextField categoryField;
    private final TextArea descriptionField;
    private final TextField priceField;
    private final TextField durationField;
    private final CheckBox activeCheck;

    public NewService(Runnable close) {
        getStyleClass().add("booking-record");
        setSpacing(12);
        setPadding(new Insets(20, 24, 20, 24));
        setMaxWidth(Double.MAX_VALUE);

        HBox header = new HBox();
        header.getStyleClass().add("booking-record-header");
        header.setAlignment(Pos.CENTER_LEFT);

        Label title = new Label("Add Service");
        title.getStyleClass().add("booking-record-title");
        HBox.setHgrow(title, Priority.ALWAYS);
        title.setMaxWidth(Double.MAX_VALUE);

        Button closeBtn = new Button("✕");
        closeBtn.getStyleClass().add("booking-record-close");
        closeBtn.setOnAction(e -> close.run());

        header.getChildren().addAll(title, closeBtn);

        serviceIdField = field("Service ID");
        nameField = field("Name");
        categoryField = field("Category");
        priceField = field("Price");
        durationField = field("Duration");

        descriptionField = new TextArea();
        descriptionField.setPromptText("Description");
        descriptionField.getStyleClass().add("registry-input");
        descriptionField.setPrefRowCount(3);
        descriptionField.setMaxWidth(Double.MAX_VALUE);

        activeCheck = new CheckBox("Active");
        activeCheck.setSelected(true);

        GridPane grid = new GridPane();
        grid.setHgap(12);
        grid.setVgap(8);
        grid.setMaxWidth(Double.MAX_VALUE);

        grid.add(labeled("SERVICE ID", serviceIdField), 0, 0);
        grid.add(labeled("NAME", nameField), 1, 0);
        grid.add(labeled("CATEGORY", categoryField), 0, 1);
        grid.add(labeled("DURATION", durationField), 1, 1);
        grid.add(labeled("PRICE", priceField), 0, 2);
        grid.add(labeled("ACTIVE", activeCheck), 1, 2);
        grid.add(labeled("DESCRIPTION", descriptionField), 0, 3, 2, 1);

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

        getChildren().addAll(header, new Separator(), grid, footer);
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
        NewService content = new NewService(close);

        StackPane root = new StackPane(content);
        root.setStyle("-fx-background-color: transparent; -fx-padding: 40;");

        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);
        scene.getStylesheets().addAll(owner.getScene().getStylesheets());

        modal.setScene(scene);
        modal.showAndWait();
    }
}