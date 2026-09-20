package ui.newModals;

import enums.BookingStatus;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.Scene;
import javafx.scene.paint.Color;

public class NewBooking extends VBox {

    private final TextField guestIdField;
    private final TextField roomIdField;
    private final TextField reservationIdField;
    private final DatePicker checkInPicker;
    private final DatePicker checkOutPicker;
    private final TextField guestsField;
    private final ComboBox<BookingStatus> statusCombo;
    private final TextField totalField;
    private final TextField packagesField;
    private final TextArea requestsField;

    public NewBooking(Runnable close) {
        getStyleClass().add("booking-record");
        setSpacing(12);
        setPadding(new Insets(20, 24, 20, 24));
        setMaxWidth(Double.MAX_VALUE);

        HBox header = new HBox();
        header.getStyleClass().add("booking-record-header");
        header.setAlignment(Pos.CENTER_LEFT);

        Label title = new Label("New Booking");
        title.getStyleClass().add("booking-record-title");
        HBox.setHgrow(title, Priority.ALWAYS);
        title.setMaxWidth(Double.MAX_VALUE);

        Button closeBtn = new Button("✕");
        closeBtn.getStyleClass().add("booking-record-close");
        closeBtn.setOnAction(e -> close.run());

        header.getChildren().addAll(title, closeBtn);

        guestIdField = field("Guest ID");
        roomIdField = field("Room ID");
        reservationIdField = field("Reservation ID");

        checkInPicker = new DatePicker();
        checkInPicker.getStyleClass().add("registry-input");
        checkInPicker.setMaxWidth(Double.MAX_VALUE);

        checkOutPicker = new DatePicker();
        checkOutPicker.getStyleClass().add("registry-input");
        checkOutPicker.setMaxWidth(Double.MAX_VALUE);

        guestsField = field("Number of guests");

        statusCombo = new ComboBox<>();
        statusCombo.getItems().addAll(BookingStatus.values());
        statusCombo.setValue(BookingStatus.PENDING);
        statusCombo.getStyleClass().add("registry-combo");
        statusCombo.setMaxWidth(Double.MAX_VALUE);

        totalField = field("Total cost");
        packagesField = field("Packages (comma separated)");

        requestsField = new TextArea();
        requestsField.setPromptText("Requests");
        requestsField.getStyleClass().add("registry-input");
        requestsField.setPrefRowCount(3);
        requestsField.setMaxWidth(Double.MAX_VALUE);

        GridPane grid = new GridPane();
        grid.setHgap(12);
        grid.setVgap(8);
        grid.setMaxWidth(Double.MAX_VALUE);

        grid.add(labeled("GUEST ID", guestIdField), 0, 0);
        grid.add(labeled("ROOM ID", roomIdField), 1, 0);
        grid.add(labeled("RESERVATION ID", reservationIdField), 0, 1);
        grid.add(labeled("NUMBER OF GUESTS", guestsField), 1, 1);
        grid.add(labeled("CHECK-IN", checkInPicker), 0, 2);
        grid.add(labeled("CHECK-OUT", checkOutPicker), 1, 2);
        grid.add(labeled("STATUS", statusCombo), 0, 3);
        grid.add(labeled("TOTAL COST", totalField), 1, 3);
        grid.add(labeled("PACKAGES", packagesField), 0, 4, 2, 1);
        grid.add(labeled("REQUESTS", requestsField), 0, 5, 2, 1);

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
        NewBooking content = new NewBooking(close);

        StackPane root = new StackPane(content);
        root.setStyle("-fx-background-color: transparent; -fx-padding: 40;");

        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);
        scene.getStylesheets().addAll(owner.getScene().getStylesheets());

        modal.setScene(scene);
        modal.showAndWait();
    }
}