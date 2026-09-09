package repositories;

import enums.AccountStatus;
import enums.Role;
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
}
