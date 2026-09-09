enum RoomStatus {
    AVAILABLE = 'available',
    OCCUPIED = 'occupied',
    MAINTENANCE = 'maintenance',
    CLOSED = 'closed'
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
    created_at: Date;
    updated_at: Date;
};