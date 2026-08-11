import asyncHandler from "../middleware/asyncHandler";
import type { req, res, next } from "../types/express";
import Response from "../utils/response";
import type { Room } from "../types/room";
import connect from "../config/connection";
import { RoomError } from "../errors"

const book = asyncHandler(async (req: req, res: res, next: next) => {
    const connection = connect;

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

    /**
     *     assign the first available room
     *     const assignedRoom: Room = availableRooms[0];
     *
     *     or
     *
     *     remove it from the available rooms
     *
     *     await connection.query(`delete from rooms where roomid = $1`, [availableRooms[0].roomid]);
     *
     *     or
     *
     *     reserve the first one (probably this one)
     *
     *
      */

    //console.log(availableRooms[0]);

    const reservedRoom = await connection.query(`update rooms set roomStatus = $1 where roomid = $2 returning *`, ['reserved', availableRooms[0].roomid]);
    console.log(reservedRoom.rows);

    return Response.success(res, {
        message: "Booking received"
    });
});

export default book;