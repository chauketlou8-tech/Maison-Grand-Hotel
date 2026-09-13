package repositories;

import enums.AccountStatus;
import enums.Role;
import enums.RoomStatus;
import models.Room;
import sockets.Server;
import tasks.CaptureRoomHistoryJob;
import utils.Conn;
import models.User;

import java.io.IOException;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

import java.util.ArrayList;
import java.util.List;

public class ServerRepository {
    public static void connectDB() throws IOException, SQLException {
        Connection conn = Conn.getConnection();

        if (conn != null && !conn.isClosed() && conn.isValid(2)) {
            System.out.println("Database opened successfully.");
        }

        else  {
            System.out.println("Error: Could not open database.");
        }

        Thread socketThread = new Thread(() -> {
            try {
                new Server();
            }

            catch (Exception e) {
                e.printStackTrace();
                try {
                    throw e;
                }

                catch (IOException | SQLException | ClassNotFoundException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        socketThread.setDaemon(true);
        socketThread.start();

        CaptureRoomHistoryJob.run(() -> {
            try {
                return ServerRepository.getRooms().toArray(new Room[0]);
            }

            catch (SQLException | IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public static List<User> getUsers() throws IOException {
        Connection conn = Conn.getConnection();

        assert conn != null;

        try (Statement statement = conn.createStatement()) {
            ResultSet rs = statement.executeQuery("select * from users");

            List<User> users = new ArrayList<>();

            while (rs.next()) {
                User user = new User(
                        rs.getInt("id"),
                        rs.getInt("employee_id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("password_hash"),
                        Role.valueOf(rs.getString("role")),
                        AccountStatus.valueOf(rs.getString("account_status")),
                        rs.getDate("created_at"),
                        rs.getDate("updated_at")
                );

                users.add(user);
            }

            return users;
        }

        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<Room> getRooms() throws SQLException, IOException {
        List<Room> rooms = new ArrayList<>();

        Connection conn = Conn.getConnection();

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
}
