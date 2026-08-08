import { logger } from "../config/logger";
import { node_env } from "../config/env";
import type { req, res, next } from "../types/express";

const errorHandler = (err: { statusCode: number; message: any; stack: any; }, req: req, res: res, next: next) => {
    logger.error(err.stack);
    const statusCode = err.statusCode || 500;
    
    res.status(statusCode).json({
        success: false,
        message: err.message || "Internal Server Error",
        stack: node_env === 'development' ? err.stack : null,
    });
    
    next();
};
    
export default errorHandler;