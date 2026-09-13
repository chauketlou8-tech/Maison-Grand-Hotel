package models;

import enums.RoomStatus;

import java.io.Serializable;

public class Room implements Serializable {

    private final long roomid;
    private final int roomnumber;
    private final int floornumber;
    private final String roomtype;
    private final float price;
    private final int capacity;
    private final RoomStatus roomstatus;
    private final String description;

    public Room(long roomid, int roomnumber, int floornumber, String roomtype, float price, int capacity, RoomStatus roomstatus, String description) {
        this.roomid = roomid;
        this.roomnumber = roomnumber;
        this.floornumber = floornumber;
        this.roomtype = roomtype;
        this.price = price;
        this.capacity = capacity;
        this.roomstatus = roomstatus;
        this.description = description;
    }

    public long getRoomid() {
        return this.roomid;
    }

    public int getRoomnumber() {
        return this.roomnumber;
    }

    public int getFloornumber() {
        return this.floornumber;
    }

    public String getRoomtype() {
        return this.roomtype;
    }

    public float getPrice() {
        return this.price;
    }

    public int getCapacity() {
        return this.capacity;
    }

    public RoomStatus getRoomstatus() {
        return this.roomstatus;
    }

    public String getDescription() {
        return this.description;
    }

    @Override
    public String toString() {
        return "{\n" +
                "  \"roomid\": " + roomid + ",\n" +
                "  \"roomnumber\": " + roomnumber + ",\n" +
                "  \"floornumber\": " + floornumber + ",\n" +
                "  \"roomtype\": \"" + roomtype + "\",\n" +
                "  \"price\": " + price + ",\n" +
                "  \"capacity\": " + capacity + ",\n" +
                "  \"roomstatus\": \"" + roomstatus + "\",\n" +
                "  \"description\": \"" + description + "\"\n" +
                "}";
    }
}