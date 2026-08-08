//main app logic

const express = require("express");
const errorHandler = require("./src/middleware/errorHandler.js")
const routes = require("./src/routes/index.js");

const app = express();

//middleware
app.use(express.json());

//routes
app.use("/api/v1", routes);

//error handler
app.use(errorHandler);

module.exports = app;