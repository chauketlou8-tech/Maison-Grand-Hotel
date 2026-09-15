package utils;

import models.Booking;
import models.Guest;
import models.Room;

import java.util.HashMap;
import java.util.Map;

public class RoomData {

    private static final Map<String, String> ROOM_TYPE_NAMES = new HashMap<>();
    static {
        ROOM_TYPE_NAMES.put("SI", "Single Room");
        ROOM_TYPE_NAMES.put("ST", "Standard Room");
        ROOM_TYPE_NAMES.put("DO", "Double Room");
        ROOM_TYPE_NAMES.put("QU", "Queen Room");
        ROOM_TYPE_NAMES.put("KI", "King Room");
        ROOM_TYPE_NAMES.put("DE", "Deluxe Room");
        ROOM_TYPE_NAMES.put("EX", "Executive Room");
        ROOM_TYPE_NAMES.put("FA", "Family Room");
        ROOM_TYPE_NAMES.put("SU", "Suite");
        ROOM_TYPE_NAMES.put("PE", "Penthouse");
    }

    private final Room room;
    private final Booking[] bookings;
    private final Guest[] guests;

    public RoomData(Room room, Booking[] bookings, Guest[] guests) {
        this.room = room;
        this.bookings = bookings;
        this.guests = guests;
    }

    public int getRoomNumber() {
        return room.getRoomnumber();
    }

    public String getRoomType() {
        String code = room.getRoomtype();
        if (code == null) return "—";
        return ROOM_TYPE_NAMES.getOrDefault(code, code);
    }

    public int getFloor() {
        return room.getFloornumber();
    }

    public String getDescription() {
        String d = room.getDescription();
        return (d == null || d.isBlank()) ? "—" : d;
    }

    public int getCapacity() {
        return room.getCapacity();
    }

    public String getPrice() {
        return "R " + String.format("%,.2f", room.getPrice());
    }

    public String getStatus() {
        return room.getRoomstatus().name().substring(0, 1) + room.getRoomstatus().name().substring(1).toLowerCase();
    }

    public String getCurrentGuest() {
        for (Booking b : bookings) {
            if (b.getRoom_id() != room.getRoomid()) continue;
            if (!"CHECKED_IN".equalsIgnoreCase(b.getBooking_status().name())) continue;

            for (Guest g : guests) {
                if (g.getId() == b.getGuest_id()) {
                    return g.getFirstname() + " " + g.getLastname();
                }
            }
        }
        return "—";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RoomData)) return false;
        RoomData r = (RoomData) o;
        return room.getRoomid() == r.room.getRoomid()
                && room.getRoomstatus() == r.room.getRoomstatus()
                && java.util.Objects.equals(getCurrentGuest(), r.getCurrentGuest());
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(room.getRoomid(), room.getRoomstatus(), getCurrentGuest());
    }
}