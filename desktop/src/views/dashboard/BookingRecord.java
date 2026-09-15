package views.dashboard;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcons;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import utils.BookingData;

public class BookingRecord extends VBox {

    public BookingRecord(BookingData data, Runnable onClose, Runnable onCheckOut, Runnable onPrintFolio) {
        getStyleClass().add("booking-record");

        HBox header = new HBox();
        header.getStyleClass().add("booking-record-header");
        header.setAlignment(Pos.CENTER_LEFT);

        Label title = new Label("Booking — " + data.getBookingId());
        title.getStyleClass().add("booking-record-title");
        HBox.setHgrow(title, Priority.ALWAYS);
        title.setMaxWidth(Double.MAX_VALUE);

        Button closeX = new Button("✕");
        closeX.getStyleClass().add("booking-record-close");
        closeX.setOnAction(e -> onClose.run());

        header.getChildren().addAll(title, closeX);

        HBox stats = new HBox();
        stats.getStyleClass().add("booking-record-stats");
        stats.getChildren().addAll(
                stat("GUEST", data.getGuestName()),
                stat("ROOM", data.getRoomNumber() + " · " + data.getRoomType()),
                stat("NIGHTS", String.valueOf(data.getNights())),
                stat("STATUS", data.getStatus())
        );

        HBox body = new HBox();
        body.getStyleClass().add("booking-record-body");

        VBox stay = new VBox();
        stay.getStyleClass().add("booking-record-section");
        HBox.setHgrow(stay, Priority.ALWAYS);
        Label stayTitle = new Label("STAY DETAILS");
        stayTitle.getStyleClass().add("booking-record-section-title");
        stay.getChildren().addAll(
                stayTitle,
                row("Booking ID", data.getBookingId()),
                row("Reference", data.getReference()),
                row("Check-In", data.getCheckIn()),
                row("Check-Out", data.getCheckOut()),
                row("Nights", String.valueOf(data.getNights())),
                row("Guests", String.valueOf(data.getGuests()))
        );

        VBox pricing = new VBox();
        pricing.getStyleClass().add("booking-record-section");
        HBox.setHgrow(pricing, Priority.ALWAYS);
        Label pricingTitle = new Label("PRICING");
        pricingTitle.getStyleClass().add("booking-record-section-title");
        pricing.getChildren().addAll(
                pricingTitle,
                row("Room Rate", data.getRoomRate()),
                row("Nights", String.valueOf(data.getNights())),
                row("Total Cost", data.getTotal())
        );

        Label pkgTitle = new Label("PACKAGES");
        pkgTitle.getStyleClass().add("booking-record-section-title");
        Label pkg = new Label(data.getPackages());
        pkg.getStyleClass().add("booking-record-value");
        pkg.setWrapText(true);

        VBox pkgBox = new VBox(pkgTitle, pkg);
        pkgBox.getStyleClass().add("booking-record-block");
        pricing.getChildren().add(pkgBox);

        body.getChildren().addAll(stay, pricing);

        Label reqTitle = new Label("SPECIAL REQUESTS");
        reqTitle.getStyleClass().add("booking-record-section-title");
        Label req = new Label(data.getRequests());
        req.getStyleClass().add("booking-record-value");
        req.setWrapText(true);

        VBox reqBox = new VBox(reqTitle, req);
        reqBox.getStyleClass().add("booking-record-block");
        reqBox.setMaxWidth(Double.MAX_VALUE);

        HBox footer = new HBox();
        footer.getStyleClass().add("booking-record-footer");
        footer.setAlignment(Pos.CENTER_LEFT);

        Button checkOut = new Button("Check Out");
        checkOut.getStyleClass().add("registry-button");

        checkOut.setOnAction(e -> onCheckOut.run());

        Button printFolio = new Button("Print Folio");
        printFolio.getStyleClass().add("registry-button");

        FontAwesomeIcon printerIcon = new FontAwesomeIcon();
        printerIcon.setIcon(FontAwesomeIcons.PRINT);
        printerIcon.setSize("11");
        printerIcon.setFill(Color.web("#9a9080"));
        printFolio.setGraphic(printerIcon);

        printFolio.setOnAction(e -> onPrintFolio.run());

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button close = new Button("Close");
        close.getStyleClass().add("registry-button-primary");
        close.setOnAction(e -> onClose.run());

        footer.getChildren().addAll(checkOut, printFolio, spacer, close);

        getChildren().addAll(
                header,
                new Separator(),
                stats,
                new Separator(),
                body,
                new Separator(),
                reqBox,
                new Separator(),
                footer
        );
    }

    private VBox stat(String label, String value) {
        Label k = new Label(label);
        k.getStyleClass().add("booking-record-stat-key");
        Label v = new Label(value);
        v.getStyleClass().add("booking-record-stat-value");
        VBox box = new VBox(2, k, v);
        box.getStyleClass().add("booking-record-stat");
        HBox.setHgrow(box, Priority.ALWAYS);
        box.setMaxWidth(Double.MAX_VALUE);
        return box;
    }

    private HBox row(String key, String value) {
        HBox r = new HBox();
        r.getStyleClass().add("booking-record-row");

        Label k = new Label(key);
        k.getStyleClass().add("booking-record-key");

        Label v = new Label(value);
        v.getStyleClass().add("booking-record-value");
        HBox.setHgrow(v, Priority.ALWAYS);
        v.setMaxWidth(Double.MAX_VALUE);

        r.getChildren().addAll(k, v);
        return r;
    }
}