import asyncHandler from "../middleware/asyncHandler";
import type { req, res, next } from "../types/express";
import Response from "../utils/response";
import type { Room } from "../types/room";
import type { Address } from "../types/address";
import type { Guest } from "../types/guest";
import connection from "../config/connection";
import { RoomError, CapacityError } from "../errors";
import createGuestID from "../utils/createGuestID"

const book = asyncHandler(async (req: req, res: res, next: next) => {

    const { firstName, lastName, email, phone, requests, guests, selectedPackages } = req.body;
    let { checkIn, checkOut, selectedRoom } = req.body;

    checkIn = checkIn.split("T")[0];
    checkOut = checkOut.split("T")[0];
    selectedRoom = selectedRoom.split(" ")[0];


    const result = await connection.query(`select * from rooms where roomstatus = $1 and roomtype = $2`, ["available", selectedRoom]);
    const availableRooms: Room[] = result.rows;

    if (!availableRooms.length) {
        return next(new RoomError("Room is not available.", 404));
    }

    const suitableRoom = availableRooms.find((room: Room) => room.capacity >= guests);

    if (!suitableRoom) {
        return next(new CapacityError("Capacity exceeded: No room available for the number of guests selected.", 409));
    }

    const reservedRoom = await connection.query(`update rooms set roomStatus = $1 where roomid = $2 returning *`, ['reserved', suitableRoom.roomid]);

    const addressResults = await connection.query(`insert into address(street, city, province, country, postalcode) values($1, $2, $3, $4, $5) returning *`, ["", "", "", "", ""]);
    const userAddress: Address = addressResults.rows[0];

    const guestResult = await connection.query(`insert into guests(guestid, firstname, lastname, email, phonenumber, address_id, idnumber, dateofbirth, nationality, registrationdate)
                                                                values($1, $2, $3, $4, $5, $6, $7, $8, $9, $10)`,
                                                                [createGuestID(selectedRoom), firstName, lastName, email, phone, userAddress.id, "", new Date(), "", new Date()]
        );

    const guest: Guest = guestResult.rows[0];



    return Response.success(res, {
        message: "Booking received"
    });
});

export default book;