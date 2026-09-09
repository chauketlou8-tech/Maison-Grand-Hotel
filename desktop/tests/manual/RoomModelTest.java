package manual;

import models.Room;
import enums.RoomStatus;

public class RoomModelTest {

    public static void main(String[] args) {
        Room room = new Room(1, 101, 1, "Standard", 1500.00f, 2, RoomStatus.AVAILABLE, "Standard room with queen bed and sea view");
        System.out.println(room);
    }
}