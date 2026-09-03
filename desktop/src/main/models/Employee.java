package main.models;

import java.util.Date;
import utils.enums.Gender;
import utils.enums.EmploymentStatus;
import utils.types.Address;

/**
 * Represents people who work at Mason Hotel and stores the information needed
 * to manage employees throughout their employment.
 *
 * This class represents all hotel staff members including:
 * - Receptionists
 * - Managers
 * - Housekeeping staff
 * - Maintenance staff
 * - Security staff
 * - Other hotel employees
 *
 * Features:
 * - Stores employee personal information
 * - Stores employment details
 * - Tracks employee positions and departments
 * - Tracks employment status
 * - Supports staff management
 * - Maintains employee records
 */
public class Employee {

    private final long id;
    private final String employeeId;
    private final String firstName;
    private final String lastName;
    private final String email;
    private final String phoneNumber;
    private final Address address;
    private final Date dateOfBirth;
    private final Gender gender;
    private final String jobPosition;
    private final String department;
    private final float salary;
    private final Date hireDate;
    private final EmploymentStatus employmentStatus;

    /**
     * Constructs a new Employee object.
     *
     * @param id               Employee's unique database stored id
     * @param employeeId       Employee's unique identifier
     * @param firstName        Employee's first name
     * @param lastName         Employee's last name
     * @param email            Employee's email address
     * @param phoneNumber      Employee's contact phone number
     * @param address          Employee's residential address
     * @param dateOfBirth      Employee's date of birth
     * @param gender           Employee's gender
     * @param jobPosition      Employee's current job position
     * @param department       Employee's department
     * @param salary           Employee's salary
     * @param hireDate         Date employee was hired
     * @param employmentStatus Employee's current employment status
     */
    public Employee(long id, String employeeId, String firstName, String lastName, String email, String phoneNumber, Address address, Date dateOfBirth, Gender gender, String jobPosition, String department, float salary, Date hireDate, EmploymentStatus employmentStatus) {
        this.id = id;
        this.employeeId = employeeId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.jobPosition = jobPosition;
        this.department = department;
        this.salary = salary;
        this.hireDate = hireDate;
        this.employmentStatus = employmentStatus;
    }

    public long getId() {
        return this.id;
    }

    public String getEmployeeId() {
        return this.employeeId;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public String getEmail() {
        return this.email;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public Address getAddress() {
        return this.address;
    }

    public Date getDateOfBirth() {
        return this.dateOfBirth;
    }

    public Gender getGender() {
        return this.gender;
    }

    public String getJobPosition() {
        return this.jobPosition;
    }

    public String getDepartment() {
        return this.department;
    }

    public float getSalary() {
        return this.salary;
    }

    public Date getHireDate() {
        return this.hireDate;
    }

    public EmploymentStatus getEmploymentStatus() {
        return this.employmentStatus;
    }

    @Override
    public String toString() {
        return "{\n" +
                "  \"employeeId\": \"" + employeeId + "\",\n" +
                "  \"firstName\": \"" + firstName + "\",\n" +
                "  \"lastName\": \"" + lastName + "\",\n" +
                "  \"email\": \"" + email + "\",\n" +
                "  \"phoneNumber\": \"" + phoneNumber + "\",\n" +
                "  \"address\": " + address + ",\n" +
                "  \"dateOfBirth\": \"" + dateOfBirth + "\",\n" +
                "  \"gender\": \"" + gender + "\",\n" +
                "  \"jobPosition\": \"" + jobPosition + "\",\n" +
                "  \"department\": \"" + department + "\",\n" +
                "  \"salary\": " + salary + ",\n" +
                "  \"hireDate\": \"" + hireDate + "\",\n" +
                "  \"employmentStatus\": \"" + employmentStatus + "\"\n" +
                "}";
    }
}