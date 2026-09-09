import express from "express"
const router = express.Router();

import validatePayment from "../validations/payment.validation";

import { pay, yocoWebhook } from "../controllers/payment.controller"

router.post("/", validatePayment, pay);
router.post("/webhooks/yoco", yocoWebhook);

export default router;