import org.junit.Test;
import org.junit.Assert.*;
import main.models.Employee;
import utils.enums.EmploymentStatus;
import utils.enums.Gender;
import utils.types.Address;

import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.assertEquals;

public class EmployeeModelTest {
    @Test
    public void testEmployeeModel() {
        Address address = new Address("08", "Woolsack Drive", null, "Cape Town", null, "Western Cape", "South Africa", "7700");
        Employee employee = new Employee(1, "EMP000", "Tlou Elvis", "Chauke", "chauketlou8@gmail.com", "0790433094", address, new Date(2007, Calendar.JUNE, 23), Gender.MALE, "Developer", "IT", 0, new Date(2026, Calendar.AUGUST, 7), EmploymentStatus.ACTIVE);

        assertEquals("Tlou Elvis", employee.getFirstName());
        assertEquals("Chauke", employee.getLastName());
        assertEquals("chauketlou8@gmail.com", employee.getEmail());
    }
}

