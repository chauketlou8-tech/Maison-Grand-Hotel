import axios from "axios";

export default async function book(bookingDetails: any) {
    await axios.post("/api/v1/book", bookingDetails);
}