package ui.views.layout;

import ui.pages.*;
import models.*;


import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.layout.Priority;

import models.Settings;
import records.Payment;
import records.RoomHistory;
import sockets.Client;
import records.Address;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import ui.pages.Notifications;

public class Content extends HBox {
    private final Settings settings;

    private final HBox container;
    private final Map<String, Object> views;

    private final DashBoard dashboard;
    private final GuestRegistry guestRegistry;
    private final Bookings bookingsPage;

    private final Reservations reservationsPage;
    private final RoomManagement roomManagementPage;
    private final Housekeeping housekeepingPage;

    private final Services servicesPage;
    private final StaffDirectory staffDirectory;
    private final Payments paymentsPage;
    private final Notifications notificationsPage;

    private Room[] rooms;
    private Reservation[] reservations;

    private Payment[] payments;
    private Guest[] guests;

    private Address[] addresses;
    private Booking[] bookings;
    private ServiceTask[] serviceTasks;

    private final Consumer<Notification[]> setNots;

    public Content(User user, Consumer<Boolean> setIsLoggedIn, Runnable updateScene, Consumer<User> setCurrentUser, Settings settings, Consumer<Notification[]> setNotifications) throws IOException, SQLException {
        this.settings = settings;
        this.setNots = setNotifications;

        setMaxWidth(Double.MAX_VALUE);
        setMaxHeight(Double.MAX_VALUE);

        // pages
        this.views = new HashMap<>();

        this.dashboard = new DashBoard();
        this.guestRegistry = new GuestRegistry();
        this.bookingsPage = new Bookings();

        this.reservationsPage = new Reservations();
        this.roomManagementPage = new RoomManagement();
        this.housekeepingPage = new Housekeeping();

        this.servicesPage = new Services();
        this.staffDirectory = new StaffDirectory();
        this.paymentsPage = new Payments();
        this.notificationsPage = new Notifications();

        ui.pages.Settings settingsPage = new ui.pages.Settings(settings);

        // map of the pages
        this.views.put("Dashboard", dashboard);
        this.views.put("Guest Registry", guestRegistry);
        this.views.put("Bookings", bookingsPage);

        this.views.put("Reservations", reservationsPage);
        this.views.put("Room Management", roomManagementPage);
        this.views.put("Housekeeping", housekeepingPage);

        this.views.put("Services", servicesPage);
        this.views.put("Staff Directory", staffDirectory);
        this.views.put("Payments", paymentsPage);

        this.views.put("System Settings", settingsPage);
        this.views.put("Notifications", notificationsPage);

        this.container = new HBox();

        this.container.setMaxWidth(Double.MAX_VALUE);
        this.container.setMaxHeight(Double.MAX_VALUE);
        HBox.setHgrow(this.container, Priority.ALWAYS);

        ScrollPane scrollPane = new ScrollPane(this.container);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");

        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);

        scrollPane.setMaxWidth(Double.MAX_VALUE);
        scrollPane.setMaxHeight(Double.MAX_VALUE);
        HBox.setHgrow(scrollPane, Priority.ALWAYS);

        this.updatePage("Dashboard");

        Thread clientThread = getClientThread();
        clientThread.start();

        SideBar sideBar = new SideBar(user, setIsLoggedIn, updateScene, setCurrentUser, this::updatePage);

        getChildren().addAll(sideBar, scrollPane);
    }

    private Thread getClientThread() throws IOException {
        Client client = new Client(this::setRooms, this::setNotifications, this::setReservations, this::setPayments, this::setRoomHistory, this::setGuests, this::setAddresses, this::setBookings, this::setServices, this::setServiceTasks, this::setServiceItems, this::setEmployees);
        return new Thread(client::listen);
    }

    public void setRooms(Room[] rooms) {
        this.rooms = rooms;

        if (dashboard != null) {
            dashboard.setRooms(rooms, settings);
        }

        if (roomManagementPage  != null) {
            this.roomManagementPage.setData(bookings, guests, rooms);
        }
    }

    public void setNotifications(Notification[] notifications) {

        if (dashboard != null) {
            dashboard.setNotifications(notifications);
        }

        if (notificationsPage != null) {
            notificationsPage.setData(notifications, settings);
        }

        this.setNots.accept(notifications);
    }

    public void setReservations(Reservation[] reservations) {
        this.reservations = reservations;

        if (dashboard != null) {
            dashboard.setReservations(reservations);
        }

        if (reservationsPage != null) {
            reservationsPage.setData(this.reservations, this.rooms, this.guests, this.payments, this.settings);
        }
    }

    public void setPayments(Payment[] payments) {
        this.payments = payments;

        if (dashboard != null && paymentsPage != null) {
            dashboard.setPayments(this.payments);
            paymentsPage.setData(this.payments, this.guests, this.settings);
        }
    }

    public void setRoomHistory(RoomHistory[] roomHistory) {

        if (dashboard != null) {
            dashboard.setRoomHistory(roomHistory, this.settings);
        }
    }

    public void setGuests(Guest[] guests) {
        this.guests = guests;

        if (guestRegistry != null) {
            guestRegistry.setData(this.guests, this.addresses, this.payments, this.bookings, this.rooms, this.settings);
        }
    }

    public void setAddresses(Address[] addresses) {
        this.addresses = addresses;
    }

    public void setBookings(Booking[] bookings) {
        this.bookings = bookings;

        if (bookingsPage != null) {
            bookingsPage.setData(this.bookings, this.rooms, this.guests, this.reservations, this.settings);
        }
    }

    public void setServices(Service[] services) {

        housekeepingPage.setData(this.rooms, services, this.serviceTasks, this.settings);
    }

    public void setServiceTasks (ServiceTask[] serviceTasks) {
        this.serviceTasks = serviceTasks;

        if (dashboard != null) {
            dashboard.setServiceTasks(this.serviceTasks);
        }
    }

    public void setServiceItems(ServiceItem[] serviceItems) {

        if (servicesPage != null) {
            this.servicesPage.setData(serviceItems);
        }
    }

    public void setEmployees(Employee[] employees) {

        if (staffDirectory != null) {
            this.staffDirectory.setData(employees, this.settings);
        }
    }

    public void updatePage(String page) {
        this.container.getChildren().clear();
        Object newPage = this.views.get(page);

        if (newPage instanceof javafx.scene.Node node) {
            if (node instanceof javafx.scene.layout.Region region) {
                region.setMaxWidth(Double.MAX_VALUE);
                region.setMaxHeight(Double.MAX_VALUE);
            }
            HBox.setHgrow(node, Priority.ALWAYS);
            VBox.setVgrow(node, Priority.ALWAYS);
            this.container.getChildren().add(node);
        }
    }
}