import express from "express";
const router = express.Router();

import getReservation from "../controllers/reservation.controller"

router.get("/:id", getReservation);

export default router;