import app from "./app";
import { db_url, port } from "./src/config/env";
import { logger } from "./src/config/logger";
import connectDB from "./src/config/db";
import websocket from "./src/sockets/websock";
import "./src/jobs/cancelExpiredReservations";

class Server {
    public async start() {
        try {
            connectDB(db_url!);
            void websocket;

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