//main app logic

import express from "express";
import errorHandler from "./src/middleware/errorHandler";
import routes from "./src/routes";

const app = express();

//middleware
app.use(express.json());

//routes
app.use("/api/v1", routes);

//error handler
app.use(errorHandler);

export default app;