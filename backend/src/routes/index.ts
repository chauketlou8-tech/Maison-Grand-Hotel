//app logic for the routes

import { Router } from "express";

import bookingRoutes from "./bookingRoutes"

const router = Router();

router.use("/book", bookingRoutes);

export default router;