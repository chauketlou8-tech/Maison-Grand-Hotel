package ui.records;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcons;
import enums.EmploymentStatus;
import enums.Gender;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import models.Employee;
import data.EmployeeData;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

public class EmployeeRecord extends VBox {

    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter TIMESTAMP_FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private final Map<String, HBox> rows = new LinkedHashMap<>();
    private final Map<String, TextField> editors = new LinkedHashMap<>();
    private final Map<String, Label> viewLabels = new LinkedHashMap<>();

    private final Button editBtn;
    private final Button saveBtn;
    private final Button cancelBtn;
    private final Button closeBtn;

    public EmployeeRecord(EmployeeData data, Runnable close) {

        getStyleClass().add("booking-record");
        setSpacing(12);
        setPadding(new Insets(20, 24, 20, 24));
        setMaxWidth(Double.MAX_VALUE);

        Employee e = data.getEmployee();

        HBox header = new HBox();
        header.getStyleClass().add("booking-record-header");
        header.setAlignment(Pos.CENTER_LEFT);

        Label title = new Label("Employee Record — " + data.getFirstName() + " " + data.getLastName() + " (" + data.getEmployeeId() + ")");
        title.getStyleClass().add("booking-record-title");
        HBox.setHgrow(title, Priority.ALWAYS);
        title.setMaxWidth(Double.MAX_VALUE);

        Button headerClose = new Button("✕");
        headerClose.getStyleClass().add("booking-record-close");
        headerClose.setOnAction(_ -> close.run());

        header.getChildren().addAll(title, headerClose);

        HBox body = new HBox();
        body.getStyleClass().add("booking-record-body");
        body.setMaxWidth(Double.MAX_VALUE);
        body.setSpacing(40);

        VBox personal = section("Personal");
        addRow(personal, "First name",    e.getFirstname());
        addRow(personal, "Last name",     e.getLastname());
        addRow(personal, "Email",         e.getEmail());
        addRow(personal, "Phone",         e.getPhonenumber());
        addRow(personal, "Date of birth", e.getDateofbirth() == null ? "" : ((java.sql.Date) e.getDateofbirth()).toLocalDate().format(DATE_FMT));
        addRow(personal, "Gender",        e.getGender() == null ? "" : e.getGender().name());
        addRow(personal, "Address ID",    String.valueOf(e.getAddress_id()));

        VBox work = section("Employment");
        addRow(work, "Employee ID", e.getEmployeeid());
        addRow(work, "Role",        e.getJobposition());
        addRow(work, "Department",  e.getDepartment());
        addRow(work, "Salary",      e.getSalary() == null ? "" : String.valueOf(e.getSalary()));
        addRow(work, "Hire date",   e.getHiredate() == null ? "" : ((java.sql.Date) e.getHiredate()).toLocalDate().format(DATE_FMT));
        addRow(work, "Status",      e.getEmploymentstatus() == null ? "" : e.getEmploymentstatus().name());
        addRow(work, "Created",     e.getCreated_at() == null ? "" : TIMESTAMP_FMT.format(e.getCreated_at().toLocalDateTime()));
        addRow(work, "Updated",     e.getUpdated_at() == null ? "" : TIMESTAMP_FMT.format(e.getUpdated_at().toLocalDateTime()));

        HBox.setHgrow(personal, Priority.ALWAYS);
        HBox.setHgrow(work, Priority.ALWAYS);
        personal.setMaxWidth(Double.MAX_VALUE);
        work.setMaxWidth(Double.MAX_VALUE);

        body.getChildren().addAll(personal, work);

        editBtn = new Button("Edit");
        editBtn.getStyleClass().add("registry-button");
        FontAwesomeIcon pencilIcon = new FontAwesomeIcon();
        pencilIcon.setIcon(FontAwesomeIcons.PENCIL);
        pencilIcon.setSize("11");
        pencilIcon.setFill(Color.web("#9a9080"));
        editBtn.setGraphic(pencilIcon);
        editBtn.setOnAction(_ -> enterEditMode());

        saveBtn = new Button("Save");
        saveBtn.getStyleClass().add("registry-button-primary");
        saveBtn.setVisible(false);
        saveBtn.setManaged(false);
        saveBtn.setOnAction(_ -> save());

        cancelBtn = new Button("Cancel");
        cancelBtn.getStyleClass().add("registry-button");
        cancelBtn.setVisible(false);
        cancelBtn.setManaged(false);
        cancelBtn.setOnAction(_ -> exitEditMode());

        closeBtn = new Button("Close");
        closeBtn.getStyleClass().add("registry-button");
        closeBtn.setOnAction(_ -> close.run());

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox footer = new HBox(8, editBtn, saveBtn, spacer, cancelBtn, closeBtn);
        footer.setAlignment(Pos.CENTER_LEFT);
        footer.setMaxWidth(Double.MAX_VALUE);

        getChildren().addAll(header, new Separator(), body, footer);
    }

