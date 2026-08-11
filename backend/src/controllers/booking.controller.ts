import asyncHandler from "../middleware/asyncHandler";
import type { req, res, next } from "../types/express";
import Response from "../utils/response";
import type { Room } from "../types/room";
import connection from "../config/connection";
import { RoomError, CapacityError } from "../errors"

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
    console.log(reservedRoom.rows);

    return Response.success(res, {
        message: "Booking received"
    });
});

export default book;