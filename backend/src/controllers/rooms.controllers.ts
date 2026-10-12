import asyncHandler from "../middleware/asyncHandler";
import type { req, res } from "../types/express";
import type { Room } from "../types/room";
import HelperFunctions from "../utils/helpers";
import Response from "../utils/response";

const getAllRooms = asyncHandler(async (req: req, res: res) => {
    const rooms: Room[] = await HelperFunctions.getRooms();
    return Response.success(res, {
        data: rooms,
    })
});

export default getAllRooms;