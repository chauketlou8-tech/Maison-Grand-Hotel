import express from "express";
const router = express.Router();

import validateBooking from "../validations/booking.validation"
import book from "../controllers/booking.controller"

router.post("/", validateBooking, book)

export default router;