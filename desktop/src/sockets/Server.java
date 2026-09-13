package sockets;

import enums.NotificationStatus;
import enums.NotificationType;
import enums.ReservationStatus;
import enums.RoomStatus;
import models.Notification;
import models.Room;
import models.Reservation;
import records.Payment;
import records.RoomHistory;
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

        System.out.println("Server started on port 3000...");

        Socket server = serverSocket.accept();

        System.out.println("Waiting for connection...");

        ObjectOutputStream out = new ObjectOutputStream(server.getOutputStream());
        ObjectInputStream in = new ObjectInputStream(server.getInputStream());

        while (!server.isClosed()) {
            Object message = in.readObject();

            System.out.println("Received: " + message);

            Room[] rooms = this.getRooms().toArray(new Room[0]);
            Notification[] notifications = this.getNotifications().toArray(new Notification[0]);
            Reservation[] reservations = this.getReservations().toArray(new Reservation[0]);
            Payment[] payments = this.getPayments().toArray(new Payment[0]);
            RoomHistory[] roomHistory = this.getRoomHistory().toArray(new RoomHistory[0]);

            out.writeObject(rooms);
            out.writeObject(notifications);
            out.writeObject(reservations);
            out.writeObject(payments);
            out.writeObject(roomHistory);
            out.flush();
            out.reset();
        }

        server.close();
        serverSocket.close();
    }

    private List<Room> getRooms() throws SQLException {
        List<Room> rooms = new ArrayList<>();

        assert conn != null;

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

    private List<Notification> getNotifications() throws IOException, SQLException {
        List<Notification> notifications = new ArrayList<>();

        assert conn != null;

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

    private List<Reservation> getReservations() throws IOException, SQLException {
        List<Reservation> reservations = new ArrayList<>();

        assert conn != null;

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
                        rs.getTimestamp("created_at"),
                        rs.getTimestamp("updated_at")
                );

                reservations.add(reservation);
            }
        }

        return reservations;
    }

    private List<Payment> getPayments() throws IOException, SQLException {
        List<Payment> payments = new ArrayList<>();

        assert conn != null;

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

    private List<RoomHistory> getRoomHistory() throws IOException, SQLException {
        List<RoomHistory> roomHistory = new ArrayList<>();

        assert conn != null;

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
}