package data;

import models.Employee;
import models.Settings;
import utils.DateFormats;

import java.util.Objects;

public class EmployeeData {

    private final Employee employee;

    private final String employeeId;
    private final String firstName;
    private final String lastName;
    private final String role;
    private final String department;

    private final String email;
    private final String phone;
    private final String gender;
    private final String salary;
    private final String startDate;

    private final String status;

    public EmployeeData(Employee employee, Settings settings) {
        this.employee = employee;

        this.employeeId = employee.getEmployeeid();
        this.firstName = employee.getFirstname();
        this.lastName = employee.getLastname();
        this.role = employee.getJobposition();
        this.department = employee.getDepartment();

        this.email = employee.getEmail();
        this.phone = employee.getPhonenumber();
        this.gender = employee.getGender() == null ? "—" : employee.getGender().name();

        this.salary = employee.getSalary() == null ? "—" : "R " + String.format("%,.2f", employee.getSalary());

        this.startDate = employee.getHiredate() == null ? "—" : ((java.sql.Date) employee.getHiredate()).toLocalDate().format(DateFormats.of(settings));

        this.status = employee.getEmploymentstatus() == null ? "—" : employee.getEmploymentstatus().name();
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getRole() {
        return role;
    }

    public String getDepartment() {
        return department;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getGender() {
        return gender;
    }

    public String getSalary() {
        return salary;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getStatus() {
        return status;
    }

    public Employee getEmployee() {
        return employee;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof EmployeeData that)) {
            return false;
        }

        return Objects.equals(employeeId, that.employeeId) && Objects.equals(firstName, that.firstName) && Objects.equals(lastName, that.lastName) && Objects.equals(role, that.role) && Objects.equals(department, that.department) && Objects.equals(email, that.email) && Objects.equals(phone, that.phone) && Objects.equals(gender, that.gender) && Objects.equals(salary, that.salary) && Objects.equals(startDate, that.startDate) && Objects.equals(status, that.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(employeeId, firstName, lastName, role, department, email, phone, gender, salary, startDate, status);
    }
}