package ui.data;

import enums.RoomStatus;
import models.Booking;
import models.Guest;
import models.Room;

import java.util.Objects;

public record RoomData(Room room, Booking[] bookings, Guest[] guests) {

    public int getRoomNumber() {
        return room.getRoomnumber();
    }

    public String getRoomType() {
        String code = room.getRoomtype();

        if (code == null) {
            return "—";
        }

        return mapRoomType(code);
    }

    private String mapRoomType(String code) {
        return switch (code) {
            case "SI" -> "Single Room";
            case "ST" -> "Standard Room";
            case "DO" -> "Double Room";
            case "QU" -> "Queen Room";
            case "KI" -> "King Room";
            case "DE" -> "Deluxe Room";
            case "EX" -> "Executive Room";
            case "FA" -> "Family Room";
            case "SU" -> "Suite";
            case "PE" -> "Penthouse";
            default -> code;
        };
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
        return room.getRoomstatus().name().charAt(0) + room.getRoomstatus().name().substring(1).toLowerCase();
    }

    public String getCurrentGuest() {
        for (Booking b : bookings) {
            if (b.getRoom_id() != room.getRoomid()) {
                continue;
            }

            if (!"CHECKED_IN".equalsIgnoreCase(b.getBooking_status().name())) {
                continue;
            }

            for (Guest g : guests) {
                if (g.getId() == b.getGuest_id()) {
                    return g.getFirstname() + " " + g.getLastname();
                }
            }
        }

        return "—";
    }

    public long getRoomId() {
        return room.getRoomid();
    }

    public RoomStatus getRoomStatus() {
        return room.getRoomstatus();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof RoomData r)) {
            return false;
        }

        return Objects.equals(getCurrentGuest(), r.getCurrentGuest()) && room.getRoomid() == r.room.getRoomid() && room.getRoomstatus() == r.room.getRoomstatus();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCurrentGuest(), room.getRoomid(), room.getRoomstatus());
    }
}