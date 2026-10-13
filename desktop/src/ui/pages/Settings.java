package ui.pages;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcons;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.util.LinkedHashMap;
import java.util.Map;

public class Settings extends VBox {

    private final VBox contentPane;
    private final Map<String, Node> panels = new LinkedHashMap<>();
    private final Map<String, Button> navButtons = new LinkedHashMap<>();
    private final records.Settings settings;

    private final Map<String, TextField> textFields = new LinkedHashMap<>();
    private final Map<String, ComboBox<String>> comboFields = new LinkedHashMap<>();
    private final Map<String, CheckBox> checkFields = new LinkedHashMap<>();

    public Settings(records.Settings settings) {
        this.settings = settings;

        getStyleClass().add("guest-registry");
        setSpacing(0);
        setMaxWidth(Double.MAX_VALUE);
        setMaxHeight(Double.MAX_VALUE);

        HBox body = new HBox();
        body.setMaxWidth(Double.MAX_VALUE);
        body.setMaxHeight(Double.MAX_VALUE);
        VBox.setVgrow(body, Priority.ALWAYS);

        VBox nav = new VBox(0);
        nav.getStyleClass().add("settings-nav");
        nav.setMinWidth(220);
        nav.setPrefWidth(220);
        nav.setMaxWidth(220);

        Label navTitle = new Label("SETTINGS");
        navTitle.getStyleClass().add("registry-field-label");
        navTitle.setPadding(new Insets(20, 20, 12, 20));
        nav.getChildren().add(navTitle);

        addNav(nav, "Property");
        addNav(nav, "Booking Rules");
        addNav(nav, "Room Types");
        addNav(nav, "Rate Plans");
        addNav(nav, "Tax & Fees");
        addNav(nav, "Payments");
        addNav(nav, "Notifications");
        addNav(nav, "Housekeeping");
        addNav(nav, "Night Audit");
        addNav(nav, "Localisation");

        ScrollPane scroll = new ScrollPane();
        scroll.getStyleClass().add("settings-scroll");
        scroll.setFitToWidth(true);
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        HBox.setHgrow(scroll, Priority.ALWAYS);
        scroll.setMaxWidth(Double.MAX_VALUE);
        scroll.setMaxHeight(Double.MAX_VALUE);

        contentPane = new VBox(0);
        contentPane.setMaxWidth(Double.MAX_VALUE);

        scroll.setContent(contentPane);

        panels.put("Property", buildPropertyPanel());
        panels.put("Booking Rules", buildBookingRulesPanel());
        panels.put("Room Types", buildRoomTypesPanel());
        panels.put("Rate Plans", buildRatePlansPanel());
        panels.put("Tax & Fees", buildTaxFeesPanel());
        panels.put("Payments", buildPaymentsPanel());
        panels.put("Notifications", buildNotificationsPanel());
        panels.put("Housekeeping", buildHousekeepingPanel());
        panels.put("Night Audit", buildNightAuditPanel());
        panels.put("Localisation", buildLocalisationPanel());

        body.getChildren().addAll(nav, scroll);
        getChildren().add(body);
        VBox.setVgrow(this, Priority.ALWAYS);

        selectSection("Property");
    }

    private void addNav(VBox nav, String name) {
        Button btn = new Button(name);
        btn.getStyleClass().add("settings-nav-item");
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setAlignment(Pos.CENTER_LEFT);
        btn.setPadding(new Insets(10, 20, 10, 20));
        btn.setOnAction(_ -> selectSection(name));

        navButtons.put(name, btn);
        nav.getChildren().add(btn);
    }

    private void selectSection(String name) {
        for (Map.Entry<String, Button> e : navButtons.entrySet()) {
            e.getValue().getStyleClass().remove("active");
            if (e.getKey().equals(name)) e.getValue().getStyleClass().add("active");
        }

        contentPane.getChildren().clear();
        Node panel = panels.get(name);
        if (panel != null) contentPane.getChildren().add(panel);
    }

    private VBox panel(String title, String subtitle) {
        VBox p = new VBox(12);
        p.setPadding(new Insets(24, 28, 24, 28));
        p.setMaxWidth(Double.MAX_VALUE);

        Label t = new Label(title);
        t.getStyleClass().add("booking-record-title");

        p.getChildren().add(t);

        if (subtitle != null) {
            Label s = new Label(subtitle);
            s.getStyleClass().add("guest-record-empty");
            p.getChildren().add(s);
        }

        return p;
    }

