//app logic for the routes

import { Router } from "express";

import bookingRoutes from "./bookingRoutes";
import paymentRoutes from "./paymentRoutes";

const router = Router();

router.use("/book", bookingRoutes);
router.use("/payment/pay", paymentRoutes);

export default router;