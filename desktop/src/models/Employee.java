package models;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import enums.Gender;
import enums.EmploymentStatus;

public class Employee implements Serializable {

    private final long id;
    private final String employeeid;
    private final String firstname;
    private final String lastname;
    private final String email;
    private final String phonenumber;
    private final int address_id;
    private final Date dateofbirth;
    private final Gender gender;
    private final String jobposition;
    private final String department;
    private final BigDecimal salary;
    private final Date hiredate;
    private final EmploymentStatus employmentstatus;
    private final Timestamp created_at;
    private final Timestamp updated_at;

    public Employee(long id, String employeeid, String firstname, String lastname, String email, String phonenumber, int address_id, Date dateofbirth, Gender gender, String jobposition, String department, BigDecimal salary, Date hiredate, EmploymentStatus employmentstatus, Timestamp created_at, Timestamp updated_at) {
        this.id = id;
        this.employeeid = employeeid;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.phonenumber = phonenumber;
        this.address_id = address_id;
        this.dateofbirth = dateofbirth;
        this.gender = gender;
        this.jobposition = jobposition;
        this.department = department;
        this.salary = salary;
        this.hiredate = hiredate;
        this.employmentstatus = employmentstatus;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public long getId() {
        return this.id;
    }

    public String getEmployeeid() {
        return this.employeeid;
    }

    public String getFirstname() {
        return this.firstname;
    }

    public String getLastname() {
        return this.lastname;
    }

    public String getEmail() {
        return this.email;
    }

    public String getPhonenumber() {
        return this.phonenumber;
    }

    public int getAddress_id() {
        return this.address_id;
    }

    public Date getDateofbirth() {
        return this.dateofbirth;
    }

    public Gender getGender() {
        return this.gender;
    }

    public String getJobposition() {
        return this.jobposition;
    }

    public String getDepartment() {
        return this.department;
    }

    public BigDecimal getSalary() {
        return this.salary;
    }

    public Date getHiredate() {
        return this.hiredate;
    }

    public EmploymentStatus getEmploymentstatus() {
        return this.employmentstatus;
    }

    public Timestamp getCreated_at() {
        return this.created_at;
    }

    public Timestamp getUpdated_at() {
        return this.updated_at;
    }

    @Override
    public String toString() {
        return "{\n" +
                "  \"id\": " + id + ",\n" +
                "  \"employeeid\": \"" + employeeid + "\",\n" +
                "  \"firstname\": \"" + firstname + "\",\n" +
                "  \"lastname\": \"" + lastname + "\",\n" +
                "  \"email\": \"" + email + "\",\n" +
                "  \"phonenumber\": \"" + phonenumber + "\",\n" +
                "  \"address_id\": " + address_id + ",\n" +
                "  \"dateofbirth\": \"" + dateofbirth + "\",\n" +
                "  \"gender\": \"" + gender + "\",\n" +
                "  \"jobposition\": \"" + jobposition + "\",\n" +
                "  \"department\": \"" + department + "\",\n" +
                "  \"salary\": " + salary + ",\n" +
                "  \"hiredate\": \"" + hiredate + "\",\n" +
                "  \"employmentstatus\": \"" + employmentstatus + "\",\n" +
                "  \"created_at\": \"" + created_at + "\",\n" +
                "  \"updated_at\": \"" + updated_at + "\"\n" +
                "}";
    }
}