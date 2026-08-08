const app = require("./app");
const { db_url, port } = require("./src/config/env");
const logger = require("./src/config/logger");
const connectDB = require("./src/config/db");

class Server {
    async start() {
        try {
            await connectDB(db_url);

            logger.info("Connected to database");

            app.listen(port, () => {
                logger.info(`Server started on port ${port}`);
            });

        } catch (err) {
            logger.error(`Error connecting to DB: ${err}`);
            process.exit(1);
        }
    }
}

const server = new Server();
void server.start();