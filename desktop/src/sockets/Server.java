package sockets;

import enums.*;
import models.*;
import records.Payment;
import records.RoomHistory;
import records.Address;
import utils.Conn;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Server {

    private final Connection conn = Conn.getConnection();

    public Server() throws IOException, ClassNotFoundException, SQLException {
        ServerSocket serverSocket = new ServerSocket(3000);

        System.out.println("Socket started on port 3000...");
        System.out.println("Waiting for connection...");

        Socket server = serverSocket.accept();

        System.out.println("Client connected...");

        try {
            ObjectOutputStream out = new ObjectOutputStream(server.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(server.getInputStream());

            out.flush();

            Thread reader = new Thread(() -> {
                try {
                    while (!server.isClosed()) {
                        Object message = in.readObject();

                        if (message instanceof String str) {
                            if (str.equals("Connecting to the server...")) {
                                System.out.println("Client connected...");
                            }
                        }
                    }
                } catch (IOException | ClassNotFoundException e) {
                    System.out.println("Reader ended: " + e.getMessage());
                }
            });

            reader.start();

            while (!server.isClosed()) {

                Room[] rooms = this.getRooms().toArray(new Room[0]);
                Notification[] notifications = this.getNotifications().toArray(new Notification[0]);
                Reservation[] reservations = this.getReservations().toArray(new Reservation[0]);
                Payment[] payments = this.getPayments().toArray(new Payment[0]);
                RoomHistory[] roomHistory = this.getRoomHistory().toArray(new RoomHistory[0]);
                Guest[] guests = this.getGuests().toArray(new Guest[0]);
                Address[] addresses = this.getAddresses().toArray(new Address[0]);
                Booking[] bookings = this.getBookings().toArray(new Booking[0]);
                Service[] services = this.getServices().toArray(new Service[0]);
                ServiceTask[] serviceTasks = this.getServiceTasks().toArray(new ServiceTask[0]);
                ServiceItem[] serviceItems = this.getServiceItems().toArray(new ServiceItem[0]);
                Employee[] employees = this.getEmployees().toArray(new Employee[0]);

                out.writeObject(rooms);
                out.writeObject(notifications);
                out.writeObject(reservations);
                out.writeObject(payments);
                out.writeObject(roomHistory);
                out.writeObject(guests);
                out.writeObject(addresses);
                out.writeObject(bookings);
                out.writeObject(services);
                out.writeObject(serviceTasks);
                out.writeObject(serviceItems);
                out.writeObject(employees);

                out.flush();
                out.reset();
            }
        }

        finally {
            server.close();
            serverSocket.close();
        }
    }

    private List<Room> getRooms() throws SQLException {
        List<Room> rooms = new ArrayList<>();

        try (Statement statement = conn.createStatement()) {
            ResultSet rs = statement.executeQuery("select * from rooms");

            while (rs.next()) {
                Room room = new Room(
                        rs.getLong("roomid"),
                        rs.getInt("roomnumber"),
                        rs.getInt("floornumber"),
                        rs.getString("roomtype"),
                        rs.getFloat("price"),
                        rs.getInt("capacity"),
                        RoomStatus.valueOf(rs.getString("roomstatus").toUpperCase()),
                        rs.getString("description")
                );

                rooms.add(room);
            }
        }

        return rooms;
    }

    private List<Notification> getNotifications() throws SQLException {
        List<Notification> notifications = new ArrayList<>();

        try (Statement statement = conn.createStatement()) {
            ResultSet rs = statement.executeQuery("select * from notifications");

            while (rs.next()) {
                Number paymentNum = (Number) rs.getObject("payment_id");
                Long payment_id = paymentNum == null ? null : paymentNum.longValue();

                Number reservationNum = (Number) rs.getObject("reservationid");
                Long reservation_id = reservationNum == null ? null : reservationNum.longValue();

                Number guestNum = (Number) rs.getObject("guestid");
                Long guest_id = guestNum == null ? null : guestNum.longValue();

                Notification notification = new Notification(
                        rs.getLong("notificationid"),
                        NotificationType.valueOf(rs.getString("type")),
                        rs.getString("title"),
                        rs.getString("message"),
                        payment_id,
                        reservation_id,
                        guest_id,
                        NotificationStatus.valueOf(rs.getString("status")),
                        rs.getTimestamp("created_at"),
                        rs.getTimestamp("updated_at")
                );

                notifications.add(notification);
            }
        }

        return notifications;
    }

    private List<Reservation> getReservations() throws SQLException {
        List<Reservation> reservations = new ArrayList<>();

        try (Statement statement = conn.createStatement()) {
            ResultSet rs = statement.executeQuery("select * from reservations");

            while (rs.next()) {
                Reservation reservation = new Reservation(
                        rs.getLong("id"),
                        (java.util.UUID) rs.getObject("reservationid"),
                        rs.getLong("guest_id"),
                        rs.getLong("room_id"),
                        rs.getString("requestedroomtype"),
                        rs.getTimestamp("reservationdate"),
                        rs.getDate("checkindate"),
                        rs.getDate("checkoutdate"),
                        rs.getLong("numberofguests"),
                        ReservationStatus.valueOf(rs.getString("reservationstatus").toUpperCase()),
                        (String[]) rs.getArray("selectedpackages").getArray(),
                        rs.getString("requests"),
                        rs.getFloat("total"),
                        rs.getTimestamp("created_at"),
                        rs.getTimestamp("updated_at")
                );

                reservations.add(reservation);
            }
        }

        return reservations;
    }

    private List<Payment> getPayments() throws SQLException {
        List<Payment> payments = new ArrayList<>();

        try (Statement statement = conn.createStatement()) {
            ResultSet rs = statement.executeQuery("select * from payments");

            while (rs.next()) {
                Payment payment = new Payment(
                        rs.getLong("id"),
                        rs.getDouble("amount"),
                        rs.getDate("paymentdate"),
                        rs.getString("paymentstatus"),
                        rs.getString("transactionreference"),
                        rs.getLong("guest_id"),
                        rs.getObject("reservation_id", java.util.UUID.class),
                        rs.getString("yoco_checkout_id"),
                        rs.getString("paymentmethod"),
                        rs.getTimestamp("created_at"),
                        rs.getTimestamp("updated_at")
                );

                payments.add(payment);
            }
        }

        return payments;
    }

    private List<RoomHistory> getRoomHistory() throws SQLException {
        List<RoomHistory> roomHistory = new ArrayList<>();

        try (Statement statement = conn.createStatement()) {
            ResultSet rs = statement.executeQuery("select * from roomhistory");

            while (rs.next()) {
                RoomHistory roomHistory1 = new RoomHistory(
                        rs.getLong("id"),
                        rs.getLong("roomid"),
                        rs.getTimestamp("status_date"),
                        rs.getString("status"),
                        rs.getTimestamp("created_at"),
                        rs.getTimestamp("updated_at")
                );

                roomHistory.add(roomHistory1);
            }
        }

        return roomHistory;
    }

    private List<Guest> getGuests() throws SQLException {
        List<Guest> guests = new ArrayList<>();

        try (Statement statement = conn.createStatement()) {
            ResultSet rs = statement.executeQuery("select * from guests");

            while (rs.next()) {
                Guest guest = new Guest(
                        rs.getLong("id"),
                        rs.getString("guestid"),
                        rs.getString("firstname"),
                        rs.getString("lastname"),
                        rs.getString("email"),
                        rs.getString("phonenumber"),
                        rs.getLong("address_id"),
                        rs.getString("idnumber"),
                        rs.getDate("dateofbirth"),
                        rs.getString("nationality"),
                        rs.getDate("registrationdate"),
                        rs.getTimestamp("created_at"),
                        rs.getTimestamp("updated_at")
                );

                guests.add(guest);
            }
        }

        return guests;
    }

    private List<Address> getAddresses() throws SQLException {
        List<Address> addresses = new ArrayList<>();

        try (Statement statement = conn.createStatement()) {
            ResultSet rs = statement.executeQuery("select * from address");

            while (rs.next()) {
                Address address = new Address(
                        rs.getLong("id"),
                        rs.getString("street"),
                        rs.getString("city"),
                        rs.getString("province"),
                        rs.getString("country"),
                        rs.getString("postalcode"),
                        rs.getTimestamp("created_at"),
                        rs.getTimestamp("updated_at")
                );

                addresses.add(address);
            }
        }

        return addresses;
    }

    private List<Booking> getBookings() throws SQLException {
        List<Booking> bookings = new ArrayList<>();

        try (Statement statement = conn.createStatement()) {
            ResultSet rs = statement.executeQuery("select * from bookings");

            while (rs.next()) {
                Booking booking = new Booking(
                        rs.getString("booking_id"),
                        rs.getObject("booking_reference", java.util.UUID.class),
                        rs.getObject("reservation_id", java.util.UUID.class),
                        rs.getLong("guest_id"),
                        rs.getLong("room_id"),
                        rs.getDate("booking_date"),
                        rs.getDate("check_in_date"),
                        rs.getDate("check_out_date"),
                        rs.getLong("number_of_guests"),
                        BookingStatus.valueOf(rs.getString("booking_status")),
                        rs.getDouble("total_cost"),
                        (String[]) rs.getArray("selected_packages").getArray(),
                        rs.getString("requests"),
                        rs.getTimestamp("created_at"),
                        rs.getTimestamp("updated_at")
                );

                bookings.add(booking);
            }
        }

        return bookings;
    }

    private List<Service> getServices() throws SQLException {
        List<Service> services = new ArrayList<>();

        try (Statement statement = conn.createStatement()) {
            ResultSet rs = statement.executeQuery("select * from services");

            while (rs.next()) {
                Service service = new Service(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getBoolean("recurring"),
                        rs.getTimestamp("created_at"),
                        rs.getTimestamp("updated_at")
                );

                services.add(service);
            }
        }

        return services;
    }

    private List<ServiceTask> getServiceTasks() throws SQLException {
        List<ServiceTask> serviceTasks = new ArrayList<>();

        try (Statement statement = conn.createStatement()) {
            ResultSet rs = statement.executeQuery("select * from servicetasks");

            while (rs.next()) {
                ServiceTask serviceTask = new ServiceTask(
                        rs.getInt("id"),
                        rs.getInt("service_id"),
                        rs.getLong("room_id"),
                        rs.getLong("assigned_to"),
                        rs.getString("status"),
                        rs.getTimestamp("scheduled_for"),
                        rs.getTimestamp("completed_at"),
                        rs.getString("notes"),
                        rs.getTimestamp("created_at"),
                        rs.getTimestamp("updated_at")
                );

                serviceTasks.add(serviceTask);
            }
        }

        return serviceTasks;
    }

    private List<ServiceItem> getServiceItems() throws SQLException {
        List<ServiceItem> serviceItems = new ArrayList<>();

        try (Statement statement = conn.createStatement()) {
            ResultSet rs = statement.executeQuery("select * from serviceitems");

            while (rs.next()) {
                ServiceItem serviceItem = new ServiceItem(
                        rs.getLong("id"),
                        rs.getInt("service_id"),
                        rs.getString("name"),
                        rs.getString("category"),
                        rs.getString("description"),
                        rs.getBigDecimal("price"),
                        rs.getString("duration"),
                        rs.getBoolean("active"),
                        rs.getTimestamp("created_at"),
                        rs.getTimestamp("updated_at")
                );

                serviceItems.add(serviceItem);
            }
        }

        return serviceItems;
    }

    private List<Employee> getEmployees() throws SQLException {
        List<Employee> employees = new ArrayList<>();

        try (Statement statement = conn.createStatement()) {
            ResultSet rs = statement.executeQuery("select * from employees");

            while (rs.next()) {
                Employee employee = new Employee(
                        rs.getLong("id"),
                        rs.getString("employeeid"),
                        rs.getString("firstname"),
                        rs.getString("lastname"),
                        rs.getString("email"),
                        rs.getString("phonenumber"),
                        rs.getInt("address_id"),
                        rs.getDate("dateofbirth"),
                        Gender.valueOf(rs.getString("gender").toUpperCase()),
                        rs.getString("jobposition"),
                        rs.getString("department"),
                        rs.getBigDecimal("salary"),
                        rs.getDate("hiredate"),
                        EmploymentStatus.valueOf(rs.getString("employmentstatus").toUpperCase()),
                        rs.getTimestamp("created_at"),
                        rs.getTimestamp("updated_at")
                );

                employees.add(employee);
            }
        }

        return employees;
    }
}