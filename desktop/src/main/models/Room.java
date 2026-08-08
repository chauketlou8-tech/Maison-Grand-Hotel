package main.models;

import utils.enums.RoomStatus;

/**
 * Represents the physical rooms available at Mason Hotel and stores the information
 * needed to manage their accommodation, pricing, and current availability.
 *
 * This class represents all room types including:
 * - Standard rooms
 * - Double rooms
 * - Deluxe rooms
 * - Suites
 * - Other room types
 *
 * Features:
 * - Stores room information
 * - Identifies individual rooms
 * - Tracks room availability
 * - Manages room types
 * - Stores room pricing
 * - Tracks room status
 * - Supports room inventory management
 */
public class Room {

    private final long roomId;
    private final long roomNumber;
    private final long floorNumber;
    private final String roomType;
    private final float price;
    private final long capacity;
    private final RoomStatus roomStatus;
    private final String description;

    /**
     * Constructs a new Room object.
     *
     * @param roomId       Room's unique identifier
     * @param roomNumber   Room's number
     * @param floorNumber  Floor number where the room is located
     * @param roomType     Type of room (Standard, Double, Deluxe, Suite, etc.)
     * @param pricePerNight Price per night for the room
     * @param capacity     Maximum number of guests the room can accommodate
     * @param roomStatus   Current status of the room (Available, Occupied, Maintenance, etc.)
     * @param description  Description of the room and its amenities
     */
    public Room(long roomId, long roomNumber, long floorNumber, String roomType, float pricePerNight, long capacity, RoomStatus roomStatus, String description) {
        this.roomId = roomId;
        this.roomNumber = roomNumber;
        this.floorNumber = floorNumber;
        this.roomType = roomType;
        this.price = pricePerNight;
        this.capacity = capacity;
        this.roomStatus = roomStatus;
        this.description = description;
    }

    public long getRoomId() {
        return this.roomId;
    }

    public long getRoomNumber() {
        return this.roomNumber;
    }

    public long getFloorNumber() {
        return this.floorNumber;
    }

    public String getRoomType() {
        return this.roomType;
    }

    public float getPrice() {
        return this.price;
    }

    public long getCapacity() {
        return this.capacity;
    }

    public RoomStatus getRoomStatus() {
        return this.roomStatus;
    }

    public String getDescription() {
        return this.description;
    }

    @Override
    public String toString() {
        return "{\n" +
                "  \"roomId\": " + roomId + ",\n" +
                "  \"roomNumber\": " + roomNumber + ",\n" +
                "  \"floorNumber\": " + floorNumber + ",\n" +
                "  \"roomType\": \"" + roomType + "\",\n" +
                "  \"price\": " + price + ",\n" +
                "  \"capacity\": " + capacity + ",\n" +
                "  \"roomStatus\": \"" + roomStatus + "\",\n" +
                "  \"description\": \"" + description + "\"\n" +
                "}";
    }
}