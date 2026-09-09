import org.junit.Test;
import models.Room;
import enums.RoomStatus;

import static org.junit.Assert.assertEquals;

public class RoomModelTest {
    @Test
    public void testRoomModel() {
        Room room = new Room(1, 101, 1, "Standard", 1500.00f, 2, RoomStatus.AVAILABLE, "Standard room with queen bed and sea view");

        assertEquals(1, room.getRoomId());
        assertEquals(101, room.getRoomNumber());
        assertEquals(1, room.getFloorNumber());
        assertEquals("Standard", room.getRoomType());
        assertEquals(1500.00f, room.getPrice(), 0.01);
        assertEquals(2, room.getCapacity());
        assertEquals(RoomStatus.AVAILABLE, room.getRoomStatus());
        assertEquals("Standard room with queen bed and sea view", room.getDescription());
    }
}