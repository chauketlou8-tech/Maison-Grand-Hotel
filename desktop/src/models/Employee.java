package models;

import java.util.Date;
import enums.Gender;
import enums.EmploymentStatus;
import records.Address;

public class Employee {

    private final long id;
    private final String employeeid;
    private final String firstname;
    private final String lastname;
    private final String email;
    private final String phonenumber;
    private final Address address;
    private final Date dateofbirth;
    private final Gender gender;
    private final String jobposition;
    private final String department;
    private final float salary;
    private final Date hiredate;
    private final EmploymentStatus employmentstatus;

    public Employee(long id, String employeeid, String firstname, String lastname, String email, String phonenumber, Address address, Date dateofbirth, Gender gender, String jobposition, String department, float salary, Date hiredate, EmploymentStatus employmentstatus) {
        this.id = id;
        this.employeeid = employeeid;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.phonenumber = phonenumber;
        this.address = address;
        this.dateofbirth = dateofbirth;
        this.gender = gender;
        this.jobposition = jobposition;
        this.department = department;
        this.salary = salary;
        this.hiredate = hiredate;
        this.employmentstatus = employmentstatus;
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

    public Address getAddress() {
        return this.address;
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

    public float getSalary() {
        return this.salary;
    }

    public Date getHiredate() {
        return this.hiredate;
    }

    public EmploymentStatus getEmploymentstatus() {
        return this.employmentstatus;
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
                "  \"address\": " + address + ",\n" +
                "  \"dateofbirth\": \"" + dateofbirth + "\",\n" +
                "  \"gender\": \"" + gender + "\",\n" +
                "  \"jobposition\": \"" + jobposition + "\",\n" +
                "  \"department\": \"" + department + "\",\n" +
                "  \"salary\": " + salary + ",\n" +
                "  \"hiredate\": \"" + hiredate + "\",\n" +
                "  \"employmentstatus\": \"" + employmentstatus + "\"\n" +
                "}";
    }
}