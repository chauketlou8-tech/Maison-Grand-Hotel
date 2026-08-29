import axios from "axios";

export default async function pay(amount: number, idNumber: string) {
    const { data } = await axios.post("/api/v1/payment/pay", {
        amount,
        idNumber,
    });

    return data.data.redirectUrl
}