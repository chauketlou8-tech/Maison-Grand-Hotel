const logger = require("../config/logger");
const { node_env } = require("../config/env");

const errorHandler = (err, req, res, next) => {
    logger.error(err.stack);
    const statusCode = err.statusCode || 500;
    
    res.status(statusCode).json({
        success: false,
        message: err.message || "Internal Server Error",
        stack: node_env === 'development' ? err.stack : null,
    });
    
    next();
};
    
module.exports = errorHandler;