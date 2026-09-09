import org.junit.Test;
import static org.junit.Assert.*;
import models.User;

import java.util.Date;

import enums.Role;
import enums.AccountStatus;

public class UserModelTest {
    @Test
    public void testUserModel() {
        User user = new User(21, "Tlou Elvis", "Chauke", "chauketlou8@gmail.com", "12345password", Role.ADMIN, AccountStatus.ACTIVE, new Date(), new Date());

        assertEquals("Tlou Elvis", user.getFirstName());
        assertEquals("Chauke", user.getLastName());
    }
}
