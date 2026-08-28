import type { Room } from "../types/room"

export default function createGuestID(room: Room): string {
    const floorNumber = room?.floornumber.toString().padStart(2, "0");
    const roomNumber = room?.roomnumber.toString().padStart(3, "0");

    return `G${floorNumber}-${room.roomtype}-${roomNumber}`;
}