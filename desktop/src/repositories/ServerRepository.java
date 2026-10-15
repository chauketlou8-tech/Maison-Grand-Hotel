package repositories;

import enums.AccountStatus;
import enums.Role;
import enums.RoomStatus;

import models.Room;
import records.Settings;
import models.User;

import sockets.Server;

import tasks.*;

import utils.Conn;

import java.io.IOException;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.ArrayList;
import java.util.List;

public class ServerRepository {
    private static Connection conn;

    public static void connectDB() throws IOException, SQLException {
        conn = Conn.getConnection();
        Settings settings = getSettings();

        if (conn != null && !conn.isClosed() && conn.isValid(2)) {
            System.out.println("Connected to the database.");
        }

        else {
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

            catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }, settings);

        CreateCleaningTasksJob.run(settings);
        CreateInspectionTasksJob.run(settings);
        NotifyLowOccupancy.run(settings);
        NotifyCheckOutReminder.run(settings);
        CreateDailyReport.run(settings);
        ChargeLateCheckout.run(settings);
    }

    public static List<User> getUsers() throws SQLException {
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
    }

    public static List<Room> getRooms() throws SQLException {
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

    public static Settings getSettings() throws SQLException {
        try (Statement statement = conn.createStatement()) {
            ResultSet rs = statement.executeQuery("select * from settings");

            if (!rs.next()) {
                throw new SQLException("settings table is empty");
            }

            return new Settings(
                    rs.getInt("id"),
                    rs.getString("hotel_name"),
                    rs.getString("hotel_email"),
                    rs.getString("hotel_phone"),
                    rs.getString("hotel_street"),
                    rs.getString("hotel_city"),
                    rs.getString("hotel_province"),
                    rs.getString("hotel_postal_code"),
                    rs.getString("hotel_country"),
                    rs.getTime("check_in_time"),
                    rs.getTime("check_out_time"),
                    rs.getTime("early_check_in_time"),
                    rs.getTime("late_check_out_time"),
                    rs.getInt("min_nights"),
                    rs.getInt("max_nights"),
                    rs.getInt("cancellation_window_hours"),
                    rs.getInt("advance_booking_limit_days"),
                    rs.getBoolean("pets_allowed"),
                    rs.getBoolean("children_allowed"),
                    rs.getBoolean("id_required"),
                    rs.getBigDecimal("vat_rate"),
                    rs.getBigDecimal("service_charge_rate"),
                    rs.getBigDecimal("tourism_levy"),
                    rs.getBoolean("prices_include_vat"),
                    rs.getBoolean("show_tax_breakdown"),
                    rs.getInt("payment_window_minutes"),
                    rs.getBoolean("auto_cancel_unpaid"),
                    rs.getBoolean("notify_reservation_received"),
                    rs.getBoolean("notify_payment_confirmation"),
                    rs.getBoolean("notify_reservation_cancelled"),
                    rs.getBoolean("notify_checkin_reminder"),
                    rs.getBoolean("notify_checkout_reminder"),
                    rs.getBoolean("notify_feedback_request"),
                    rs.getBoolean("notify_staff_new_reservation"),
                    rs.getBoolean("notify_staff_new_payment"),
                    rs.getBoolean("notify_staff_task_completed"),
                    rs.getBoolean("notify_staff_low_occupancy"),
                    rs.getTime("housekeeping_task_time"),
                    rs.getString("housekeeping_default_status"),
                    rs.getString("housekeeping_assignment"),
                    rs.getBoolean("auto_inspection_after_checkout"),
                    rs.getBigDecimal("damage_report_threshold"),
                    rs.getTime("night_audit_time"),
                    rs.getString("night_audit_frequency"),
                    rs.getBoolean("auto_generate_daily_report"),
                    rs.getString("date_format"),
                    rs.getString("time_format"),
                    rs.getInt("grace_minutes"),
                    rs.getInt("full_night_hours"),
                    rs.getInt("escalate_hours"),
                    rs.getBigDecimal("occupancy_threshold"),
                    rs.getBigDecimal("extra_adult_fee"),
                    rs.getBigDecimal("child_fee"),
                    rs.getTimestamp("created_at"),
                    rs.getTimestamp("updated_at")
            );
        }
    }
}