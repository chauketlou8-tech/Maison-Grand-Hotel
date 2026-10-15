import express from "express";
const router = express.Router();

import validateBooking from "../validations/booking.validation"
import { book, findBooking } from "../controllers/booking.controller"
import { bookLimiter, findBookingLimiter } from "../middleware/rateLimiter";

router.post("/", bookLimiter, validateBooking, book);
router.post("/find", findBookingLimiter, findBooking);

export default router;