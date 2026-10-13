import axios from "axios";

export default async function findBooking(email: string) {
    const { data } = await axios.post("/api/v1/book/find", {
        email
    });

    return data.data;
}