    private VBox section(String title) {
        VBox v = new VBox(8);
        v.getStyleClass().add("booking-record-section");
        v.setSpacing(16);
        v.setAlignment(Pos.CENTER_LEFT);
        v.setPadding(new Insets(20));
        v.setMaxWidth(Double.MAX_VALUE);

        Label l = new Label(title.toUpperCase());
        l.getStyleClass().add("booking-record-section-title");

        v.getChildren().addAll(l, new Separator());
        return v;
    }

    private HBox editableField(String key, String value) {
        HBox row = new HBox();
        row.getStyleClass().add("booking-record-row");
        row.setSpacing(16);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(20));
        row.setMaxWidth(Double.MAX_VALUE);

        Label k = new Label(key);
        k.getStyleClass().add("booking-record-key");

        TextField tf = new TextField(value == null ? "" : value);
        tf.getStyleClass().add("registry-input");
        tf.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(tf, Priority.ALWAYS);

        row.getChildren().addAll(k, tf);

        textFields.put(key, tf);

        return row;
    }

    private HBox readOnlyField(String key, String value) {
        HBox row = new HBox();
        row.getStyleClass().add("booking-record-row");
        row.setSpacing(16);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(20));
        row.setMaxWidth(Double.MAX_VALUE);

        Label k = new Label(key);
        k.getStyleClass().add("booking-record-key");

        Label v = new Label(value == null ? "—" : value);
        v.getStyleClass().add("booking-record-value");
        HBox.setHgrow(v, Priority.ALWAYS);
        v.setMaxWidth(Double.MAX_VALUE);

        row.getChildren().addAll(k, v);
        return row;
    }

    private HBox dropdownField(String key, String value, String... options) {
        HBox row = new HBox();
        row.getStyleClass().add("booking-record-row");
        row.setSpacing(16);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(20));
        row.setMaxWidth(Double.MAX_VALUE);

        Label k = new Label(key);
        k.getStyleClass().add("booking-record-key");

        ComboBox<String> cb = new ComboBox<>();
        cb.getItems().addAll(options);
        cb.setValue(value);
        cb.getStyleClass().add("registry-combo");
        cb.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(cb, Priority.ALWAYS);

        row.getChildren().addAll(k, cb);

        comboFields.put(key, cb);

        return row;
    }

    private HBox toggle(String key, boolean on) {
        HBox row = new HBox();
        row.getStyleClass().add("booking-record-row");
        row.setSpacing(16);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(20));
        row.setMaxWidth(Double.MAX_VALUE);

        Label k = new Label(key);
        k.getStyleClass().add("booking-record-key");
        HBox.setHgrow(k, Priority.ALWAYS);
        k.setMaxWidth(Double.MAX_VALUE);

        CheckBox cb = new CheckBox();
        cb.setSelected(on);

        row.getChildren().addAll(k, cb);

        checkFields.put(key, cb);

        return row;
    }

    private HBox footer() {
        Button save = new Button("Save");
        save.getStyleClass().add("registry-button-primary");

        FontAwesomeIcon saveIcon = new FontAwesomeIcon();
        saveIcon.setIcon(FontAwesomeIcons.CHECK);
        saveIcon.setSize("11");
        saveIcon.setFill(Color.web("#0a0908"));
        save.setGraphic(saveIcon);

        save.setOnAction(_ -> save());

        HBox f = new HBox(save);
        f.setAlignment(Pos.CENTER_LEFT);
        f.setPadding(new Insets(16, 0, 0, 0));
        return f;
    }

    private VBox buildPropertyPanel() {
        VBox p = panel("Property", "Hotel identity and contact details used on guest communications.");

        VBox identity = section("Identity");
        identity.getChildren().addAll(editableField("Hotel name", settings.hotelName()));

        VBox contact = section("Contact");
        contact.getChildren().addAll(
                editableField("Phone", settings.hotelPhone()),
                editableField("Email", settings.hotelEmail()),
                editableField("Street", settings.hotelStreet()),
                editableField("City", settings.hotelCity()),
                editableField("Province", settings.hotelProvince()),
                editableField("Postal code", settings.hotelPostalCode()),
                editableField("Country", settings.hotelCountry())
        );

        p.getChildren().addAll(identity, contact, footer());
        return p;
    }

    private VBox buildBookingRulesPanel() {
        VBox p = panel("Booking Rules", "Standard policies applied to all reservations and bookings.");

        VBox times = section("Times");
        times.getChildren().addAll(
                editableField("Check-in time",        String.valueOf(settings.checkInTime())),
                editableField("Check-out time",       String.valueOf(settings.checkOutTime())),
                editableField("Early check-in from",  settings.earlyCheckInTime() == null ? "" : String.valueOf(settings.earlyCheckInTime())),
                editableField("Late check-out until", settings.lateCheckOutTime() == null ? "" : String.valueOf(settings.lateCheckOutTime()))
        );

        VBox rules = section("Stay Rules");
        rules.getChildren().addAll(
                editableField("Minimum nights",               String.valueOf(settings.minNights())),
                editableField("Maximum nights",               String.valueOf(settings.maxNights())),
                editableField("Cancellation window (hours)",  String.valueOf(settings.cancellationWindowHours())),
                editableField("Advance booking limit (days)", String.valueOf(settings.advanceBookingLimitDays()))
        );

        VBox policy = section("Policies");
        policy.getChildren().addAll(
                toggle("Pets allowed",            settings.petsAllowed()),
                toggle("Children allowed",        settings.childrenAllowed()),
                toggle("ID required at check-in", settings.idRequired())
        );

        VBox fees = section("Guest Fees");
        fees.getChildren().addAll(
                editableField("Extra adult fee (per night)", settings.extraAdultFee() == null ? "" : settings.extraAdultFee().toPlainString()),
                editableField("Child fee (per night)",       settings.childFee() == null ? "" : settings.childFee().toPlainString())
        );

        VBox late = section("Late Checkout");
        late.getChildren().addAll(
                editableField("Grace period (minutes)",        String.valueOf(settings.graceMinutes())),
                editableField("Full night after (hours)",      String.valueOf(settings.fullNightHours())),
                editableField("Escalate after (hours)",        String.valueOf(settings.escalateHours()))
        );

        p.getChildren().addAll(times, rules, policy, fees, late, footer());
        return p;
    }

    private VBox buildRoomTypesPanel() {
        VBox p = panel("Room Types", "Room categories, capacity, and base pricing.");

        VBox list = section("Types");

        Object[][] types = {
                {"SI", "Single Room",    "18 m²", "2–4",  "2800"},
                {"ST", "Standard Room",  "26 m²", "2–6",  "3600"},
                {"DO", "Double Room",    "32 m²", "2–7",  "4400"},
                {"QU", "Queen Room",     "38 m²", "3–8",  "5200"},
                {"KI", "King Room",      "44 m²", "4–9",  "6400"},
                {"DE", "Deluxe Room",    "52 m²", "5–10", "8200"},
                {"EX", "Executive Room", "58 m²", "7–12", "10500"},
                {"FA", "Family Room",    "72 m²", "2–6",  "9800"},
                {"SU", "Suite",          "95 m²", "8–14", "16000"},
                {"PE", "Penthouse",      "210 m²","15–20","28000"}
        };

        for (Object[] t : types) {
            HBox row = new HBox();
            row.getStyleClass().add("booking-record-row");
            row.setSpacing(16);
            row.setAlignment(Pos.CENTER_LEFT);
            row.setPadding(new Insets(20));
            row.setMaxWidth(Double.MAX_VALUE);

            Label code = new Label((String) t[0]);
            code.getStyleClass().add("booking-record-key");
            code.setMinWidth(40);

            Label name = new Label((String) t[1]);
            name.getStyleClass().add("booking-record-value");
            name.setMinWidth(150);

            Label size = new Label((String) t[2]);
            size.getStyleClass().add("booking-record-value");
            size.setMinWidth(70);

            Label floors = new Label("Floors " + t[3]);
            floors.getStyleClass().add("booking-record-value");
            floors.setMinWidth(110);

            TextField price = new TextField((String) t[4]);
            price.getStyleClass().add("registry-input");
            HBox.setHgrow(price, Priority.ALWAYS);
            price.setMaxWidth(Double.MAX_VALUE);

            row.getChildren().addAll(code, name, size, floors, price);
            list.getChildren().add(row);
        }

        p.getChildren().addAll(list, footer());
        return p;
    }

    private VBox buildRatePlansPanel() {
        VBox p = panel("Rate Plans", "Packages and add-ons available to guests at booking.");

        VBox plans = section("Packages");

        Object[][] plansData = {
                {"Romance Package",   "R 3,200", "Champagne & strawberries on arrival · Couples' spa treatment (90 min) · Late check-out (14:00)"},
                {"Breakfast Included","R 620",   "Daily breakfast for two at The Atrium · Full hot & continental selection · Freshly squeezed juice & pastries"},
                {"Airport Transfer",  "R 950",   "Private chauffeur from Cape Town International (CPT) · Electric vehicle available on request · Meet & greet service"},
                {"Secure Parking",    "R 220",   "24-hour secure parking · Valet service · EV charging station"},
                {"Spa Day Pass",      "R 1,200", "Full access to spa facilities · Sauna & steam room · Pool access · Gym access"}
        };

        for (Object[] t : plansData) {
            HBox row = new HBox();
            row.getStyleClass().add("booking-record-row");
            row.setSpacing(16);
            row.setAlignment(Pos.CENTER_LEFT);
            row.setPadding(new Insets(20));
            row.setMaxWidth(Double.MAX_VALUE);

            Label name = new Label((String) t[0]);
            name.getStyleClass().add("booking-record-value");
            name.setMinWidth(160);

            TextField price = new TextField((String) t[1]);
            price.getStyleClass().add("registry-input");
            price.setPrefWidth(120);

            Label includes = new Label((String) t[2]);
            includes.getStyleClass().add("guest-record-empty");
            includes.setWrapText(true);
            HBox.setHgrow(includes, Priority.ALWAYS);
            includes.setMaxWidth(Double.MAX_VALUE);

            row.getChildren().addAll(name, price, includes);
            plans.getChildren().add(row);
        }

        p.getChildren().addAll(plans, footer());
        return p;
    }

    private VBox buildTaxFeesPanel() {
        VBox p = panel("Tax & Fees", "Taxes and charges applied to bookings.");

        VBox taxes = section("Taxes");
        taxes.getChildren().addAll(
                editableField("VAT rate (%)", settings.vatRate() == null ? "" : settings.vatRate().toPlainString()),
                editableField("Service charge (%)", settings.serviceChargeRate() == null ? "" : settings.serviceChargeRate().toPlainString()),
                editableField("Tourism levy (per room / night)", settings.tourismLevy() == null ? "" : settings.tourismLevy().toPlainString())
        );

        VBox display = section("Display");
        display.getChildren().addAll(
                dropdownField("Prices shown to guests", settings.pricesIncludeVat() ? "Including VAT" : "Excluding VAT", "Including VAT", "Excluding VAT"),
                toggle("Show tax breakdown at checkout", settings.showTaxBreakdown())
        );

        p.getChildren().addAll(taxes, display, footer());
        return p;
    }

    private VBox buildPaymentsPanel() {
        VBox p = panel("Payments", "Payment provider configuration and processing rules.");

        VBox provider = section("Provider");
        provider.getChildren().addAll(readOnlyField("Provider", "Yoco"), readOnlyField("Currency", "ZAR"));

        VBox rules = section("Rules");
        rules.getChildren().addAll(
                editableField("Payment window (minutes)", String.valueOf(settings.paymentWindowMinutes())),
                dropdownField("Auto-cancel unpaid reservations", settings.autoCancelUnpaid() ? "Enabled" : "Disabled", "Enabled", "Disabled")
        );

        p.getChildren().addAll(provider, rules, footer());
        return p;
    }

    private VBox buildNotificationsPanel() {
        VBox p = panel("Notifications", "Which events trigger emails and in-app alerts.");

        VBox email = section("Guest Emails");
        email.getChildren().addAll(
                toggle("Reservation received", settings.notifyReservationReceived()),
                toggle("Payment confirmation", settings.notifyPaymentConfirmation()),
                toggle("Reservation cancelled (non-payment)", settings.notifyReservationCancelled()),
                toggle("Check-in reminder (24h before)", settings.notifyCheckinReminder()),
                toggle("Check-out reminder (2h before)", settings.notifyCheckoutReminder()),
                toggle("Post-stay feedback request", settings.notifyFeedbackRequest())
        );

        VBox app = section("Staff Notifications");
        app.getChildren().addAll(
                toggle("New reservation", settings.notifyStaffNewReservation()),
                toggle("New payment received", settings.notifyStaffNewPayment()),
                toggle("Service task completed", settings.notifyStaffTaskCompleted()),
                toggle("Low occupancy alert", settings.notifyStaffLowOccupancy())
        );

        VBox occupancy = section("Occupancy");
        occupancy.getChildren().addAll(
                editableField("Low occupancy threshold", settings.occupancyThreshold() == null ? "" : settings.occupancyThreshold().toPlainString())
        );

        p.getChildren().addAll(email, app, occupancy, footer());
        return p;
    }

    private VBox buildHousekeepingPanel() {
        VBox p = panel("Housekeeping", "Task generation and assignment rules.");

        VBox cleaning = section("Cleaning");
        cleaning.getChildren().addAll(
                editableField("Auto-create task time", String.valueOf(settings.housekeepingTaskTime())),
                dropdownField("Default task status", settings.housekeepingDefaultStatus(), "pending", "completed"),
                dropdownField("Assignment", settings.housekeepingAssignment(), "Round-robin", "Manual", "By floor")
        );

        VBox inspection = section("Inspection");
        inspection.getChildren().addAll(
                toggle("Auto-create inspection after checkout", settings.autoInspectionAfterCheckout()),
                editableField("Damage report threshold (R)", settings.damageReportThreshold() == null ? "" : settings.damageReportThreshold().toPlainString())
        );

        p.getChildren().addAll(cleaning, inspection, footer());
        return p;
    }

    private VBox buildNightAuditPanel() {
        VBox p = panel("Night Audit", "End-of-day rollover and automated reporting.");

        VBox schedule = section("Schedule");
        schedule.getChildren().addAll(
                editableField("Night audit time", String.valueOf(settings.nightAuditTime())),
                dropdownField("Frequency", settings.nightAuditFrequency(), "Daily", "Weekly", "Monthly")
        );

        VBox reporting = section("Reporting");
        reporting.getChildren().addAll(
                toggle("Auto-generate daily report", settings.autoGenerateDailyReport())
        );

        p.getChildren().addAll(schedule, reporting, footer());
        return p;
    }

    private VBox buildLocalisationPanel() {
        VBox p = panel("Localisation", "Currency, timezone, and language defaults.");

        VBox locale = section("Regional");
        locale.getChildren().addAll(
                readOnlyField("Currency", "ZAR (R)"),
                readOnlyField("Timezone", "Africa/Johannesburg"),
                readOnlyField("Language", "English (en-ZA)"),
                dropdownField("Date format", settings.dateFormat(), "DD/MM/YYYY", "MM/DD/YYYY", "YYYY-MM-DD"),
                dropdownField("Time format", settings.timeFormat(), "24-hour", "12-hour")
        );

        p.getChildren().addAll(locale, footer());
        return p;
    }

    private void save() {
        String hotelName         = textFields.get("Hotel name").getText();
        String hotelPhone        = textFields.get("Phone").getText();
        String hotelEmail        = textFields.get("Email").getText();
        String hotelStreet       = textFields.get("Street").getText();
        String hotelCity         = textFields.get("City").getText();
        String hotelProvince     = textFields.get("Province").getText();
        String hotelPostalCode   = textFields.get("Postal code").getText();
        String hotelCountry      = textFields.get("Country").getText();

        String checkInTime       = textFields.get("Check-in time").getText();
        String checkOutTime      = textFields.get("Check-out time").getText();
        String earlyCheckInTime  = textFields.get("Early check-in from").getText();
        String lateCheckOutTime  = textFields.get("Late check-out until").getText();
        String minNights         = textFields.get("Minimum nights").getText();
        String maxNights         = textFields.get("Maximum nights").getText();
        String cancellationHours = textFields.get("Cancellation window (hours)").getText();
        String advanceLimitDays  = textFields.get("Advance booking limit (days)").getText();

        boolean petsAllowed      = checkFields.get("Pets allowed").isSelected();
        boolean childrenAllowed  = checkFields.get("Children allowed").isSelected();
        boolean idRequired       = checkFields.get("ID required at check-in").isSelected();

        String extraAdultFee     = textFields.get("Extra adult fee (per night)").getText();
        String childFee          = textFields.get("Child fee (per night)").getText();

        String graceMinutes      = textFields.get("Grace period (minutes)").getText();
        String fullNightHours    = textFields.get("Full night after (hours)").getText();
        String escalateHours     = textFields.get("Escalate after (hours)").getText();

        String vatRate           = textFields.get("VAT rate (%)").getText();
        String serviceChargeRate = textFields.get("Service charge (%)").getText();
        String tourismLevy       = textFields.get("Tourism levy (per room / night)").getText();
        String pricesIncludeVat  = comboFields.get("Prices shown to guests").getValue();
        boolean showTaxBreakdown = checkFields.get("Show tax breakdown at checkout").isSelected();

        String paymentWindow     = textFields.get("Payment window (minutes)").getText();
        String autoCancelUnpaid  = comboFields.get("Auto-cancel unpaid reservations").getValue();

        boolean notifyReservationReceived  = checkFields.get("Reservation received").isSelected();
        boolean notifyPaymentConfirmation  = checkFields.get("Payment confirmation").isSelected();
        boolean notifyReservationCancelled = checkFields.get("Reservation cancelled (non-payment)").isSelected();
        boolean notifyCheckinReminder      = checkFields.get("Check-in reminder (24h before)").isSelected();
        boolean notifyCheckoutReminder     = checkFields.get("Check-out reminder (2h before)").isSelected();
        boolean notifyFeedbackRequest      = checkFields.get("Post-stay feedback request").isSelected();

        boolean notifyStaffNewReservation = checkFields.get("New reservation").isSelected();
        boolean notifyStaffNewPayment     = checkFields.get("New payment received").isSelected();
        boolean notifyStaffTaskCompleted  = checkFields.get("Service task completed").isSelected();
        boolean notifyStaffLowOccupancy   = checkFields.get("Low occupancy alert").isSelected();

        String occupancyThreshold = textFields.get("Low occupancy threshold").getText();

        String housekeepingTaskTime      = textFields.get("Auto-create task time").getText();
        String housekeepingDefaultStatus = comboFields.get("Default task status").getValue();
        String housekeepingAssignment    = comboFields.get("Assignment").getValue();
        boolean autoInspection           = checkFields.get("Auto-create inspection after checkout").isSelected();
        String damageReportThreshold     = textFields.get("Damage report threshold (R)").getText();

        String nightAuditTime      = textFields.get("Night audit time").getText();
        String nightAuditFrequency = comboFields.get("Frequency").getValue();
        boolean autoGenerateReport = checkFields.get("Auto-generate daily report").isSelected();

        String dateFormat = comboFields.get("Date format").getValue();
        String timeFormat = comboFields.get("Time format").getValue();

        try (java.sql.Connection conn = utils.Conn.getConnection();
             java.sql.PreparedStatement ps = conn.prepareStatement(
                     "update settings set " +
                             "hotel_name = ?, " +
                             "hotel_email = ?, " +
                             "hotel_phone = ?, " +
                             "hotel_street = ?, " +
                             "hotel_city = ?, " +
                             "hotel_province = ?, " +
                             "hotel_postal_code = ?, " +
                             "hotel_country = ?, " +
                             "check_in_time = ?::time, " +
                             "check_out_time = ?::time, " +
                             "early_check_in_time = ?::time, " +
                             "late_check_out_time = ?::time, " +
                             "min_nights = ?, " +
                             "max_nights = ?, " +
                             "cancellation_window_hours = ?, " +
                             "advance_booking_limit_days = ?, " +
                             "pets_allowed = ?, " +
                             "children_allowed = ?, " +
                             "id_required = ?, " +
                             "extra_adult_fee = ?, " +
                             "child_fee = ?, " +
                             "grace_minutes = ?, " +
                             "full_night_hours = ?, " +
                             "escalate_hours = ?, " +
                             "vat_rate = ?, " +
                             "service_charge_rate = ?, " +
                             "tourism_levy = ?, " +
                             "prices_include_vat = ?, " +
                             "show_tax_breakdown = ?, " +
                             "payment_window_minutes = ?, " +
                             "auto_cancel_unpaid = ?, " +
                             "notify_reservation_received = ?, " +
                             "notify_payment_confirmation = ?, " +
                             "notify_reservation_cancelled = ?, " +
                             "notify_checkin_reminder = ?, " +
                             "notify_checkout_reminder = ?, " +
                             "notify_feedback_request = ?, " +
                             "notify_staff_new_reservation = ?, " +
                             "notify_staff_new_payment = ?, " +
                             "notify_staff_task_completed = ?, " +
                             "notify_staff_low_occupancy = ?, " +
                             "occupancy_threshold = ?, " +
                             "housekeeping_task_time = ?::time, " +
                             "housekeeping_default_status = ?, " +
                             "housekeeping_assignment = ?, " +
                             "auto_inspection_after_checkout = ?, " +
                             "damage_report_threshold = ?, " +
                             "night_audit_time = ?::time, " +
                             "night_audit_frequency = ?, " +
                             "auto_generate_daily_report = ?, " +
                             "date_format = ?, " +
                             "time_format = ?, " +
                             "updated_at = now() " +
                             "where id = 1")) {

            int i = 1;

            ps.setString(i++, hotelName);
            ps.setString(i++, hotelEmail);
            ps.setString(i++, hotelPhone);
            ps.setString(i++, hotelStreet);
            ps.setString(i++, hotelCity);
            ps.setString(i++, hotelProvince);
            ps.setString(i++, hotelPostalCode);
            ps.setString(i++, hotelCountry);

            ps.setString(i++, blankToNull(checkInTime));
            ps.setString(i++, blankToNull(checkOutTime));
            ps.setString(i++, blankToNull(earlyCheckInTime));
            ps.setString(i++, blankToNull(lateCheckOutTime));

            ps.setInt(i++, Integer.parseInt(minNights));
            ps.setInt(i++, Integer.parseInt(maxNights));
            ps.setInt(i++, Integer.parseInt(cancellationHours));
            ps.setInt(i++, Integer.parseInt(advanceLimitDays));

            ps.setBoolean(i++, petsAllowed);
            ps.setBoolean(i++, childrenAllowed);
            ps.setBoolean(i++, idRequired);

            ps.setBigDecimal(i++, new java.math.BigDecimal(extraAdultFee));
            ps.setBigDecimal(i++, new java.math.BigDecimal(childFee));

            ps.setInt(i++, Integer.parseInt(graceMinutes));
            ps.setInt(i++, Integer.parseInt(fullNightHours));
            ps.setInt(i++, Integer.parseInt(escalateHours));

            ps.setBigDecimal(i++, new java.math.BigDecimal(vatRate));
            ps.setBigDecimal(i++, new java.math.BigDecimal(serviceChargeRate));
            ps.setBigDecimal(i++, new java.math.BigDecimal(tourismLevy));

            ps.setBoolean(i++, "Including VAT".equalsIgnoreCase(pricesIncludeVat));
            ps.setBoolean(i++, showTaxBreakdown);

            ps.setInt(i++, Integer.parseInt(paymentWindow));
            ps.setBoolean(i++, "Enabled".equalsIgnoreCase(autoCancelUnpaid));

            ps.setBoolean(i++, notifyReservationReceived);
            ps.setBoolean(i++, notifyPaymentConfirmation);
            ps.setBoolean(i++, notifyReservationCancelled);
            ps.setBoolean(i++, notifyCheckinReminder);
            ps.setBoolean(i++, notifyCheckoutReminder);
            ps.setBoolean(i++, notifyFeedbackRequest);

            ps.setBoolean(i++, notifyStaffNewReservation);
            ps.setBoolean(i++, notifyStaffNewPayment);
            ps.setBoolean(i++, notifyStaffTaskCompleted);
            ps.setBoolean(i++, notifyStaffLowOccupancy);

            ps.setBigDecimal(i++, new java.math.BigDecimal(occupancyThreshold));

            ps.setString(i++, blankToNull(housekeepingTaskTime));
            ps.setString(i++, housekeepingDefaultStatus);
            ps.setString(i++, housekeepingAssignment);
            ps.setBoolean(i++, autoInspection);
            ps.setBigDecimal(i++, new java.math.BigDecimal(damageReportThreshold));

            ps.setString(i++, blankToNull(nightAuditTime));
            ps.setString(i++, nightAuditFrequency);
            ps.setBoolean(i++, autoGenerateReport);

            ps.setString(i++, dateFormat);
            ps.setString(i, timeFormat);

            ps.executeUpdate();

            System.out.println("Settings updated");
        }

        catch (java.sql.SQLException | java.io.IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String blankToNull(String s) {
        return (s == null || s.isBlank()) ? null : s;
    }
}