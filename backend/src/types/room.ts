enum RoomStatus {
    AVAILABLE,
    OCCUPIED,
    MAINTENANCE,
    CLOSED
}

export type Room = {
    roomId: number;
    roomNumber: number;
    floorNumber: number;
    roomType: string;
    price: number;
    capacity: number;
    roomStatus: RoomStatus;
    description: string;
}