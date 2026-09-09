export type Reservation = {
    id: number;
    reservationid: string;
    guest_id: number;
    room_id: number;
    requestedroomtype: string;
    reservationdate: Date;
    checkindate: Date;
    checkoutdate: Date;
    numberofguests: number;
    reservationstatus: 'PENDING' | 'CONFIRMED' | 'CANCELLED' | 'EXPIRED';
    selectedpackages: string[];
    requests: string;
    created_at: Date;
    updated_at: Date;
};