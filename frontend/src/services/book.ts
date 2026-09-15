import axios from "axios";

export default async function book(bookingDetails: any) {
    const { data } = await axios.post("/api/v1/book", bookingDetails);

    const guest_id = data.data.guest_id;
    const reservation_id = data.data.reservation.reservation_id;

    console.log(data);

    return {
        guest_id,
        reservation_id,
    }
}