package ui.pages;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcons;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import models.ServiceItem;
import utils.Conn;
import ui.data.ServiceItemData;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Services extends VBox {

    private final TableView<ServiceItemData> service_displayer;
    private final ObservableList<ServiceItemData> allData = FXCollections.observableArrayList();
    private final ObservableList<ServiceItemData> filteredData = FXCollections.observableArrayList();

    private final HBox categoryRow;
    private final Label countLabel;

    private String activeCategory = "All";

    private boolean updateQueued = false;
    private ServiceItem[] pendingItems;

    public Services() throws IOException {
        HBox button_row = new HBox();
        button_row.getStyleClass().add("registry-button-row");
        button_row.setAlignment(Pos.CENTER_LEFT);
        button_row.setSpacing(8);
        button_row.setPadding(new Insets(16, 20, 12, 20));
        button_row.setMaxWidth(Double.MAX_VALUE);

        HBox spacer = new HBox();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        countLabel = new Label("0 active · 0 unavailable");
        countLabel.getStyleClass().add("registry-count");

        button_row.getChildren().addAll(spacer, countLabel);

        HBox filter_row = new HBox();
        filter_row.getStyleClass().add("registry-filter-row");
        filter_row.setSpacing(16);
        filter_row.setAlignment(Pos.CENTER_LEFT);
        filter_row.setPadding(new Insets(20));
        filter_row.setMaxWidth(Double.MAX_VALUE);

        Label categoryLabel = new Label("Category:");
        categoryLabel.getStyleClass().add("registry-field-label");

        categoryRow = new HBox();
        categoryRow.setSpacing(6);
        categoryRow.setAlignment(Pos.CENTER_LEFT);

        filter_row.getChildren().addAll(categoryLabel, categoryRow);

        this.service_displayer = new TableView<>();
        this.service_displayer.getStyleClass().add("registry-table");
        this.service_displayer.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        this.service_displayer.setMaxWidth(Double.MAX_VALUE);
        this.service_displayer.setMaxHeight(Double.MAX_VALUE);
        VBox.setVgrow(this.service_displayer, Priority.ALWAYS);

        TableColumn<ServiceItemData, String> name = new TableColumn<>("Service Name");
        TableColumn<ServiceItemData, String> category = new TableColumn<>("Category");
        TableColumn<ServiceItemData, String> description = new TableColumn<>("Description");
        TableColumn<ServiceItemData, String> price = new TableColumn<>("Price");
        TableColumn<ServiceItemData, String> duration = new TableColumn<>("Duration");
        TableColumn<ServiceItemData, String> status = new TableColumn<>("Status");
        TableColumn<ServiceItemData, Void> toggle = new TableColumn<>("Toggle");

        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        category.setCellValueFactory(new PropertyValueFactory<>("category"));
        description.setCellValueFactory(new PropertyValueFactory<>("description"));
        price.setCellValueFactory(new PropertyValueFactory<>("price"));
        duration.setCellValueFactory(new PropertyValueFactory<>("duration"));
        status.setCellValueFactory(new PropertyValueFactory<>("status"));

        toggle.setCellFactory(_ -> new TableCell<>() {
            private final Button toggleBtn = new Button();

            {
                toggleBtn.setOnAction(_ -> {
                    ServiceItemData row = getTableView().getItems().get(getIndex());

                    if (row == null) return;

                    if (row.isActive()) {
                        toggleBtn.setText("Disable");
                        toggleBtn.setStyle("-fx-background-color: #b87838; -fx-text-fill: #ffffff; -fx-font-size: 11px; -fx-padding: 4 14 4 14;");
                    }

                    else {
                        toggleBtn.setText("Enable");
                        toggleBtn.setStyle("-fx-background-color: #4a9d6a; -fx-text-fill: #ffffff; -fx-font-size: 11px; -fx-padding: 4 14 4 14;");
                    }

                    try {
                        toggle(row.getId(), row.isActive());
                    }

                    catch (IOException | SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || getIndex() < 0 || getIndex() >= getTableView().getItems().size()) {
                    setGraphic(null);
                    return;
                }

                ServiceItemData row = getTableView().getItems().get(getIndex());

                if (row.isActive()) {
                    toggleBtn.setText("Disable");
                    toggleBtn.setStyle("-fx-background-color: #b87838; -fx-text-fill: #ffffff; -fx-font-size: 11px; -fx-padding: 4 14 4 14;");
                }

                else {
                    toggleBtn.setText("Enable");
                    toggleBtn.setStyle("-fx-background-color: #4a9d6a; -fx-text-fill: #ffffff; -fx-font-size: 11px; -fx-padding: 4 14 4 14;");
                }

                setGraphic(toggleBtn);
            }
        });

        this.service_displayer.getColumns().addAll(name, category, description, price, duration, status, toggle);

        getChildren().addAll(button_row, filter_row, this.service_displayer);
        VBox.setVgrow(this, Priority.ALWAYS);
        setMaxWidth(Double.MAX_VALUE);
        setMaxHeight(Double.MAX_VALUE);
    }

    public void setData(ServiceItem[] serviceItems) {
        if (serviceItems == null) return;

        pendingItems = serviceItems;

        if (updateQueued) return;
        updateQueued = true;

        Platform.runLater(() -> {
            updateQueued = false;
            processData(pendingItems);
        });
    }

    private void processData(ServiceItem[] serviceItems) {
        if (serviceItems == null) return;

        ObservableList<ServiceItemData> newData = FXCollections.observableArrayList();

        for (ServiceItem si : serviceItems) {
            newData.add(new ServiceItemData(si));
        }

        ObservableList<String> categories = FXCollections.observableArrayList();
        categories.add("All");

        for (ServiceItem si : serviceItems) {
            String c = si.getCategory();
            if (c != null && !categories.contains(c)) categories.add(c);
        }

        if (!newData.equals(allData)) {
            allData.setAll(newData);
            rebuildCategoryChips(categories);
        }

        applyFilter();
    }

    private void rebuildCategoryChips(ObservableList<String> categories) {
        categoryRow.getChildren().clear();

        if (!categories.contains(activeCategory)) {
            activeCategory = "All";
        }

        for (String c : categories) {
            Button chip = new Button(c);
            chip.getStyleClass().add("registry-button");
            chip.setStyle(activeCategory.equals(c) ? "-fx-background-color: #c9a84c; -fx-text-fill: #0a0908; -fx-border-color: #c9a84c;" : "");

            chip.setOnAction(_ -> {
                activeCategory = c;

                for (Node n : categoryRow.getChildren()) {
                    n.setStyle("");
                }

                chip.setStyle("-fx-background-color: #c9a84c; -fx-text-fill: #0a0908; -fx-border-color: #c9a84c;");
                applyFilter();
            });

            categoryRow.getChildren().add(chip);
        }
    }

    private void applyFilter() {
        ObservableList<ServiceItemData> filtered = FXCollections.observableArrayList();

        for (ServiceItemData d : allData) {
            if (!activeCategory.equals("All") && !activeCategory.equals(d.getCategory())) continue;
            filtered.add(d);
        }

        if (!filtered.equals(service_displayer.getItems())) {
            filteredData.setAll(filtered);

            if (service_displayer.getItems() != filteredData) {
                service_displayer.setItems(filteredData);
            }
        }

        int active = 0;
        int inactive = 0;

        for (ServiceItemData d : allData) {
            if (d.isActive()) active++;
            else inactive++;
        }

        String countStr = active + " active · " + inactive + " unavailable";

        if (!countStr.equals(countLabel.getText())) {
            countLabel.setText(countStr);
        }
    }

    private void toggle(long serviceItemId, boolean isActive) throws IOException, SQLException {
        Connection conn = Conn.getConnection();
        assert(conn != null);

        try(PreparedStatement statement = conn.prepareStatement("update serviceitems set active = ?, updated_at = now() where id = ?")) {
            statement.setBoolean(1, !isActive);
            statement.setLong(2, serviceItemId);

            statement.execute();
        }
    }
}