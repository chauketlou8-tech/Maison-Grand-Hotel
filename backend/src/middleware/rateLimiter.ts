import rateLimit from "express-rate-limit";
import { logger } from "../config/logger";
import type { res, req, next } from "../types/express";

export const globalLimiter = rateLimit({
    windowMs: 15 * 60 * 1000,
    max: 1000,
    standardHeaders: true,
    legacyHeaders: false,
    message: { success: false, message: "Too many requests, please try again later." },

    handler: (req: req, res: res, next: next, options) => {
        logger.warn(`Rate limit hit on ${req.method} ${req.originalUrl} from ${req.ip}`);
        res.status(options.statusCode).json(options.message);
    },
});

export const findBookingLimiter = rateLimit({
    windowMs: 15 * 60 * 1000,
    max: 1000,
    standardHeaders: true,
    legacyHeaders: false,
    message: { success: false, message: "Too many requests, please try again later." },

    handler: (req: req, res: res, next: next, options) => {
        logger.warn(`Rate limit hit on ${req.method} ${req.originalUrl} from ${req.ip}`);
        res.status(options.statusCode).json(options.message);
    },
});

export const bookLimiter = rateLimit({
    windowMs: 15 * 60 * 1000,
    max: 1000,
    standardHeaders: true,
    legacyHeaders: false,
    message: { success: false, message: "Too many requests, please try again later." },

    handler: (req: req, res: res, next: next, options) => {
        logger.warn(`Rate limit hit on ${req.method} ${req.originalUrl} from ${req.ip}`);
        res.status(options.statusCode).json(options.message);
    },
});

export const payLimiter = rateLimit({
    windowMs: 15 * 60 * 1000,
    max: 1000,
    standardHeaders: true,
    legacyHeaders: false,
    message: { success: false, message: "Too many requests, please try again later." },

    handler: (req: req, res: res, next: next, options) => {
        logger.warn(`Rate limit hit on ${req.method} ${req.originalUrl} from ${req.ip}`);
        res.status(options.statusCode).json(options.message);
    },
});