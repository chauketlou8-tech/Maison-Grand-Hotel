package views.dashboard;

import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.layout.Priority;
import models.*;
import records.Payment;
import records.RoomHistory;
import sockets.Client;
import records.Address;
import utils.NavItem;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class Content extends HBox {
    private NavItem activeNavItem;

    private Room[] rooms;
    private Notification[] notifications;
    private Reservation[] reservations;
    private Payment[] payments;
    private RoomHistory[] roomHistory;
    private Guest[] guests;
    private Address[] addresses;
    private Booking[] bookings;

    private DashBoard dashboard;
    private GuestRegistry guestRegistry;

    private HBox c;
    private Map<String, Object> views;

    public Content(User user, Consumer<Boolean> setIsLoggedIn, Runnable updateScene, Consumer<User> setCurrentUser) throws IOException {
        this.views = new HashMap<>();
        this.dashboard = new DashBoard();
        this.guestRegistry = new GuestRegistry();

        this.views.put("Dashboard", dashboard);
        this.views.put("Guest Registry", guestRegistry);

        this.c = new HBox();
        this.c.setMaxWidth(Double.MAX_VALUE);
        this.c.setMaxHeight(Double.MAX_VALUE);
        HBox.setHgrow(this.c, Priority.ALWAYS);

        ScrollPane scrollPane = new ScrollPane(this.c);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setMaxWidth(Double.MAX_VALUE);
        scrollPane.setMaxHeight(Double.MAX_VALUE);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");
        HBox.setHgrow(scrollPane, Priority.ALWAYS);

        this.updatePage("Dashboard");

        Client client = new Client(this::setRooms, this::setNotifications, this::setReservations, this::setPayments, this::setRoomHistory, this::setGuests, this::setAddresses, this::setBookings);

        Thread clientThread = new Thread(client::listen);
        clientThread.start();

        SideBar sideBar = new SideBar(user, this::setActiveNavItem, setIsLoggedIn, updateScene, setCurrentUser, this::updatePage);

        getChildren().addAll(sideBar, scrollPane);
    }

    public void setActiveNavItem(NavItem item) {
        activeNavItem = item;
        System.out.println(activeNavItem.getText());
    }

    public void setRooms(Room[] rooms) {
        this.rooms = rooms;

        if (dashboard != null) {
            dashboard.setRooms(rooms);
        }
    }

    public void setNotifications(Notification[] notifications) {
        this.notifications = notifications;

        if (dashboard != null) {
            dashboard.setNotifications(notifications);
        }
    }

    public void setReservations(Reservation[] reservations) {
        this.reservations = reservations;

        if (dashboard != null) {
            dashboard.setReservations(reservations);
        }
    }

    public void setPayments(Payment[] payments) {
        this.payments = payments;

        if (dashboard != null) {
            dashboard.setPayments(payments);
        }
    }

    public void setRoomHistory(RoomHistory[] roomHistory) {
        this.roomHistory = roomHistory;

        if (dashboard != null) {
            dashboard.setRoomHistory(roomHistory);
        }
    }

    public void setGuests(Guest[] guests) {
        this.guests = guests;

        if (guestRegistry != null) {
            guestRegistry.setData(this.guests, this.addresses, this.payments, this.bookings);
        }
    }

    public void setAddresses(Address[] addresses) {
        this.addresses = addresses;
    }

    public void setBookings(Booking[] bookings) {
        this.bookings = bookings;
    }

    public void updatePage(String page) {
        this.c.getChildren().clear();
        Object newPage = this.views.get(page);

        if (newPage instanceof javafx.scene.Node node) {
            if (node instanceof javafx.scene.layout.Region region) {
                region.setMaxWidth(Double.MAX_VALUE);
                region.setMaxHeight(Double.MAX_VALUE);
            }
            HBox.setHgrow(node, Priority.ALWAYS);
            VBox.setVgrow(node, Priority.ALWAYS);
            this.c.getChildren().add(node);
        }
    }
}