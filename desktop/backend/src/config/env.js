const path = require("path");
require("dotenv").config({
    path: path.join(__dirname, "../../.env"),
});


const port = process.env.PORT || 3002;
const node_env = process.env.NODE_ENV;
const db_url = process.env.DATABASE_URL;

if (!node_env) {
    throw new Error('Missing environment environment');
}

if (!db_url && node_env === "development") {
    throw new Error("Database URL missing");
}
    
module.exports = {
    port,
    node_env,
    db_url,
}