import express from "express";
const router = express.Router();

import getSettings from "../controllers/settings.controllers"

router.get("/", getSettings);

export default router;