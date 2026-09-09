package manual;

import models.User;
import enums.AccountStatus;
import enums.Role;

import java.util.Date;

public class UserModelTest {

    public static void main(String[] args) {
        User user = new User(21, "Tlou Elvis", "Chauke", "chsuketlou8@gmail.com", "12345password", Role.ADMIN, AccountStatus.ACTIVE, new Date(), new Date());
        System.out.println(user);
    }
}