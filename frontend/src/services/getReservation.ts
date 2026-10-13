import axios from "axios";
import type { Reservation } from "../types/Reservation"

export default async function getReservation(reservationid: string) {
    const { data } = await axios.get(`/api/v1/reservations/${reservationid}`);
    const reservation: Reservation = data.data;
    return reservation;
}