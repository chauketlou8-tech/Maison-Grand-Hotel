import asyncHandler from "../middleware/asyncHandler";
import type { req, res } from "../types/express";
import Response from "../utils/response";
import type { Room } from "../types/room";
import connect from "../config/connection";

const book = asyncHandler(async (req: req, res: res) => {
    const connection = connect;

    const { firstName, lastName, email, phone, requests, guests, selectedPackages } = req.body;
    let { checkIn, checkOut, selectedRoom } = req.body;

    checkIn = checkIn.split("T")[0];
    checkOut = checkOut.split("T")[0];
    selectedRoom = selectedRoom.split(" ")[0];


    const result = await connection.query(`select * from rooms where roomstatus = $1 and roomtype = $2`, ["available", selectedRoom]);
    const availableRooms = result.rows;

    /**
     * checkIn : "2026-08-18"
     * checkOut : "2026-08-19"
     * email : "chauketlou8@gmail.com"
     * firstName : "Tlou"
     * guests : "2"
     * lastName : "Chauke"
     * phone : "+27790433094"
     * requests : ""
     * selectedPackages : ["romance", "breakfast", "transfer"]
     */

    return Response.success(res, {
        message: "Booking received"
    });
});

export default book;