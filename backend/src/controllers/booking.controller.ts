import asyncHandler from "../middleware/asyncHandler";
import type { req, res } from "../types/express";
import Response from "../utils/response";

const book = asyncHandler(async (req: req, res: res) => {
    console.log(req.body);

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