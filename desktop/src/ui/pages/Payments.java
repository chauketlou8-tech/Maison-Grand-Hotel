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
import models.Guest;
import records.Settings;
import records.Payment;
import data.PaymentData;
import ui.records.PaymentRecord;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Payments extends VBox {

    private final TableView<PaymentData> payment_displayer;
    private final ObservableList<PaymentData> allData = FXCollections.observableArrayList();
    private final ObservableList<PaymentData> filteredData = FXCollections.observableArrayList();

    private final Label collectedValue;
    private final Label inFlightValue;
    private final Label failedValue;
    private final Label thisMonthValue;

    private final TextField searchField;
    private final ComboBox<String> methodCombo;
    private final ComboBox<String> statusCombo;
    private final Label countLabel;

    private boolean updateQueued = false;
    private Payment[] pendingPayments;
    private Guest[] pendingGuests;

    public Payments() {
        HBox button_row = new HBox();
        button_row.getStyleClass().add("registry-button-row");
        button_row.setAlignment(Pos.CENTER_LEFT);
        button_row.setSpacing(8);
        button_row.setPadding(new Insets(16, 20, 12, 20));
        button_row.setMaxWidth(Double.MAX_VALUE);

        HBox left_panel = new HBox();
        left_panel.setSpacing(8);
        left_panel.setAlignment(Pos.CENTER_LEFT);

        Button print = new Button("Print");
        print.getStyleClass().add("registry-button");
        FontAwesomeIcon printIcon = new FontAwesomeIcon();
        printIcon.setIcon(FontAwesomeIcons.PRINT);
        printIcon.setSize("11");
        printIcon.setFill(Color.web("#9a9080"));
        print.setGraphic(printIcon);
        print.setOnAction(_ -> printSelected());

        Button export = new Button("Export CSV");
        export.getStyleClass().add("registry-button");
        FontAwesomeIcon exportIcon = new FontAwesomeIcon();
        exportIcon.setIcon(FontAwesomeIcons.ARROW_DOWN);
        exportIcon.setSize("11");
        exportIcon.setFill(Color.web("#9a9080"));
        export.setGraphic(exportIcon);
        export.setOnAction(_ -> exportSelectedCsv());

        Button exportPdf = new Button("Export PDF");
        exportPdf.getStyleClass().add("registry-button");
        FontAwesomeIcon pdfIcon = new FontAwesomeIcon();
        pdfIcon.setIcon(FontAwesomeIcons.FILE_PDF_ALT);
        pdfIcon.setSize("11");
        pdfIcon.setFill(Color.web("#9a9080"));
        exportPdf.setGraphic(pdfIcon);
        exportPdf.setOnAction(_ -> exportSelectedPdf());

        Button clear = new Button("Clear");
        clear.getStyleClass().add("registry-button");
        FontAwesomeIcon refreshIcon = new FontAwesomeIcon();
        refreshIcon.setIcon(FontAwesomeIcons.REFRESH);
        refreshIcon.setSize("11");
        refreshIcon.setFill(Color.web("#9a9080"));
        clear.setGraphic(refreshIcon);

        left_panel.getChildren().addAll(print, export, exportPdf, clear);

        HBox right_panel = new HBox();
        right_panel.setAlignment(Pos.CENTER_RIGHT);
        HBox.setHgrow(right_panel, Priority.ALWAYS);

        countLabel = new Label("0 transactions");
        countLabel.getStyleClass().add("registry-count");
        right_panel.getChildren().add(countLabel);

        button_row.getChildren().addAll(left_panel, right_panel);

        HBox stats = new HBox();
        stats.getStyleClass().add("housekeeping-stats");
        stats.setSpacing(12);
        stats.setPadding(new Insets(20));
        stats.setMaxWidth(Double.MAX_VALUE);

        VBox collectedCard = statCard("COLLECTED");
        collectedValue = (Label) collectedCard.getProperties().get("value");
        collectedValue.setText("R 0.00");

        VBox inFlightCard = statCard("IN FLIGHT");
        inFlightValue = (Label) inFlightCard.getProperties().get("value");
        inFlightValue.setText("R 0.00");

        VBox failedCard = statCard("FAILED");
        failedValue = (Label) failedCard.getProperties().get("value");
        failedValue.setText("R 0.00");

        VBox monthCard = statCard("THIS MONTH");
        thisMonthValue = (Label) monthCard.getProperties().get("value");
        thisMonthValue.setText("R 0.00");

        stats.getChildren().addAll(collectedCard, inFlightCard, failedCard, monthCard);

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
        searchField.setPromptText("Guest, txn id, reference…");
        searchField.getStyleClass().add("registry-input");
        searchField.setPrefWidth(260);

        search_panel.getChildren().addAll(searchLabel, searchIcon, searchField);

        HBox method_panel = new HBox();
        method_panel.setSpacing(8);
        method_panel.setAlignment(Pos.CENTER_LEFT);

        Label methodLabel = new Label("Method:");
        methodLabel.getStyleClass().add("registry-field-label");

        methodCombo = new ComboBox<>();
        methodCombo.getStyleClass().add("registry-combo");
        methodCombo.setPrefWidth(160);
        methodCombo.setValue("All");

        method_panel.getChildren().addAll(methodLabel, methodCombo);

        HBox status_panel = new HBox();
        status_panel.setSpacing(8);
        status_panel.setAlignment(Pos.CENTER_LEFT);

        Label statusLabel = new Label("Status:");
        statusLabel.getStyleClass().add("registry-field-label");

        statusCombo = new ComboBox<>();
        statusCombo.getStyleClass().add("registry-combo");
        statusCombo.setPrefWidth(160);
        statusCombo.setValue("All");

        status_panel.getChildren().addAll(statusLabel, statusCombo);

        filter_row.getChildren().addAll(search_panel, method_panel, status_panel);

        searchField.textProperty().addListener((_, _, _) -> applyFilters());
        methodCombo.valueProperty().addListener((_, _, _) -> applyFilters());
        statusCombo.valueProperty().addListener((_, _, _) -> applyFilters());

        clear.setOnAction(_ -> {
            searchField.clear();
            methodCombo.setValue("All");
            statusCombo.setValue("All");
            applyFilters();
        });

        this.payment_displayer = new TableView<>();
        this.payment_displayer.getStyleClass().add("registry-table");
        this.payment_displayer.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        this.payment_displayer.setMaxWidth(Double.MAX_VALUE);
        this.payment_displayer.setMaxHeight(Double.MAX_VALUE);
        VBox.setVgrow(this.payment_displayer, Priority.ALWAYS);

        TableColumn<PaymentData, String> txnId = new TableColumn<>("TXN");
        TableColumn<PaymentData, String> guestName = new TableColumn<>("Guest");
        TableColumn<PaymentData, String> reference = new TableColumn<>("Reference");
        TableColumn<PaymentData, String> method = new TableColumn<>("Method");
        TableColumn<PaymentData, String> amount = new TableColumn<>("Amount");
        TableColumn<PaymentData, String> date = new TableColumn<>("Date");
        TableColumn<PaymentData, String> status = new TableColumn<>("Status");

        txnId.setCellValueFactory(new PropertyValueFactory<>("txnId"));
        guestName.setCellValueFactory(new PropertyValueFactory<>("guestName"));
        reference.setCellValueFactory(new PropertyValueFactory<>("reference"));
        method.setCellValueFactory(new PropertyValueFactory<>("method"));
        amount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        date.setCellValueFactory(new PropertyValueFactory<>("date"));
        status.setCellValueFactory(new PropertyValueFactory<>("status"));

        this.payment_displayer.getColumns().addAll(txnId, guestName, reference, method, amount, date, status);

        payment_displayer.setOnMouseClicked(event -> {
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
                    PaymentData data = (PaymentData) row.getItem();
                    try {
                        openPaymentModal(data);
                    }
                    catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                    payment_displayer.getSelectionModel().clearSelection();
                }
            }
        });

        getChildren().addAll(button_row, stats, filter_row, this.payment_displayer);
        VBox.setVgrow(this, Priority.ALWAYS);
        setMaxWidth(Double.MAX_VALUE);
        setMaxHeight(Double.MAX_VALUE);
    }

    private VBox statCard(String label) {
        VBox card = new VBox(4);
        card.getStyleClass().add("stat-card");
        card.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(card, Priority.ALWAYS);

        Label labelLbl = new Label(label);
        labelLbl.getStyleClass().add("stat-label");

        Label valueLbl = new Label("R 0.00");
        valueLbl.getStyleClass().add("stat-value");

        card.getChildren().addAll(labelLbl, valueLbl);
        card.getProperties().put("value", valueLbl);

        return card;
    }

    public void setData(Payment[] payments, Guest[] guests, Settings settings) {
        if (payments == null || guests == null) return;

        pendingPayments = payments;
        pendingGuests = guests;

        if (updateQueued) return;
        updateQueued = true;

        Platform.runLater(() -> {
            updateQueued = false;
            processData(pendingPayments, pendingGuests, settings);
        });
    }

    private void processData(Payment[] payments, Guest[] guests, Settings settings) {
        if (payments == null || guests == null) return;

        ObservableList<PaymentData> newData = FXCollections.observableArrayList();

        for (Payment p : payments) {
            newData.add(new PaymentData(p, guests, settings));
        }

        ObservableList<String> methods = FXCollections.observableArrayList();
        methods.add("All");

        ObservableList<String> statuses = FXCollections.observableArrayList();
        statuses.add("All");

        for (PaymentData d : newData) {
            String m = d.getMethod();
            if (m != null && !m.equals("—") && !methods.contains(m)) methods.add(m);

            String s = d.getStatus();
            if (s != null && !s.equals("—") && !statuses.contains(s)) statuses.add(s);
        }

        methods.sort(String::compareTo);
        statuses.sort(String::compareTo);

        if (!newData.equals(allData)) {
            allData.setAll(newData);
        }

        if (!methods.equals(methodCombo.getItems())) {
            String current = methodCombo.getValue();
            methodCombo.setItems(methods);
            methodCombo.setValue(current != null && methods.contains(current) ? current : "All");
        }

        if (!statuses.equals(statusCombo.getItems())) {
            String current = statusCombo.getValue();
            statusCombo.setItems(statuses);
            statusCombo.setValue(current != null && statuses.contains(current) ? current : "All");
        }

        updateStats();
        applyFilters();
    }

    private void updateStats() {
        double collected = 0;
        double inFlight = 0;
        double failed = 0;
        double thisMonth = 0;

        for (PaymentData d : allData) {
            double amt = d.getPayment().amount();

            if (d.isCollected()) collected += amt;
            if (d.isInFlight()) inFlight += amt;
            if (d.isFailed()) failed += amt;
            if (d.isCollected() && d.isThisMonth()) thisMonth += amt;
        }

        String c = "R " + String.format("%,.2f", collected);
        String f = "R " + String.format("%,.2f", inFlight);
        String x = "R " + String.format("%,.2f", failed);
        String m = "R " + String.format("%,.2f", thisMonth);

        if (!c.equals(collectedValue.getText())) collectedValue.setText(c);
        if (!f.equals(inFlightValue.getText())) inFlightValue.setText(f);
        if (!x.equals(failedValue.getText())) failedValue.setText(x);
        if (!m.equals(thisMonthValue.getText())) thisMonthValue.setText(m);
    }

    private void applyFilters() {
        String query = searchField.getText() == null ? "" : searchField.getText().trim().toLowerCase();
        String method = methodCombo.getValue() == null ? "All" : methodCombo.getValue();
        String status = statusCombo.getValue() == null ? "All" : statusCombo.getValue();

        ObservableList<PaymentData> filtered = FXCollections.observableArrayList();

        for (PaymentData d : allData) {
            if (!method.equals("All") && !method.equalsIgnoreCase(d.getMethod())) continue;
            if (!status.equals("All") && !status.equalsIgnoreCase(d.getStatus())) continue;

            if (!query.isEmpty()) {
                String haystack = (d.getTxnId() + " " + d.getGuestName() + " " + d.getReference()
                        + " " + d.getMethod() + " " + d.getStatus()).toLowerCase();
                if (!haystack.contains(query)) continue;
            }

            filtered.add(d);
        }

        if (!filtered.equals(payment_displayer.getItems())) {
            filteredData.setAll(filtered);

            if (payment_displayer.getItems() != filteredData) {
                payment_displayer.setItems(filteredData);
            }
        }

        String countStr = filtered.size() + (filtered.size() == 1 ? " transaction" : " transactions");

        if (!countStr.equals(countLabel.getText())) {
            countLabel.setText(countStr);
        }
    }

    private void openPaymentModal(PaymentData data) throws IOException {
        Stage modal = new Stage();
        modal.initModality(Modality.APPLICATION_MODAL);
        modal.initStyle(StageStyle.TRANSPARENT);
        modal.initOwner(this.getScene().getWindow());

        Runnable close = modal::close;

        PaymentRecord record = new PaymentRecord(data, close, data.getSettings());

        StackPane root = new StackPane(record);
        root.setStyle("-fx-background-color: transparent; -fx-padding: 40;");

        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);
        scene.getStylesheets().addAll(this.getScene().getStylesheets());

        modal.setScene(scene);
        modal.showAndWait();
    }

    private void exportSelectedCsv() {
        PaymentData selected = payment_displayer.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showAlert("Select a payment to export.");
            return;
        }

        Path dir = recordsDir();
        Path file = dir.resolve("payment-" + selected.getTxnId() + ".csv");

        try {
            Files.createDirectories(dir);

            try (Writer w = Files.newBufferedWriter(file)) {
                w.write("txn_id,guest,reference,method,amount,date,status\n");
                w.write(csv(selected.getTxnId()) + "," +
                        csv(selected.getGuestName()) + "," +
                        csv(selected.getReference()) + "," +
                        csv(selected.getMethod()) + "," +
                        csv(selected.getAmount()) + "," +
                        csv(selected.getDate()) + "," +
                        csv(selected.getStatus()) + "\n");
            }

            showAlert("Exported to " + file.toAbsolutePath());
        }

        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void exportSelectedPdf() {
        PaymentData selected = payment_displayer.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showAlert("Select a payment to export.");
            return;
        }

        Path projectRoot = Paths.get(System.getProperty("user.dir")).getParent();
        Path script = projectRoot.resolve("pyutils").resolve("payment_pdf.py");
        Path outDir = recordsDir();

        try {
            pdfMeth(selected, script, outDir);

            showAlert("Exported to " + outDir.toAbsolutePath());
        }

        catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void printSelected() {
        PaymentData selected = payment_displayer.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showAlert("Select a payment to print.");
            return;
        }

        Path projectRoot = Paths.get(System.getProperty("user.dir")).getParent();
        Path script = projectRoot.resolve("pyutils").resolve("payment_pdf.py");
        Path outDir = recordsDir();
        Path pdf = outDir.resolve("payment-" + selected.getTxnId() + ".pdf");

        try {
            if (!Files.exists(pdf)) {
                pdfMeth(selected, script, outDir);
            }

            if (!java.awt.Desktop.isDesktopSupported()) {
                showAlert("Printing is not supported on this system.");
                return;
            }

            java.awt.Desktop.getDesktop().print(pdf.toFile());
        }

        catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void pdfMeth(PaymentData selected, Path script, Path outDir) throws IOException, InterruptedException {
        Process p = runPdfProcess(script, selected, outDir);

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println("[pdf] " + line);
            }
        }

        int exit = p.waitFor();

        if (exit != 0) {
            throw new RuntimeException("PDF generation failed (exit " + exit + ")");
        }
    }

    private Process runPdfProcess(Path script, PaymentData selected, Path outDir) throws IOException {
        ProcessBuilder pb = new ProcessBuilder(
                "python",
                script.toAbsolutePath().toString(),
                selected.getTxnId(),
                selected.getGuestName(),
                selected.getReference(),
                selected.getMethod(),
                selected.getAmount(),
                selected.getDate(),
                selected.getStatus(),
                outDir.toAbsolutePath().toString()
        );
        pb.redirectErrorStream(true);

        return pb.start();
    }

    private Path recordsDir() {
        return Paths.get(System.getProperty("user.dir")).getParent().resolve("resources").resolve("records");
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, message, ButtonType.OK);
        alert.setHeaderText(null);
        alert.initOwner(this.getScene().getWindow());
        alert.showAndWait();
    }

    private String csv(String s) {
        if (s == null) return "";

        String v = s.replace("\"", "\"\"");

        if (v.contains(",") || v.contains("\"") || v.contains("\n")) {
            return "\"" + v + "\"";
        }

        return v;
    }
}