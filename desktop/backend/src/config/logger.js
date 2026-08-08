const winston = require("winston");
const path = require("path");

const logger = winston.createLogger({
    level: "info",
    format: winston.format.combine(
        winston.format.timestamp(),
        winston.format.json()
    ),
    transports: [
        new winston.transports.Console(),
        new winston.transports.File({ filename: "logs/combined.log"}),
        new winston.transports.File({ filename: "logs/error.logs", level: "error" })
    ]
});

module.exports = logger;