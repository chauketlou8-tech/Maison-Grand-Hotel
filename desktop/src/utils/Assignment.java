package utils;

import records.Settings;
import repositories.ServerRepository;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public final class Assignment {

    private static final Connection connection;

    static {
        try {
            connection = Conn.getConnection();
        }

        catch (IOException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static final Settings settings;

    static {
        try {
            settings = ServerRepository.getSettings();
        }

        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static final String assignment = settings.housekeepingAssignment();

    private Assignment() {}

    public static Long pick(String role, int index, int floor) throws SQLException {
        long[] ids = switch (role) {
            case "Housekeeping" -> getActiveHousekeeping();
            case "Maintenance"  -> getActiveMaintenance();
            case "Management"   -> getActiveManagement();
            case "Front Desk / Guest Services" -> getActiveFrontDesk();
            case "Kitchen / Restaurant" -> getActiveKitchen();
            case "Administrative" -> getActiveAdministrative();
            case "Temporary / Contract" -> getActiveTemporary();
            case "Management Trainees / Interns" -> getActiveTrainees();
            case "housekeepers" -> getActiveHousekeepers();
            case "supervisors"  -> getActiveSupervisors();
            case "housekeepingManager" -> getActiveHousekeepingManager();
            case "laundry"      -> getActiveLaundry();
            case "maintenance"  -> getActiveMaintenanceTechs();
            case "maintenanceManager" -> getActiveMaintenanceManager();
            case "chefs"        -> getActiveChefs();
            case "waiters"      -> getActiveWaiters();
            case "frontDesk"    -> getActiveFrontDeskStaff();
            case "concierge"    -> getActiveConcierge();
            case "management"   -> getActiveManagementStaff();
            case "hr"           -> getActiveHR();
            case "finance"      -> getActiveFinance();
            case "it"           -> getActiveIT();
            case "admin"        -> getActiveAdmin();
            case "trainees"     -> getActiveTraineesStaff();
            case "temporary"    -> getActiveTemporaryStaff();
            default -> new long[0];
        };

        return pickAssignee(index, floor, ids);
    }

    // ── Department groups ──────────────────────────────────────

    public static long[] getActiveHousekeeping() throws SQLException {
        return query("select id from employees where department = 'Housekeeping' and employmentstatus = 'ACTIVE' order by id");
    }

    public static long[] getActiveMaintenance() throws SQLException {
        return query("select id from employees where department = 'Maintenance' and employmentstatus = 'ACTIVE' order by id");
    }

    public static long[] getActiveManagement() throws SQLException {
        return query("select id from employees where department = 'Management' and employmentstatus = 'ACTIVE' order by id");
    }

    public static long[] getActiveFrontDesk() throws SQLException {
        return query("select id from employees where department = 'Front Desk / Guest Services' and employmentstatus = 'ACTIVE' order by id");
    }

    public static long[] getActiveKitchen() throws SQLException {
        return query("select id from employees where department = 'Kitchen / Restaurant' and employmentstatus = 'ACTIVE' order by id");
    }

    public static long[] getActiveAdministrative() throws SQLException {
        return query("select id from employees where department = 'Administrative' and employmentstatus = 'ACTIVE' order by id");
    }

    public static long[] getActiveTemporary() throws SQLException {
        return query("select id from employees where department = 'Temporary / Contract' and employmentstatus = 'ACTIVE' order by id");
    }

    public static long[] getActiveTrainees() throws SQLException {
        return query("select id from employees where department = 'Management Trainees / Interns' and employmentstatus = 'ACTIVE' order by id");
    }

    // ── Functional groups ──────────────────────────────────────

    public static long[] getActiveHousekeepers() throws SQLException {
        return query("select id from employees where employmentstatus = 'ACTIVE' and jobposition in ('Room Attendant', 'Public Area Attendant', 'Temporary Housekeeper') order by id");
    }

    public static long[] getActiveSupervisors() throws SQLException {
        return query("select id from employees where employmentstatus = 'ACTIVE' and jobposition = 'Housekeeping Supervisor' order by id");
    }

    public static long[] getActiveHousekeepingManager() throws SQLException {
        return query("select id from employees where employmentstatus = 'ACTIVE' and jobposition = 'Housekeeping Manager' order by id");
    }

    public static long[] getActiveLaundry() throws SQLException {
        return query("select id from employees where employmentstatus = 'ACTIVE' and jobposition = 'Laundry Supervisor' order by id");
    }

    public static long[] getActiveMaintenanceTechs() throws SQLException {
        return query("select id from employees where employmentstatus = 'ACTIVE' and jobposition in ('Maintenance Technician', 'Electrician', 'Plumber', 'HVAC Technician') order by id");
    }

    public static long[] getActiveMaintenanceManager() throws SQLException {
        return query("select id from employees where employmentstatus = 'ACTIVE' and jobposition = 'Maintenance Manager' order by id");
    }

    public static long[] getActiveChefs() throws SQLException {
        return query("select id from employees where employmentstatus = 'ACTIVE' and jobposition in ('Executive Chef', 'Sous Chef', 'Chef de Partie', 'Pastry Chef', 'Commis Chef') order by id");
    }

    public static long[] getActiveWaiters() throws SQLException {
        return query("select id from employees where employmentstatus = 'ACTIVE' and jobposition in ('Waiter', 'Bartender', 'Restaurant Supervisor') order by id");
    }

    public static long[] getActiveFrontDeskStaff() throws SQLException {
        return query("select id from employees where employmentstatus = 'ACTIVE' and jobposition in ('Front Desk Supervisor', 'Senior Receptionist', 'Receptionist', 'Night Auditor') order by id");
    }

    public static long[] getActiveConcierge() throws SQLException {
        return query("select id from employees where employmentstatus = 'ACTIVE' and jobposition in ('Concierge', 'Guest Relations Officer') order by id");
    }

    public static long[] getActiveManagementStaff() throws SQLException {
        return query("select id from employees where employmentstatus = 'ACTIVE' and jobposition in ('General Manager', 'Deputy General Manager', 'Operations Manager', 'Front Office Manager') order by id");
    }

    public static long[] getActiveHR() throws SQLException {
        return query("select id from employees where employmentstatus = 'ACTIVE' and jobposition in ('Human Resources Manager', 'Human Resources Officer') order by id");
    }

    public static long[] getActiveFinance() throws SQLException {
        return query("select id from employees where employmentstatus = 'ACTIVE' and jobposition in ('Finance Manager', 'Accountant', 'Payroll Officer') order by id");
    }

    public static long[] getActiveIT() throws SQLException {
        return query("select id from employees where employmentstatus = 'ACTIVE' and jobposition in ('IT Manager', 'IT Support Officer') order by id");
    }

    public static long[] getActiveAdmin() throws SQLException {
        return query("select id from employees where employmentstatus = 'ACTIVE' and jobposition in ('Administrative Officer', 'Administrative Assistant', 'Procurement Officer', 'Sales and Marketing Officer', 'Revenue Manager') order by id");
    }

    public static long[] getActiveTraineesStaff() throws SQLException {
        return query("select id from employees where employmentstatus = 'ACTIVE' and jobposition in ('Management Trainee', 'Intern') order by id");
    }

    public static long[] getActiveTemporaryStaff() throws SQLException {
        return query("select id from employees where employmentstatus = 'ACTIVE' and jobposition in ('Temporary Receptionist', 'Temporary Housekeeper', 'Temporary Waiter') order by id");
    }

    // ── Shared query ───────────────────────────────────────────

    private static long[] query(String sql) throws SQLException {
        List<Long> ids = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {

            while (rs.next()) {
                ids.add(rs.getLong("id"));
            }
        }

        long[] out = new long[ids.size()];

        for (int i = 0; i < ids.size(); i++) {
            out[i] = ids.get(i);
        }

        return out;
    }

    private static Long pickAssignee(int index, int floor, long[] staffIds) {
        if (staffIds.length == 0) {
            return null;
        }

        if ("By floor".equalsIgnoreCase(assignment)) {
            return staffIds[Math.floorMod(floor, staffIds.length)];
        }

        return staffIds[index % staffIds.length];
    }
}