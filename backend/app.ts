//main app logic

import express from "express";
import errorHandler from "./src/middleware/errorHandler";
import routes from "./src/routes";

const app = express();

//middleware
app.use(express.json({
    verify: (req: any, res, buf) => {
        if (req.originalUrl === "/api/v1/payment/pay/webhooks/yoco") {
            req.rawBody = Buffer.from(buf);
        }
    }
}));

//routes
app.use("/api/v1", routes);

//error handler
app.use(errorHandler);

export default app;