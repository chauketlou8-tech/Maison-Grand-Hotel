//app logic for the routes

import { Router } from "express";

import bookingRoutes from "./bookingRoutes";
import paymentRoutes from "./paymentRoutes";
import settingsRoutes from "./settingsRoutes";
import roomsRoutes from "./roomsRoutes";
import reservationRoutes from "./reservationRoutes";

const router = Router();

router.use("/book", bookingRoutes);
router.use("/payment/pay", paymentRoutes);
router.use("/settings", settingsRoutes);
router.use("/rooms", roomsRoutes);
router.use("/reservations", reservationRoutes);

export default router;