import express from "express";
import errorHandler from "./src/middleware/errorHandler";
import routes from "./src/routes";
import { globalLimiter } from "./src/middleware/rateLimiter";

const app = express();

app.use(express.json({
    verify: (req: any, res, buf) => {
        if (req.originalUrl === "/api/v1/payment/pay/webhooks/yoco") {
            req.rawBody = Buffer.from(buf);
        }
    }
}));

app.use(globalLimiter);

app.use("/api/v1", routes);

app.use(errorHandler);

export default app;