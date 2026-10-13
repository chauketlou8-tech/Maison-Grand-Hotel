import asyncHandler from "../middleware/asyncHandler";
import type { req, res, next } from "../types/express";
import Response from "../utils/response";
import connection from "../config/connection";
import type { Reservation } from "../types/reservation";

const getReservation = asyncHandler(async (req: req, res: res, next: next) => {
    const id = req.params.id;

    const reservationResults = await connection.query(`
        select r.id, r.requestedroomtype, r.reservationdate, r.checkindate, r.checkoutdate, r.numberofguests, r.reservationstatus, r.selectedpackages, r.requests, r.reservationid, r.created_at, r.updated_at, r.total,
        g.guestid, g.firstname, g.lastname, g.email, g.phonenumber, g.idnumber, g.dateofbirth, g.nationality, g.registrationdate, g.created_at, g.updated_at,
        rm.roomid, rm.roomnumber, rm.floornumber, rm.roomtype, rm.price, rm.capacity, rm.roomstatus, rm.description, rm.created_at, rm.updated_at,
        a.street, a.city, a.province, a.country, a.postalcode, a.created_at, a.updated_at
        from reservations r join guests g on r.guest_id = g.id join rooms rm on r.room_id = rm.roomid join address a on g.address_id = a.id
        where r.reservationid = $1
    `, [id]);

    const reservation: Reservation = reservationResults.rows[0];

    if (!reservation) return Response.notFound(res, {
        message: "Reservation not found",
    });

    return Response.success(res, {
        data: reservation,
    });
});

export default getReservation;