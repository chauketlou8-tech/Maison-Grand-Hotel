import axios from "axios";

export default async function book(bookingDetails: any) {
    const { data } = await axios.post("/api/v1/book", bookingDetails);
    return data.data.guest_id;
}