import axios from "axios";

export default async function pay(amount: number, guest_id: number, reservation_id: string) {
    const { data } = await axios.post("/api/v1/payment/pay", {
        amount,
        guest_id,
        reservation_id,
    });

    return data.data.redirectUrl
}