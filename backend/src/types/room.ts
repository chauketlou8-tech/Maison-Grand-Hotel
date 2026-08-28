enum RoomStatus {
    AVAILABLE,
    OCCUPIED,
    MAINTENANCE,
    CLOSED
}

export type Room = {
    roomid: number;
    roomnumber: number;
    floornumber: number;
    roomtype: string;
    price: number;
    capacity: number;
    roomstatus: RoomStatus;
    description: string;
}