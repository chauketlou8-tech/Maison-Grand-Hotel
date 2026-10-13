import express from "express";
const router = express.Router();

import validateBooking from "../validations/booking.validation"
import { book, findBooking } from "../controllers/booking.controller"

router.post("/", validateBooking, book);
router.post("/find", findBooking);

export default router;