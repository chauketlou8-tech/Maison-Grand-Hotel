import axios from "axios";

export default async function pay(paymentDetails: any) {
    await axios.post("/api/v1/payment/pay", paymentDetails);
}