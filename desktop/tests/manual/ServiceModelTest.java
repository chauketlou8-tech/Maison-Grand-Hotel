package manual;

import main.models.Service;

public class ServiceModelTest {

    public static void main(String[] args) {
        Service service = new Service(1, "Room Service", "24-hour in-room dining service", "Food & Beverage", 50.00f, true);
        System.out.println(service);
    }
}