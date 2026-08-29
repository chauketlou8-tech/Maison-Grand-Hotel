import express from "express"
const router = express.Router();

import pay from "../controllers/payment.controller"

router.post("/", pay);

export default router;