import express from "express";
const router = express.Router();

import { submitFeedBack, getReviews } from "../controllers/submitFeedBack";
import { feedbackLimiter } from "../middleware/rateLimiter";
import validateFeedback from "../validations/feedback.validation";

router.post("/feedback", feedbackLimiter, validateFeedback, submitFeedBack);
router.get("/reviews", feedbackLimiter, getReviews);

export default router;