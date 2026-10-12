import asyncHandler from "../middleware/asyncHandler";
import connection from "../config/connection";

import Response from "../utils/response";
import HelperFunctions from "../utils/helpers";

import { RoomError, CapacityError } from "../errors";

import type { req, res, next } from "../types/express";

import type { Room } from "../types/room";
import type { Address } from "../types/address";
import type { Settings } from "../types/settings";

import type { Guest } from "../types/guest";
import type { Reservation } from "../types/reservation";
import type { Notification } from "../types/notification";

import { base_url } from "../config/env";

import sendEmail from "../services/email.service";

import getSettings from "../sockets/settingsClient";

const book = asyncHandler(async (req: req, res: res, next: next) => {

    const { firstName, lastName, email, phone, requests, guests, selectedPackages, idNumber, nationality, address, total } = req.body;
    let { checkIn, checkOut, selectedRoom, dateOfBirth } = req.body;

    const settings: Settings | null = getSettings();

    checkIn = checkIn.split("T")[0];
    checkOut = checkOut.split("T")[0];
    dateOfBirth = dateOfBirth.split("T")[0];
    selectedRoom = selectedRoom.split(" ")[0];

    const getter = selectedRoom[0].toUpperCase() + selectedRoom[1].toUpperCase();

    const result = await connection.query(`
        select * from rooms where roomstatus = 'available' and roomtype = $1
    `, [getter]);

    const availableRooms: Room[] = result.rows;

    if (!availableRooms.length) {
        return next(new RoomError("Room is not available.", 404));
    }

    const suitableRoom = availableRooms.find((room: Room) => room.capacity >= guests);

    if (!suitableRoom) {
        return next(new CapacityError("Capacity exceeded: No room available for the number of guests selected.", 409));
    }

    const reservedRoomResult = await connection.query(`
        update rooms set roomStatus = 'reserved' where roomid = $1 returning *
    `, [suitableRoom.roomid]);

    const reservedRoom: Room = reservedRoomResult.rows[0];

    if (!reservedRoom) return;

    await connection.query(`
        insert into roomhistory (roomid, status_date, status)
        values ($1, date_trunc('day', now() at time zone 'Africa/Johannesburg') at time zone 'UTC', $2)
        on conflict (roomid, status_date) do update set status = excluded.status, updated_at = now()
    `, [reservedRoom.roomid, "reserved"]);

    const addressResults = await connection.query(`
        insert into address(street, city, province, country, postalcode)
        values($1, $2, $3, $4, $5)
        returning *
    `, [address.street, address.city, address.province, address.country, address.postalCode]);

    const userAddress: Address = addressResults.rows[0];

    const guestResult = await connection.query(`
                insert into guests(guestid, firstname, lastname, email, phonenumber, address_id, idnumber, dateofbirth, nationality, registrationdate)
                values($1, $2, $3, $4, $5, $6, $7, $8, $9, $10)
                returning *
        `, [HelperFunctions.createGuestID(reservedRoom), firstName, lastName, email, phone, userAddress.id, idNumber, dateOfBirth, nationality, new Date()]
    );

    const guest: Guest = guestResult.rows[0];

    const reservationsResults = await connection.query(`
        insert into reservations(guest_id, requestedroomtype, reservationdate, checkindate, checkoutdate, numberofguests, selectedpackages, requests, room_id, reservationid, total)
        values ($1, $2, $3, $4, $5, $6, $7, $8, $9, $10, $11)
        returning *
    `, [guest.id, getter, new Date(), checkIn, checkOut, guests, selectedPackages, requests, reservedRoom.roomid, HelperFunctions.generateRandomID(), total]);

    const reservation: Reservation = reservationsResults.rows[0];

    const fullReservationResults = await connection.query(`
        select json_build_object(
                       'guest', json_build_object(
                        'guestid', g.guestid,
                        'firstname', g.firstname,
                        'lastname', g.lastname,
                        'email', g.email,
                        'phonenumber', g.phonenumber,
                        'idnumber', g.idnumber,
                        'dateofbirth', g.dateofbirth,
                        'nationality', g.nationality,
                        'registrationdate', g.registrationdate,
                        'address', json_build_object (
                                'street', a.street,
                                'city', a.city,
                                'province', a.province,
                                'country', a.country,
                                'postalcode', a.postalcode
                                   )
                                ),
                       'room', json_build_object (
                               'roomid', rm.roomid,
                               'roomnumber', rm.roomnumber,
                               'floornumber', rm.floornumber,
                               'roomtype', rm.roomtype,
                               'price', rm.price,
                               'capacity', rm.capacity,
                               'description', rm.description
                               ),
                       'reservation_id', r.reservationid,
                       'requestedRoomType', r.requestedroomtype,
                       'reservationDate', r.reservationdate,
                       'checkInDate', r.checkindate,
                       'checkOutDate', r.checkoutdate,
                       'numberOfGuests', r.numberOfGuests,
                       'reservationStatus', r.reservationStatus,
                       'selectedpackages', r.selectedpackages,
                       'requests', r.requests
               )
                   as Reservation from reservations r join guests g on r.guest_id = g.id
                                                      join address a on g.address_id = a.id join rooms rm on r.room_id = rm.roomid
        where g.id = $1 and r.id = $2 and rm.roomid = $3
    `, [guest.id, reservation.id, reservedRoom.roomid]);

    const name = `${firstName} ${lastName}`;

    const fullReservation: Reservation = fullReservationResults.rows[0].reservation;

    const notification: Notification = {
        type: "reservation_request",
        title: "New Reservation Request",
        message: `${name} has requested a reservation.`,
        reservation_id: reservation.id,
        guest_id: guest.id,
        status: "unread",
        created_at: new Date(),
        updated_at: new Date()
    }

    if (settings?.notify_staff_new_reservation) {
        await connection.query(`
            insert into notifications(type, title, message, reservationid, guestid)
            values($1, $2, $3, $4, $5)
        `, [notification.type, notification.title, notification.message, notification.reservation_id, notification.guest_id]);
    }

    if (settings && settings.notify_reservation_received) {
        const address = [
            settings.hotel_street,
            settings.hotel_city,
            settings.hotel_province,
            settings.hotel_postal_code
        ].filter(Boolean).join(", ");

        const emailSubject = `${settings.hotel_name} — Reservation Received`;

        const emailMessage = `Dear ${name},

Thank you for choosing ${settings.hotel_name}.

We have successfully received your reservation request. Your booking is currently awaiting payment confirmation and will be secured once payment is completed.

RESERVATION DETAILS
─────────────────
Reservation ID: ${reservation.reservationid}
Check-in: ${checkIn}
Check-out: ${checkOut}
Guests: ${guests}
Room: ${getter}

VIEW YOUR BOOKING
─────────────────
To view your booking details at any time, visit:
${base_url}/booking/${reservation.reservationid}

PAYMENT INFORMATION
─────────────────
To confirm your reservation, please complete your payment within the next ${settings.payment_window_minutes} minutes to avoid cancellation.

If you have already made payment, please disregard this notice.

QUESTIONS?
─────────────────
If you have any questions regarding your reservation, please contact us and provide your Reservation ID.

Kind regards,

${settings.hotel_name}
Guest Services
${settings.hotel_email}
${settings.hotel_phone}
${address}
www.maisongrandhotel.co.za
`;

        await sendEmail(email, emailSubject, emailMessage, name);
    }

    return Response.success(res, {
        message: "Booking received",
        data: {
            reservation: fullReservation,
            notification: notification,
            guest_id: guest.id,
        }
    });
});

export default book;