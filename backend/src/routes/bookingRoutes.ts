import express from "express";
const router = express.Router();

import book from "../controllers/booking.controller"

router.post("/", book)

export default router;