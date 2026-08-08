import org.junit.Test;
import main.models.Service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ServiceModelTest {
    @Test
    public void testServiceModel() {
        Service service = new Service(1, "Room Service", "24-hour in-room dining service", "Food & Beverage", 50.00f, true);

        assertEquals(1, service.getId());
        assertEquals("Room Service", service.getServiceName());
        assertEquals("24-hour in-room dining service", service.getDescription());
        assertEquals("Food & Beverage", service.getCategory());
        assertEquals(50.00f, service.getPrice(), 0.01);
        assertTrue(service.isAvailable());
    }
}