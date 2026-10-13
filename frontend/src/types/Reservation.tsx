export type Reservation = {
    id: number;
    reservationid: string;
    requestedroomtype: string;
    reservationdate: string;
    checkindate: string;
    checkoutdate: string;
    numberofguests: number;
    reservationstatus: 'PENDING' | 'CONFIRMED' | 'CANCELLED' | 'EXPIRED' | 'PROCESSING';
    selectedpackages: string[];
    requests: string;
    total: number;
    created_at: string;
    updated_at: string;

    guest_id: number;
    firstname: string;
    lastname: string;
    email: string;
    phonenumber: string;
    idnumber: string;
    dateofbirth: string;
    nationality: string;

    roomid: number;
    roomnumber: number;
    floornumber: number;
    roomtype: string;
    price: number;
    capacity: number;
    roomstatus: string;
    description: string;

    street: string;
    city: string;
    province: string;
    country: string;
    postalcode: string;
};