    private VBox section(String title) {
        VBox v = new VBox();
        v.getStyleClass().add("booking-record-section");

        Label t = new Label(title.toUpperCase());
        t.getStyleClass().add("booking-record-section-title");

        v.getChildren().addAll(t, new Separator());
        return v;
    }

    private void addRow(VBox parent, String key, String value) {
        HBox row = new HBox();
        row.getStyleClass().add("booking-record-row");
        row.setMaxWidth(Double.MAX_VALUE);

        Label k = new Label(key);
        k.getStyleClass().add("booking-record-key");

        Label v = new Label(value);
        v.getStyleClass().add("booking-record-value");
        HBox.setHgrow(v, Priority.ALWAYS);
        v.setMaxWidth(Double.MAX_VALUE);

        TextField editor = new TextField(value);
        editor.getStyleClass().add("registry-input");
        editor.setMaxWidth(Double.MAX_VALUE);
        editor.setVisible(false);
        editor.setManaged(false);
        HBox.setHgrow(editor, Priority.ALWAYS);

        row.getChildren().addAll(k, v, editor);
        parent.getChildren().add(row);

        rows.put(key, row);
        editors.put(key, editor);
        viewLabels.put(key, v);
    }

    private void enterEditMode() {
        for (Map.Entry<String, HBox> entry : rows.entrySet()) {
            String key = entry.getKey();

            Label label = viewLabels.get(key);
            TextField editor = editors.get(key);

            label.setVisible(false);
            label.setManaged(false);

            editor.setVisible(true);
            editor.setManaged(true);
        }

        editBtn.setVisible(false);
        editBtn.setManaged(false);

        closeBtn.setVisible(false);
        closeBtn.setManaged(false);

        saveBtn.setVisible(true);
        saveBtn.setManaged(true);

        cancelBtn.setVisible(true);
        cancelBtn.setManaged(true);
    }

    private void exitEditMode() {
        for (Map.Entry<String, HBox> entry : rows.entrySet()) {
            String key = entry.getKey();

            Label label = viewLabels.get(key);
            TextField editor = editors.get(key);

            editor.setText(label.getText());
            editor.setVisible(false);
            editor.setManaged(false);

            label.setVisible(true);
            label.setManaged(true);
        }

        saveBtn.setVisible(false);
        saveBtn.setManaged(false);

        cancelBtn.setVisible(false);
        cancelBtn.setManaged(false);

        editBtn.setVisible(true);
        editBtn.setManaged(true);

        closeBtn.setVisible(true);
        closeBtn.setManaged(true);
    }

    private void save() {
        String firstName    = editors.get("First name").getText();
        String lastName     = editors.get("Last name").getText();
        String email        = editors.get("Email").getText();
        String phone        = editors.get("Phone").getText();
        Date dateOfBirth    = Date.from(LocalDate.parse(editors.get("Date of birth").getText(), DATE_FMT).atStartOfDay(ZoneId.systemDefault()).toInstant());
        Gender gender       = Gender.valueOf(editors.get("Gender").getText().toUpperCase());
        int addressId       = Integer.parseInt(editors.get("Address ID").getText());
        String employeeId   = editors.get("Employee ID").getText();
        String role         = editors.get("Role").getText();
        String department   = editors.get("Department").getText();
        BigDecimal salary   = new BigDecimal(editors.get("Salary").getText());
        Date hireDate       = Date.from(LocalDate.parse(editors.get("Hire date").getText(), DATE_FMT).atStartOfDay(ZoneId.systemDefault()).toInstant());
        EmploymentStatus status = EmploymentStatus.valueOf(editors.get("Status").getText().toUpperCase());
        Timestamp created   = Timestamp.valueOf(LocalDateTime.parse(editors.get("Created").getText(), TIMESTAMP_FMT));
        Timestamp updated   = Timestamp.valueOf(LocalDateTime.parse(editors.get("Updated").getText(), TIMESTAMP_FMT));
    }
}