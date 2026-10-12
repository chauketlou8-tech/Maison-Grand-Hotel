import express from "express";
const router = express.Router();

import getAllRooms from "../controllers/rooms.controllers";

router.get("/",  getAllRooms);

export default router;