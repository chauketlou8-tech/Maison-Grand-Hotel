package ui.pages;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcons;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import models.Employee;
import ui.data.EmployeeData;
import records.Settings;
import ui.records.EmployeeRecord;

import java.io.IOException;
import java.sql.SQLException;

public class StaffDirectory extends VBox {

    private final TableView<EmployeeData> employee_displayer;
    private final ObservableList<EmployeeData> allData = FXCollections.observableArrayList();
    private final ObservableList<EmployeeData> filteredData = FXCollections.observableArrayList();

    private final TextField searchField;
    private final ComboBox<String> roleCombo;
    private final Label countLabel;

    private boolean updateQueued = false;
    private Employee[] pendingEmployees;

    public StaffDirectory() {
        HBox button_row = new HBox();
        button_row.getStyleClass().add("registry-button-row");
        button_row.setAlignment(Pos.CENTER_LEFT);
        button_row.setSpacing(8);
        button_row.setPadding(new Insets(16, 20, 12, 20));
        button_row.setMaxWidth(Double.MAX_VALUE);

        HBox left_panel = new HBox();
        left_panel.setSpacing(8);
        left_panel.setAlignment(Pos.CENTER_LEFT);

        Button clear = new Button("Clear");
        clear.getStyleClass().add("registry-button");

        FontAwesomeIcon refreshIcon = new FontAwesomeIcon();
        refreshIcon.setIcon(FontAwesomeIcons.REFRESH);
        refreshIcon.setSize("11");
        refreshIcon.setFill(Color.web("#9a9080"));
        clear.setGraphic(refreshIcon);

        left_panel.getChildren().addAll(clear);

        HBox right_panel = new HBox();
        right_panel.setAlignment(Pos.CENTER_RIGHT);
        HBox.setHgrow(right_panel, Priority.ALWAYS);

        countLabel = new Label("0 staff");
        countLabel.getStyleClass().add("registry-count");
        right_panel.getChildren().add(countLabel);

        button_row.getChildren().addAll(left_panel, right_panel);

        HBox filter_row = new HBox();
        filter_row.getStyleClass().add("registry-filter-row");
        filter_row.setSpacing(16);
        filter_row.setAlignment(Pos.CENTER_LEFT);
        filter_row.setPadding(new Insets(20));
        filter_row.setMaxWidth(Double.MAX_VALUE);

        HBox search_panel = new HBox();
        search_panel.setSpacing(8);
        search_panel.setAlignment(Pos.CENTER_LEFT);

        Label searchLabel = new Label("Search:");
        searchLabel.getStyleClass().add("registry-field-label");

        FontAwesomeIcon searchIcon = new FontAwesomeIcon();
        searchIcon.setIcon(FontAwesomeIcons.SEARCH);
        searchIcon.setSize("11");
        searchIcon.setFill(Color.web("#6a6058"));

        searchField = new TextField();
        searchField.setPromptText("Name, department, email…");
        searchField.getStyleClass().add("registry-input");
        searchField.setPrefWidth(280);

        search_panel.getChildren().addAll(searchLabel, searchIcon, searchField);

        HBox role_panel = new HBox();
        role_panel.setSpacing(8);
        role_panel.setAlignment(Pos.CENTER_LEFT);

        Label roleLabel = new Label("Role:");
        roleLabel.getStyleClass().add("registry-field-label");

        roleCombo = new ComboBox<>();
        roleCombo.getStyleClass().add("registry-combo");
        roleCombo.setPrefWidth(160);
        roleCombo.setValue("All Roles");

        role_panel.getChildren().addAll(roleLabel, roleCombo);

        filter_row.getChildren().addAll(search_panel, role_panel);

        searchField.textProperty().addListener((_, _, _) -> applyFilters());
        roleCombo.valueProperty().addListener((_, _, _) -> applyFilters());

        clear.setOnAction(_ -> {
            searchField.clear();
            roleCombo.setValue("All Roles");
            applyFilters();
        });

        this.employee_displayer = new TableView<>();
        this.employee_displayer.getStyleClass().add("registry-table");
        this.employee_displayer.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        this.employee_displayer.setMaxWidth(Double.MAX_VALUE);
        this.employee_displayer.setMaxHeight(Double.MAX_VALUE);
        VBox.setVgrow(this.employee_displayer, Priority.ALWAYS);

        TableColumn<EmployeeData, String> employeeId = new TableColumn<>("Employee ID");
        TableColumn<EmployeeData, String> lastName = new TableColumn<>("Last Name");
        TableColumn<EmployeeData, String> firstName = new TableColumn<>("First Name");
        TableColumn<EmployeeData, String> role = new TableColumn<>("Role");
        TableColumn<EmployeeData, String> department = new TableColumn<>("Department");
        TableColumn<EmployeeData, String> email = new TableColumn<>("Email");
        TableColumn<EmployeeData, String> phone = new TableColumn<>("Phone");
        TableColumn<EmployeeData, String> gender = new TableColumn<>("Gender");
        TableColumn<EmployeeData, String> salary = new TableColumn<>("Salary");
        TableColumn<EmployeeData, String> startDate = new TableColumn<>("Start Date");
        TableColumn<EmployeeData, String> status = new TableColumn<>("Status");

        employeeId.setCellValueFactory(new PropertyValueFactory<>("employeeId"));
        lastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        firstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        role.setCellValueFactory(new PropertyValueFactory<>("role"));
        department.setCellValueFactory(new PropertyValueFactory<>("department"));
        email.setCellValueFactory(new PropertyValueFactory<>("email"));
        phone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        gender.setCellValueFactory(new PropertyValueFactory<>("gender"));
        salary.setCellValueFactory(new PropertyValueFactory<>("salary"));
        startDate.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        status.setCellValueFactory(new PropertyValueFactory<>("status"));

        this.employee_displayer.getColumns().addAll(employeeId, lastName, firstName, role, department, email, phone, gender, salary, startDate, status);

        employee_displayer.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                Node target = (Node) event.getTarget();
                TableRow<?> row = null;

                while (target != null) {
                    if (target instanceof TableRow) {
                        row = (TableRow<?>) target;
                        break;
                    }
                    target = target.getParent();
                }

                if (row != null && row.getItem() != null) {
                    EmployeeData data = (EmployeeData) row.getItem();
                    try {
                        openEmployeeModal(data);
                    }
                    catch (IOException | SQLException e) {
                        throw new RuntimeException(e);
                    }

                    employee_displayer.getSelectionModel().clearSelection();
                }
            }
        });

        getChildren().addAll(button_row, filter_row, this.employee_displayer);
        VBox.setVgrow(this, Priority.ALWAYS);
        setMaxWidth(Double.MAX_VALUE);
        setMaxHeight(Double.MAX_VALUE);
    }

    public void setData(Employee[] employees, Settings settings) {
        if (employees == null) return;

        pendingEmployees = employees;

        if (updateQueued) return;
        updateQueued = true;

        Platform.runLater(() -> {
            updateQueued = false;
            processData(pendingEmployees, settings);
        });
    }

    private void processData(Employee[] employees, Settings settings) {
        if (employees == null) return;

        ObservableList<EmployeeData> newData = FXCollections.observableArrayList();

        for (Employee e : employees) {
            newData.add(new EmployeeData(e, settings));
        }

        ObservableList<String> roles = FXCollections.observableArrayList();
        roles.add("All Roles");

        for (Employee e : employees) {
            String r = e.getJobposition();
            if (r != null && !roles.contains(r)) roles.add(r);
        }

        roles.sort(String::compareTo);

        if (!newData.equals(allData)) {
            allData.setAll(newData);
        }

        if (!roles.equals(roleCombo.getItems())) {
            String current = roleCombo.getValue();
            roleCombo.setItems(roles);

            if (current != null && roles.contains(current)) {
                roleCombo.setValue(current);
            }

            else {
                roleCombo.setValue("All Roles");
            }
        }

        applyFilters();
    }

    private void applyFilters() {
        String query = searchField.getText() == null ? "" : searchField.getText().trim().toLowerCase();
        String role = roleCombo.getValue() == null ? "All Roles" : roleCombo.getValue();

        ObservableList<EmployeeData> filtered = FXCollections.observableArrayList();

        for (EmployeeData d : allData) {
            if (!role.equals("All Roles") && !role.equals(d.getRole())) continue;

            if (!query.isEmpty()) {
                String haystack = (d.getEmployeeId() + " " + d.getFirstName() + " " + d.getLastName() + " "
                        + d.getDepartment() + " " + d.getEmail() + " " + d.getPhone()).toLowerCase();
                if (!haystack.contains(query)) continue;
            }

            filtered.add(d);
        }

        if (!filtered.equals(employee_displayer.getItems())) {
            filteredData.setAll(filtered);

            if (employee_displayer.getItems() != filteredData) {
                employee_displayer.setItems(filteredData);
            }
        }

        int active = 0;
        for (EmployeeData d : filtered) {
            if ("ACTIVE".equalsIgnoreCase(d.getStatus())) active++;
        }

        String countStr = filtered.size() + " staff · " + active + " active";

        if (!countStr.equals(countLabel.getText())) {
            countLabel.setText(countStr);
        }
    }

    private void openEmployeeModal(EmployeeData data) throws IOException, SQLException {
        Stage modal = new Stage();
        modal.initModality(Modality.APPLICATION_MODAL);
        modal.initStyle(StageStyle.TRANSPARENT);
        modal.initOwner(this.getScene().getWindow());

        Runnable close = modal::close;

        EmployeeRecord record = new EmployeeRecord(data, close);

        StackPane root = new StackPane(record);
        root.setStyle("-fx-background-color: transparent; -fx-padding: 40;");

        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);
        scene.getStylesheets().addAll(this.getScene().getStylesheets());

        modal.setScene(scene);
        modal.showAndWait();
    }
}