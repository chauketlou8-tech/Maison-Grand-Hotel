package ui.newModals;

import enums.EmploymentStatus;
import enums.Gender;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class NewEmployee extends VBox {

    private final TextField employeeIdField;
    private final TextField firstNameField;
    private final TextField lastNameField;
    private final TextField emailField;
    private final TextField phoneField;
    private final TextField addressIdField;
    private final DatePicker dobPicker;
    private final ComboBox<Gender> genderCombo;
    private final TextField jobPositionField;
    private final TextField departmentField;
    private final TextField salaryField;
    private final DatePicker hireDatePicker;
    private final ComboBox<EmploymentStatus> statusCombo;

    public NewEmployee(Runnable close) {
        getStyleClass().add("booking-record");
        setSpacing(12);
        setPadding(new Insets(20, 24, 20, 24));
        setMaxWidth(Double.MAX_VALUE);

        HBox header = new HBox();
        header.getStyleClass().add("booking-record-header");
        header.setAlignment(Pos.CENTER_LEFT);

        Label title = new Label("Add Employee");
        title.getStyleClass().add("booking-record-title");
        HBox.setHgrow(title, Priority.ALWAYS);
        title.setMaxWidth(Double.MAX_VALUE);

        Button closeBtn = new Button("✕");
        closeBtn.getStyleClass().add("booking-record-close");
        closeBtn.setOnAction(e -> close.run());

        header.getChildren().addAll(title, closeBtn);

        employeeIdField = field("Employee ID");
        firstNameField = field("First name");
        lastNameField = field("Last name");
        emailField = field("Email");
        phoneField = field("Phone");
        addressIdField = field("Address ID");
        jobPositionField = field("Job position");
        departmentField = field("Department");
        salaryField = field("Salary");

        dobPicker = new DatePicker();
        dobPicker.getStyleClass().add("registry-input");
        dobPicker.setMaxWidth(Double.MAX_VALUE);

        hireDatePicker = new DatePicker();
        hireDatePicker.getStyleClass().add("registry-input");
        hireDatePicker.setMaxWidth(Double.MAX_VALUE);

        genderCombo = new ComboBox<>();
        genderCombo.getItems().addAll(Gender.values());
        genderCombo.setValue(Gender.values()[0]);
        genderCombo.getStyleClass().add("registry-combo");
        genderCombo.setMaxWidth(Double.MAX_VALUE);

        statusCombo = new ComboBox<>();
        statusCombo.getItems().addAll(EmploymentStatus.values());
        statusCombo.setValue(EmploymentStatus.ACTIVE);
        statusCombo.getStyleClass().add("registry-combo");
        statusCombo.setMaxWidth(Double.MAX_VALUE);

        GridPane grid = new GridPane();
        grid.setHgap(12);
        grid.setVgap(8);
        grid.setMaxWidth(Double.MAX_VALUE);

        grid.add(labeled("EMPLOYEE ID", employeeIdField), 0, 0);
        grid.add(labeled("ADDRESS ID", addressIdField), 1, 0);
        grid.add(labeled("FIRST NAME", firstNameField), 0, 1);
        grid.add(labeled("LAST NAME", lastNameField), 1, 1);
        grid.add(labeled("EMAIL", emailField), 0, 2);
        grid.add(labeled("PHONE", phoneField), 1, 2);
        grid.add(labeled("DATE OF BIRTH", dobPicker), 0, 3);
        grid.add(labeled("GENDER", genderCombo), 1, 3);
        grid.add(labeled("JOB POSITION", jobPositionField), 0, 4);
        grid.add(labeled("DEPARTMENT", departmentField), 1, 4);
        grid.add(labeled("SALARY", salaryField), 0, 5);
        grid.add(labeled("HIRE DATE", hireDatePicker), 1, 5);
        grid.add(labeled("EMPLOYMENT STATUS", statusCombo), 0, 6, 2, 1);

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
        NewEmployee content = new NewEmployee(close);

        StackPane root = new StackPane(content);
        root.setStyle("-fx-background-color: transparent; -fx-padding: 40;");

        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);
        scene.getStylesheets().addAll(owner.getScene().getStylesheets());

        modal.setScene(scene);
        modal.showAndWait();
    }
}